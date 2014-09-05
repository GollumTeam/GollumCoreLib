package mods.gollum.core.common.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.blocks.BlockSpawner;
import mods.gollum.core.common.building.Building.Unity;
import mods.gollum.core.common.building.Building.Unity.Content;
import mods.gollum.core.common.tileentities.TileEntityBlockSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

public class Builder {


	public void build(World world, int rotate, Building building, int x, int y, int z) {
		this.build(world, world.rand, x / 16, z / 16, rotate, building, x, y, z);
	}
	
	public void build(World world, Random random, int chunkX, int chunkZ, int rotate, Building building, int initX, int initY, int initZ) {
		
		ModGollumCoreLib.log.info("Create building width matrix : "+building.name+" "+initX+" "+initY+" "+initZ);
		
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
		
		// Parcours la matrice et ajoute des blocks de stone pour les blocks qui s'accroche
		for (int x = 0; x < building.maxX(rotate); x++) {
			for (int y = 0; y < building.maxY(); y++) {
				for (int z = 0; z < building.maxZ(rotate); z++) {
						
						if (!building.isEraseBlock(x, y, z, rotate)) {
							continue;
						}
						
						// Position réél dans le monde du block
						int finalX = initX + x*dx;
						int finalY = initY + y;
						int finalZ = initZ + z*dz;
						world.setBlock(finalX, finalY, finalZ, Block.stone.blockID, 0, 0);
		
				}
			}
		}
		
		// Parcours la matrice et ajoute les blocks
		for (int x = 0; x < building.maxX(rotate); x++) {
			for (int y = 0; y < building.maxY(); y++) {
				for (int z = 0; z < building.maxZ(rotate); z++) {
					
					Unity unity = building.get(x, y, z, rotate);
					
					if (unity == null) {
						continue;
					}
					
					// Position réél dans le monde du block
					int finalX = initX + x*dx;
					int finalY = initY + y;
					int finalZ = initZ + z*dz;
					
					if (unity.block != null) {
						world.setBlock(finalX, finalY, finalZ, unity.block.blockID, unity.metadata, 0);
					} else {
						world.setBlock(finalX, finalY, finalZ, 0, 0, 2);
					}
					
					this.setOrientation (world, finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation));
					this.setContents    (world, random, finalX, finalY, finalZ, unity.contents);
					this.setExtra       (world, random, finalX, finalY, finalZ, unity.extra, initX, initY, initZ, rotate, building.maxX(rotate), building.maxZ(rotate));
				}
			}
		}
		
		
		//////////////////////////////////
		// Ajoute les blocks aléatoires //
		//////////////////////////////////
		
		for(ArrayList<Building> group: building.getRandomBlocksGroup()) {
			
			Building lisBlockRandom = group.get(random.nextInt(group.size ()));
			
			for (int x = 0; x < lisBlockRandom.maxX(rotate); x++) {
				for (int y = 0; y < lisBlockRandom.maxY(); y++) {
					for (int z = 0; z < lisBlockRandom.maxZ(rotate); z++) {
						Unity unity = lisBlockRandom.get(x, y, z, rotate);
						
						// Position réél dans le monde du block
						int finalX = initX + x*dx;
						int finalY = initY + y;
						int finalZ = initZ + z*dz;
						
						if (unity != null && unity.block != null && unity.block.blockID != 0) {
							world.setBlock(finalX, finalY, finalZ, unity.block.blockID, unity.metadata, 0);
							
							this.setOrientation (world, finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation));
							this.setContents    (world, random, finalX, finalY, finalZ, unity.contents);
							this.setExtra       (world, random, finalX, finalY, finalZ, unity.extra, initX, initY, initZ, rotate, building.maxX(rotate), building.maxZ(rotate));
						}
					}
				}
			}
		}
		
		////////////////////////////////////////////////
		// Vide 10 blocs au dessus de la construction //
		////////////////////////////////////////////////
		for (int x = 0; x < building.maxX(rotate); x++) {
			for (int y = building.maxY(); y < building.maxY()+10; y++) {
				for (int z = 0; z < building.maxZ(rotate); z++) {
					// Position réél dans le monde du block
					int finalX = initX + x*dx;
					int finalY = initY + y;
					int finalZ = initZ + z*dz;
					world.setBlock(finalX, finalY, finalZ, 0, 0, 2);
				}
			}
		}
		
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
//		////////////////////////
//		// Notifie les blocks //
//		////////////////////////
//		for (int x = 0; x < building.maxX; x++) {
//			for (int y = building.maxY; y < 256; y++) {
//				for (int z = 0; z < building.maxZ; z++) {
//					// Position réél dans le monde du block
//					int finalX = initX + x;
//					int finalY = initY + y;
//					int finalZ = initZ + z;
//					world.setBlockMetadataWithNotify (finalX, finalY, finalZ, world.getBlockMetadata (finalX, finalY, finalZ), 3);
//				}
//			}
//		}
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
					
					ItemStack itemStack;
					if (content.type == Content.TYPE_ITEM) {
						if (content.metadata == -1) {
							itemStack = new ItemStack(Item.itemsList[content.id], nombre);
						} else {
							itemStack = new ItemStack(Item.itemsList[content.id], nombre, content.metadata);
						}
					} else {
						if (content.metadata == -1) {
							itemStack = new ItemStack(Block.blocksList [content.id], nombre);
						} else {
							itemStack = new ItemStack(Block.blocksList [content.id], nombre, content.metadata);
						}
					}
					((IInventory) te).setInventorySlotContents (i, itemStack);
				}
			}
		}
		
	}
	
	/**
	 * Insert les extras informations du block
	 * @param world
	 * @param random
	 * @param x
	 * @param y
	 * @param x
	 * @param contents
	 * @param initX
	 * @param initY
	 * @param initZ
	 * @param rotate	
	 */
	// TODO revoir le postion
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
		
		
		if (block instanceof BlockCommandBlock) {
			
			TileEntity te  = world.getBlockTileEntity (x, y, z);
			if (te instanceof TileEntityCommandBlock) {
				
				String command = ""; try { command = extra.get("command"); } catch (Exception e) {} command = (command != null) ? command : "";
				
				
				int varX = 0; try { varX = Integer.parseInt(extra.get("x"))*dx; } catch (Exception e) {}
				int varY = 0; try { varY = Integer.parseInt(extra.get("y")); } catch (Exception e) {}
				int varZ = 0; try { varZ = Integer.parseInt(extra.get("z"))*dz; } catch (Exception e) {}
				
				command = command.replace("{$x}", ""+(this.getRotatedX(varX, varZ, rotate, maxX, maxZ) + initX));
				command = command.replace("{$y}", ""+ (varY + initY));
				command = command.replace("{$z}", ""+(this.getRotatedZ(varX, varZ, rotate, maxX, maxZ) + initZ));
				ModGollumCoreLib.log.info("command : "+command);
				
				((TileEntityCommandBlock) te).setCommand(command);
				
			}
			
		}
		
		if (block instanceof BlockSpawner) {
			
			TileEntity te  = world.getBlockTileEntity (x, y, z);
			if (te instanceof TileEntityBlockSpawner) {
				String entity = ""; try { entity = extra.get("entity"); } catch (Exception e) {} entity = (entity != null) ? entity : "Chicken";
				((TileEntityBlockSpawner) te).setModId (entity);
			}
		}
		

		if (block instanceof BlockMobSpawner) {

			TileEntity te  = world.getBlockTileEntity (x, y, z);
			if (te instanceof TileEntityMobSpawner) {
				String entity = ""; try { entity = extra.get("entity"); } catch (Exception e) {} entity = (entity != null) ? entity : "Pigg";
				((TileEntityMobSpawner) te).getSpawnerLogic().setMobID(entity);
			}
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
	private int getRotatedX(int x, int z, int rotate, int maxX, int maxZ) {
		if (rotate == Building.ROTATED_90) {
			return z;
		}
		if (rotate == Building.ROTATED_180) {

			int newX = this.getRotatedX (x, z, Building.ROTATED_90, maxX, maxZ);
			int newZ = this.getRotatedZ (x, z, Building.ROTATED_90, maxX, maxZ);
			
			return this.getRotatedX (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		if (rotate == Building.ROTATED_270) {

			int newX = this.getRotatedX (x, z, Building.ROTATED_180, maxX, maxZ);
			int newZ = this.getRotatedZ (x, z, Building.ROTATED_180, maxX, maxZ);
			
			return this.getRotatedX (newX, newZ, Building.ROTATED_90, maxX, maxZ);
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
	private int getRotatedZ(int x, int z, int rotate, int maxX, int maxZ) {
		if (rotate == Building.ROTATED_90) {
			return maxX - x -1;
		}
		if (rotate == Building.ROTATED_180) {

			int newX = this.getRotatedX (x, z, Building.ROTATED_90, maxX, maxZ);
			int newZ = this.getRotatedZ (x, z, Building.ROTATED_90, maxX, maxZ);
			
			return this.getRotatedZ (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		if (rotate == Building.ROTATED_270) {

			int newX = this.getRotatedX (x, z, Building.ROTATED_180, maxX, maxZ);
			int newZ = this.getRotatedZ (x, z, Building.ROTATED_180, maxX, maxZ);
			
			return this.getRotatedZ (newX, newZ, Building.ROTATED_90, maxX, maxZ);
		}
		return z;
	}
	
	/**
	 * Affecte l'orientation
	 * @param i
	 * @param j
	 * @param k
	 * @param orientation
	 * @param rotation
	 */
	private void setOrientation(World world, int x, int y, int z, int orientation) {
		
		Block block  = Block.blocksList [world.getBlockId (x, y, z)];
		int metadata = world.getBlockMetadata (x, y, z);
		
		if (
			block instanceof BlockTorch ||
			block instanceof BlockButton
		) {
			
			if (orientation == Unity.ORIENTATION_NONE)  { metadata = (metadata & 0x8) + 0; } else 
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 4; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 1; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}

		if (block instanceof BlockDirectional) {
			
			if (orientation == Unity.ORIENTATION_NONE)  { metadata = (metadata & 0xC) + 0; } else 
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0xC) + 0; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0xC) + 2; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0xC) + 3; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0xC) + 1; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}
		
		if (block instanceof BlockDoor) {

			if ((metadata & 0x8) != 0x8) {
				if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x4) + 3; } else 
				if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x4) + 1; } else 
				if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x4) + 2; } else 
				if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x4) + 0; } else 
				{
					ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
				}
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}
		
		if (block instanceof BlockTrapDoor) {
			
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 1; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 0; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}
		
		if (
			block instanceof BlockLadder ||
			block instanceof BlockFurnace ||
			block instanceof BlockChest ||
			block instanceof BlockDispenser ||
			block instanceof BlockPistonBase
		) {
			
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 4; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 5; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}
		
		if (block instanceof BlockStairs) {
			
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0xC) + 2; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0xC) + 3; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0xC) + 0; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0xC) + 1; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}

		if (block instanceof BlockLever) {
			

			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 4; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 1; } else 
			
			if (orientation == Unity.ORIENTATION_BOTTOM_VERTICAL)   { metadata = (metadata & 0x8) + 5; } else 
			if (orientation == Unity.ORIENTATION_BOTTOM_HORIZONTAL) { metadata = (metadata & 0x8) + 6; } else
			
			if (orientation == Unity.ORIENTATION_TOP_VERTICAL)   { metadata = (metadata & 0x8) + 7; } else 
			if (orientation == Unity.ORIENTATION_TOP_HORIZONTAL) { metadata = (metadata & 0x8) + 0; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
			return;
		}
		
	}
	
}
