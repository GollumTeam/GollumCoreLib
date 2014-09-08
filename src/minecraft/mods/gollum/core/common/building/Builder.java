package mods.gollum.core.common.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Building.GroupSubBuildings;
import mods.gollum.core.common.building.Building.ListSubBuildings;
import mods.gollum.core.common.building.Building.SubBuilding;
import mods.gollum.core.common.building.Building.Unity;
import mods.gollum.core.common.building.Building.Unity.Content;
import mods.gollum.core.common.building.Building.Unity3D;
import mods.gollum.core.common.building.handler.BlockCommandBlockBuildingHandler;
import mods.gollum.core.common.building.handler.BlockDirectionalBuildingHandler;
import mods.gollum.core.common.building.handler.BlockDirectionalWithBit1BuildingHandler;
import mods.gollum.core.common.building.handler.BlockDirectionalWithNoneBuildingHandler;
import mods.gollum.core.common.building.handler.BlockDoorBuildingHandler;
import mods.gollum.core.common.building.handler.BlockLeverBuildingHandler;
import mods.gollum.core.common.building.handler.BlockMobSpawnerBuildingHandler;
import mods.gollum.core.common.building.handler.BlockProximitySpawnBuildingHandler;
import mods.gollum.core.common.building.handler.BlockSignBuildingHandler;
import mods.gollum.core.common.building.handler.BlockStairsBuildingHandler;
import mods.gollum.core.common.building.handler.BlockTrapDoorBuildingHandler;
import mods.gollum.core.common.building.handler.BuildingBlockHandler;
import mods.gollum.core.tools.registry.BuildingBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Builder {
	
	public Builder() {
		BuildingBlockRegistry.register(new BlockSignBuildingHandler());
		BuildingBlockRegistry.register(new BlockDirectionalBuildingHandler());
		BuildingBlockRegistry.register(new BlockDirectionalWithNoneBuildingHandler());
		BuildingBlockRegistry.register(new BlockDirectionalWithBit1BuildingHandler());
		BuildingBlockRegistry.register(new BlockTrapDoorBuildingHandler());
		BuildingBlockRegistry.register(new BlockLeverBuildingHandler());
		BuildingBlockRegistry.register(new BlockDoorBuildingHandler());
		BuildingBlockRegistry.register(new BlockStairsBuildingHandler());
		BuildingBlockRegistry.register(new BlockCommandBlockBuildingHandler());
		BuildingBlockRegistry.register(new BlockProximitySpawnBuildingHandler());
		BuildingBlockRegistry.register(new BlockMobSpawnerBuildingHandler());
	}
	
	public void build(World world, SubBuilding subBuilding) {
		this.build(world, subBuilding.orientation, subBuilding.building, subBuilding.x, subBuilding.y, subBuilding.z);
	}
	
	public void build(World world, int rotate, Building building, int initX, int initY, int initZ) {
		
		Random random = world.rand;
		
		ModGollumCoreLib.log.info("Create building width matrix : "+building.name+" "+initX+" "+initY+" "+initZ);

		initY = initY + building.height;
		initY = (initY < 3) ? 3 : initY;
		
		int dx = -1; 
		int dz = 1;
		switch (rotate) {
			case Building.ROTATED_90:
				dx = -1; 
				dz = -1;
				break;
			case Building.ROTATED_180:
				dx = 1; 
				dz = -1;
				break;
			case Building.ROTATED_270:
				dx = 1; 
				dz = 1;
				break;
			default: 
				break;
		}
		
		// Peut etre inutile
		for (Unity3D unity3D : building.unities) {
			
			// Position réél dans le monde du block
			int finalX = initX + unity3D.x(rotate)*dx;
			int finalY = initY + unity3D.y(rotate);
			int finalZ = initZ + unity3D.z(rotate)*dz;
			world.setBlock(finalX, finalY, finalZ, Block.stone.blockID, 0, 0);
			
		}
		
		for (Unity3D unity3D : building.unities) {
			
			Unity unity = unity3D.unity;
			
			// Position réél dans le monde du block
			int finalX = initX + unity3D.x(rotate)*dx;
			int finalY = initY + unity3D.y(rotate);
			int finalZ = initZ + unity3D.z(rotate)*dz;
			
			if (unity.block != null) {
				world.removeBlockTileEntity(finalX, finalY, finalZ);
				world.setBlock(finalX, finalY, finalZ, unity.block.blockID, unity.metadata, 0);
			} else {
				world.setBlock(finalX, finalY, finalZ, 0, 0, 2);
			}
			
			this.setOrientation (world, finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation), rotate);
			this.setContents    (world, random, finalX, finalY, finalZ, unity.contents);
			this.setExtra       (world, random, finalX, finalY, finalZ, unity.extra, initX, initY, initZ, rotate, building.maxX(rotate), building.maxZ(rotate));
		}
		
		
		//////////////////////////////////
		// Ajoute les blocks aléatoires //
		//////////////////////////////////
		
		for(GroupSubBuildings group: building.getRandomGroupSubBuildings()) {
			
			ListSubBuildings randomBuilding = group.get(random.nextInt(group.size ()));
			
			for (SubBuilding subBuilding : randomBuilding) {
				this.build (world, rotate, subBuilding.building, initX+subBuilding.x*dx, initY+subBuilding.y, initZ+subBuilding.z*dz);
			}
		}
//		
//		////////////////////////////////////////////////
//		// Vide 10 blocs au dessus de la construction //
//		////////////////////////////////////////////////
//		for (int x = 0; x < building.maxX(rotate); x++) {
//			for (int y = building.maxY(); y < building.maxY()+10; y++) {
//				for (int z = 0; z < building.maxZ(rotate); z++) {
//					// Position réél dans le monde du block
//					int finalX = initX + x*dx;
//					int finalY = initY + y;
//					int finalZ = initZ + z*dz;
//					world.setBlock(finalX, finalY, finalZ, 0, 0, 2);
//				}
//			}
//		}
//		
//		/////////////////////////////////////////////////////////////
//		// Rempli en dessous du batiment pour pas que ca sois vide //
//		/////////////////////////////////////////////////////////////
//		
//		for (int x = 0; x < building.maxX; x++) {
//			for (int z = 0; z < building.maxZ; z++) {
//				for (int y = -1; true; y--) {
//					int finalX = initX + x*dx;
//					int finalY = initY + y;
//					int finalZ = initZ + z*dz;
//					finalX = initX + x*dx;
//					finalY = initY + y;
//					finalZ = initZ + z*dz;
//					if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
//						break;
//					}
//				}
//			}
//		}
//
//		///////////////////////////////////////////////////////////////////////////////
//		// Crée un escalier sur les block de remplissage pour que ca sois plus jolie //
//		///////////////////////////////////////////////////////////////////////////////
//		
//		// Les bords
//		for (int z = 0; z < building.maxZ; z++) {
//			for (int x = -1 ; x >= -4; x--) {
//				boolean first = true;
//				for (int y = -1; true; y--) {
//					
//					if (x < y) {
//						continue;
//					}
//					
//					int finalX = initX + x*dx;
//					int finalY = initY + y - building.height - 1;
//					int finalZ = initZ + z*dz;
//					if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
//						break;
//					}
//					first = false;
//				}
//				if (first) {
//					break;
//				}
//			}
//		}
//		for (int z = 0; z < building.maxZ; z++) {
//			for (int x = building.maxX ; x < building.maxX+4; x++) {
//				boolean first = true;
//				for (int y = -1; true; y--) {
//					
//					if (building.maxX - x <= y) {
//						continue;
//					}
//					
//					int finalX = initX + x;
//					int finalY = initY + y - building.height - 1;
//					int finalZ = initZ + z;
//					if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
//						break;
//					}
//					first = false;
//				}
//				if (first) {
//					break;
//				}
//			}
//		}
//		for (int x = 0; x < building.maxX; x++) {
//			for (int z = -1 ; z >= -4; z--) {
//				boolean first = true;
//				for (int y = -1; true; y--) {
//					
//					if (z < y) {
//						continue;
//					}
//					
//					int finalX = initX + x;
//					int finalY = initY + y - building.height - 1;
//					int finalZ = initZ + z;
//					if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
//						break;
//					}
//					first = false;
//				}
//				if (first) {
//					break;
//				}
//			}
//		}
//		for (int x = 0; x < building.maxX; x++) {
//			for (int z = building.maxZ ; z < building.maxZ+4; z++) {
//				boolean first = true;
//				for (int y = -1; true; y--) {
//					
//					if (building.maxZ - z <= y) {
//						continue;
//					}
//					
//					int finalX = initX + x;
//					int finalY = initY + y - building.height - 1;
//					int finalZ = initZ + z;
//					if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
//						break;
//					}
//					first = false;
//				}
//				if (first) {
//					break;
//				}
//			}
//		}
//		
//		// Les angles
//		for (int x = -1 ; x >= -4; x--) {
//			for (int z = -1 ; z >= -4; z--) {
//				for (int y = -1; true; y--) {
//					if (Math.abs(x) + Math.abs(z) >= Math.abs(y) + 1) { continue; }
//					if (Math.abs(x) + Math.abs(z) >= 5) { break; }
//					
//					int finalX = initX + x;
//					int finalY = initY + y - building.height - 1;
//					int finalZ = initZ + z;
//					if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
//						break;
//					}
//				}
//			}
//		}
//		for (int x = -1 ; x >= -4; x--) {
//			for (int z = building.maxZ ; z < building.maxZ+4; z++) {
//				for (int y = -1; true; y--) {
//					if (Math.abs(x) + Math.abs(z - building.maxZ) >= Math.abs(y)) { continue; }
//					if (Math.abs(x) + Math.abs(z - building.maxZ) >= 4) { break; }
//					
//					int finalX = initX + x;
//					int finalY = initY + y - building.height - 1;
//					int finalZ = initZ + z;
//					if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
//						break;
//					}
//				}
//			}
//		}
//		for (int x = building.maxX ; x < building.maxX+4; x++) {
//			for (int z = -1 ; z >= -4; z--) {
//				for (int y = -1; true; y--) {
//					if (Math.abs(x - building.maxX) + Math.abs(z) >= Math.abs(y)) { continue; }
//					if (Math.abs(x - building.maxX) + Math.abs(z) >= 4) { break; }
//					
//					int finalX = initX + x;
//					int finalY = initY + y - building.height - 1;
//					int finalZ = initZ + z;
//					if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
//						break;
//					}
//				}
//			}
//		}
//		for (int x = building.maxX ; x < building.maxX+4; x++) {
//			for (int z = building.maxZ ; z < building.maxZ+4; z++) {
//				for (int y = -1; true; y--) {
//					if (Math.abs(x - building.maxX) + Math.abs(z - building.maxZ) >= Math.abs(y) - 1) { continue; }
//					if (Math.abs(x - building.maxX) + Math.abs(z - building.maxZ) >= 3) { break; }
//					
//					int finalX = initX + x;
//					int finalY = initY + y - building.height - 1;
//					int finalZ = initZ + z;
//					if (!placeUnderBlock (world, finalX, finalY, finalZ, y)) {
//						break;
//					}
//				}
//			}
//		}
//		
		////////////////////////
		// Notifie les blocks //
		////////////////////////

		for (Unity3D unity3D : building.unities) {
			
			// Position réél dans le monde du block
			int finalX = initX + unity3D.x(rotate)*dx;
			int finalY = initY + unity3D.y(rotate);
			int finalZ = initZ + unity3D.z(rotate)*dz;
			world.markBlockForUpdate(finalX, finalY, finalZ);
//			world.setBlockMetadataWithNotify (finalX, finalY, finalZ, world.getBlockMetadata (finalX, finalY, finalZ), 3);
			
		}
	}
//	
//	/**
//	 * Place des escalier
//	 */
//	private boolean placeUnderBlock (World world, int finalX, int finalY, int finalZ, int profondeur) {
//		if (
//			world.getBlockId(finalX, finalY, finalZ) != Block.grass.blockID   &&
//			world.getBlockId(finalX, finalY, finalZ) != Block.stone.blockID   &&
//			world.getBlockId(finalX, finalY, finalZ) != Block.dirt.blockID    &&
//			world.getBlockId(finalX, finalY, finalZ) != Block.bedrock.blockID &&
//			finalY > 0
//		) {
//			if (profondeur > -5) {
//				world.setBlock(finalX, finalY, finalZ, Block.grass.blockID, 0, 2);
//			} else {
//				world.setBlock(finalX, finalY, finalZ, Block.stone.blockID, 0, 2);
//			}
//			return true;
//		}
//		
//		return false;
//	}
//	
	/**
	 * Retourne l'orientation retourner en fonction de la rotation
	 * @param rotate
	 * @param orientation
	 * @return
	 */
	private int rotateOrientation(int rotate, int orientation) {
		if (rotate == Building.ROTATED_90) {
			
			switch (orientation) { 
				case Unity.ORIENTATION_UP:
					return Unity.ORIENTATION_RIGTH;
				case Unity.ORIENTATION_RIGTH:
					return Unity.ORIENTATION_DOWN;
				case Unity.ORIENTATION_DOWN:
					return Unity.ORIENTATION_LEFT;
				case Unity.ORIENTATION_LEFT:
					return Unity.ORIENTATION_UP;
					
				case Unity.ORIENTATION_TOP_HORIZONTAL:
					return Unity.ORIENTATION_TOP_VERTICAL;
				case Unity.ORIENTATION_TOP_VERTICAL:
					return Unity.ORIENTATION_TOP_HORIZONTAL;
					
				case Unity.ORIENTATION_BOTTOM_HORIZONTAL:
					return Unity.ORIENTATION_BOTTOM_VERTICAL;
				case Unity.ORIENTATION_BOTTOM_VERTICAL:
					return Unity.ORIENTATION_BOTTOM_HORIZONTAL;
					
				default:
					return Unity.ORIENTATION_NONE;
			}
		}
		if (rotate == Building.ROTATED_180) {
			return this.rotateOrientation(Building.ROTATED_90, this.rotateOrientation(Building.ROTATED_90, orientation));
		}
		if (rotate == Building.ROTATED_270) {
			return this.rotateOrientation(Building.ROTATED_180, this.rotateOrientation(Building.ROTATED_90, orientation));
		}
		return orientation;
	}
	
	/**
	 * Retourne le block
	 * @param world
	 * @param random
	 * @param x
	 * @param y
	 * @param z
	 * @param contents
	 */
	private void setContents(World world, Random random, int x, int y, int z, ArrayList<ArrayList<Content>> contents) {
		
		Block block  = Block.blocksList [world.getBlockId (x, y, z)];
		
		if (block instanceof BlockContainer) {
			
			TileEntity te  = world.getBlockTileEntity (x, y, z);
			if (te instanceof IInventory) {
				
				for (int i = 0; i < contents.size(); i++) {
					
					ArrayList<Content> groupItem = contents.get(i);
					
					// Recupère un item aléatoirement
					Content content = groupItem.get(random.nextInt (groupItem.size()));
					// Calcule le nombre aléatoire d'item
					int diff   = content.max - content.min;
					int nombre = content.min + ((diff > 0) ? random.nextInt (diff) : 0);
					
					if (content.item != null) {
						ItemStack itemStack;
						if (content.metadata == -1) {
							itemStack = new ItemStack(content.item, nombre);
						} else {
							itemStack = new ItemStack(content.item, nombre, content.metadata);
						}
						
						((IInventory) te).setInventorySlotContents (i, itemStack);
					}
				}
			}
		}
		
	}
	
	/**
	 * Insert les extras informations du block
	 */
	private void setExtra(World world, Random random, int x, int y, int z, HashMap<String, String> extra,int initX, int initY, int initZ, int rotate, int maxX, int maxZ) {
		
		Block block  = Block.blocksList [world.getBlockId (x, y, z)];

		int dx = -1; 
		int dz = 1;
		switch (rotate) {
			case Building.ROTATED_90:
				dx = -1; 
				dz = -1;
				break;
			case Building.ROTATED_180:
				dx = 1; 
				dz = -1;
				break;
			case Building.ROTATED_270:
				dx = 1; 
				dz = 1;
				break;
			default: 
				break;
		}
		
		for (BuildingBlockHandler handler : BuildingBlockRegistry.instance().getHandlers()) {
			handler.setExtra(block, world, random, x, y, z, extra, initX, initY, initZ, rotate, dx, dz, maxX, maxZ);
		}
		
	}
	
	/**
	 * Retourne le block
	 * @param x
	 * @param z
	 * @param rotate
	 * @param maxZ
	 * @return
	 */
	public static int getRotatedX(int x, int z, int rotate, int maxX, int maxZ) {
		if (rotate == Building.ROTATED_90) {
			return z;
		}
		if (rotate == Building.ROTATED_180) {

			int newX = getRotatedX (x, z, Building.ROTATED_90, maxX, maxZ);
			int newZ = getRotatedZ (x, z, Building.ROTATED_90, maxX, maxZ);
			
			return getRotatedX (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		if (rotate == Building.ROTATED_270) {

			int newX = getRotatedX (x, z, Building.ROTATED_180, maxX, maxZ);
			int newZ = getRotatedZ (x, z, Building.ROTATED_180, maxX, maxZ);
			
			return getRotatedX (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		return x;
	}
	
	/**
	 * Retourne le block
	 * @param x
	 * @param z
	 * @param rotate
	 * @param maxX
	 * @return
	 */
	public static int getRotatedZ(int x, int z, int rotate, int maxX, int maxZ) {
		if (rotate == Building.ROTATED_90) {
			return maxX - x -1;
		}
		if (rotate == Building.ROTATED_180) {

			int newX = getRotatedX (x, z, Building.ROTATED_90, maxX, maxZ);
			int newZ = getRotatedZ (x, z, Building.ROTATED_90, maxX, maxZ);
			
			return getRotatedZ (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		if (rotate == Building.ROTATED_270) {

			int newX = getRotatedX (x, z, Building.ROTATED_180, maxX, maxZ);
			int newZ = getRotatedZ (x, z, Building.ROTATED_180, maxX, maxZ);
			
			return getRotatedZ (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		return z;
	}
	
	/**
	 * Affecte l'orientation
	 */
	private void setOrientation(World world, int x, int y, int z, int orientation, int rotate) {
		
		Block block  = Block.blocksList [world.getBlockId (x, y, z)];
		int metadata = world.getBlockMetadata (x, y, z);
		
		for (BuildingBlockHandler handler : BuildingBlockRegistry.instance().getHandlers()) {
			handler.setOrientation(world, x, y, z, block, metadata, orientation, rotate);
		}
		
	}
	
}
