package com.gollum.core.common.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Semaphore;

import com.gollum.core.common.building.Building.GroupSubBuildings;
import com.gollum.core.common.building.Building.ListSubBuildings;
import com.gollum.core.common.building.Building.SubBuilding;
import com.gollum.core.common.building.Building.Unity;
import com.gollum.core.common.building.Building.Unity3D;
import com.gollum.core.common.building.Building.Unity.Content;
import com.gollum.core.common.building.handler.BlockCommandBlockBuildingHandler;
import com.gollum.core.common.building.handler.BlockDirectionalBuildingHandler;
import com.gollum.core.common.building.handler.BlockDirectionalWithBit1BuildingHandler;
import com.gollum.core.common.building.handler.BlockDirectionalWithNoneBuildingHandler;
import com.gollum.core.common.building.handler.BlockDoorBuildingHandler;
import com.gollum.core.common.building.handler.BlockLeverBuildingHandler;
import com.gollum.core.common.building.handler.BlockMobSpawnerBuildingHandler;
import com.gollum.core.common.building.handler.BlockProximitySpawnBuildingHandler;
import com.gollum.core.common.building.handler.BlockSignBuildingHandler;
import com.gollum.core.common.building.handler.BlockStairsBuildingHandler;
import com.gollum.core.common.building.handler.BlockTrapDoorBuildingHandler;
import com.gollum.core.common.building.handler.BuildingBlockHandler;
import com.gollum.core.tools.registry.BuildingBlockRegistry;

import static com.gollum.core.ModGollumCoreLib.log;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class Builder {
	
	public static ArrayList<Thread> currentBuilds = new ArrayList<Thread>();
	
	private final Object lock = new Object();
	
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
		this.build(world, subBuilding.building, subBuilding.orientation, subBuilding.x, subBuilding.y, subBuilding.z);
	}
	
	public void build(World world, Building building, int rotate, int initX, int initY, int initZ) {
		
		BuilderRunnable thread = new BuilderRunnable(world, building, rotate, initX, initY, initZ);
		thread.start();
		this.currentBuilds.add(thread);
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
	
	class BuilderRunnable extends Thread {
		
		WorldServer world;
		Building building;
		int rotate;
		int initX;
		int initY;
		int initZ;
		
		public BuilderRunnable(World world, Building building, int rotate, int initX, int initY, int initZ) {
			this.world    = (WorldServer) world;
			this.building = building;
			this.rotate   = rotate;
			this.initX    = initX;
			this.initY    = initY;
			this.initZ    = initZ;
		}
		
		public void run() {
			
			try {
				
				log.info("Create building width matrix : "+building.name+" "+initX+" "+initY+" "+initZ);
	
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
				
				this.placeBlockStone(dx, dz);
				this.placeBlock(dx, dz);
				this.placeBlockRandom(dx, dz);
				
				notifyBlocks(dx, dz);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		private void notifyBlocks(int dx, int dz) {
			for (Unity3D unity3D : building.unities) {
				
				Unity unity = unity3D.unity;
				
				// Position réél dans le monde du block
				int finalX = initX + unity3D.x(rotate)*dx;
				int finalY = initY + unity3D.y(rotate);
				int finalZ = initZ + unity3D.z(rotate)*dz;
				
				synchronized (lock) {
					world.markBlockForUpdate(finalX, finalY, finalZ);
				}
			}
		}
		
		private void placeBlockStone(int dx, int dz) {
			// Peut etre inutile
			for (Unity3D unity3D : building.unities) {
				
				// Position réél dans le monde du block
				int finalX = initX + unity3D.x(rotate)*dx;
				int finalY = initY + unity3D.y(rotate);
				int finalZ = initZ + unity3D.z(rotate)*dz;
				
				synchronized (lock) {
					world.setBlock(finalX, finalY, finalZ, Blocks.stone, 0, 0);
				}
				
			}
		}

		private void placeBlock(int dx, int dz) {
			ArrayList<Unity3D> afters = new ArrayList<Unity3D>();
			
			for (Unity3D unity3D : building.unities) {
				
				Unity unity = unity3D.unity;
				
				// Position réél dans le monde du block
				int finalX = initX + unity3D.x(rotate)*dx;
				int finalY = initY + unity3D.y(rotate);
				int finalZ = initZ + unity3D.z(rotate)*dz;
				
				synchronized (lock) {
					
					world.removeTileEntity(finalX, finalY, finalZ);
					
					if (
						unity.after  ||
						unity.block instanceof BlockDoor  ||
						unity.block instanceof BlockBed   ||
						unity.block instanceof BlockChest ||
						unity.block instanceof BlockTorch ||
						unity.block instanceof BlockLever ||
						unity.block instanceof BlockSign
					) {
						afters.add(unity3D);
						world.setBlock(finalX, finalY, finalZ, Blocks.air, 0, 0);
					} else if (unity.block != null) {
						world.setBlock(finalX, finalY, finalZ, unity.block, unity.metadata, 0);
					} else {
						world.setBlock(finalX, finalY, finalZ, Blocks.air, 0, 0);
					}
					
					this.setOrientation (finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation));
					this.setContents    (finalX, finalY, finalZ, unity.contents);
					this.setExtra       (finalX, finalY, finalZ, unity.extra, building.maxX(rotate), building.maxZ(rotate));
				}	
			}
			
			for (Unity3D unity3D : afters) {
				
				Unity unity = unity3D.unity;
				
				// Position réél dans le monde du block
				int finalX = initX + unity3D.x(rotate)*dx;
				int finalY = initY + unity3D.y(rotate);
				int finalZ = initZ + unity3D.z(rotate)*dz;
				
				synchronized (lock) {
					world.setBlock(finalX, finalY, finalZ, unity.block, unity.metadata, 0);
				}
				
				this.setOrientation (finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation));
				this.setContents    (finalX, finalY, finalZ, unity.contents);
				this.setExtra       (finalX, finalY, finalZ, unity.extra, building.maxX(rotate), building.maxZ(rotate));
				
			}
		}
		
		private void placeBlockRandom(int dx, int dz) {
			
			for(GroupSubBuildings group: building.getRandomGroupSubBuildings()) {
				
				ListSubBuildings randomBuilding = group.get(world.rand.nextInt(group.size ()));
				
				for (SubBuilding subBuilding : randomBuilding) {
					
					BuilderRunnable thread = new BuilderRunnable(world, subBuilding.building, rotate, initX+subBuilding.x*dx, initY+subBuilding.y, initZ+subBuilding.z*dz);
					thread.run();
				}
			}
		}
		
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
		private void setContents(int x, int y, int z, ArrayList<ArrayList<Content>> contents) {
			
			Block block  = world.getBlock (x, y, z);
			
			if (block instanceof BlockContainer) {
				
				TileEntity te  = world.getTileEntity (x, y, z);
				if (te instanceof IInventory) {
					
					for (int i = 0; i < contents.size(); i++) {
						
						ArrayList<Content> groupItem = contents.get(i);
						
						// Recupère un item aléatoirement
						Content content = groupItem.get(world.rand.nextInt (groupItem.size()));
						// Calcule le nombre aléatoire d'item
						int diff   = content.max - content.min;
						int nombre = content.min + ((diff > 0) ? world.rand.nextInt (diff) : 0);
						
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
		private void setExtra(int x, int y, int z, HashMap<String, String> extra, int maxX, int maxZ) {
			
			Block block  = world.getBlock (x, y, z);
	
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
				handler.setExtra(block, world, world.rand, x, y, z, extra, initX, initY, initZ, rotate, dx, dz, maxX, maxZ);
			}
			
		}
		
		/**
		 * Affecte l'orientation
		 */
		private void setOrientation(int x, int y, int z, int orientation) {
			
			Block block  = world.getBlock (x, y, z);
			int metadata = world.getBlockMetadata (x, y, z);
			
			for (BuildingBlockHandler handler : BuildingBlockRegistry.instance().getHandlers()) {
				handler.setOrientation(world, x, y, z, block, metadata, orientation, rotate);
			}
			
		}
	}
	
}
