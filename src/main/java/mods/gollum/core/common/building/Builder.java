package mods.gollum.core.common.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static mods.gollum.core.ModGollumCoreLib.log;
import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Builder.Progress;
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
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Builder {
	
	private World world;
	private Building building;
	private Random random;
	private int rotate;
	private int initX;
	private int initY;
	private int initZ;
	private Progress progress;
	private int dx;
	private int dz;
	private int step;
	private Object[] unities;
	
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
	
	public void build(World world, SubBuilding subBuilding, Progress progress) {
		this.build(world, subBuilding.building, subBuilding.orientation, subBuilding.x, subBuilding.y, subBuilding.z, progress);
	}
	
	public void build(World world, Building building, int rotate, int initX, int initY, int initZ, Progress progress) {
		
		if (progress.iteration == 0) {
			log.info("Create building width matrix : "+building.name+" "+initX+" "+initY+" "+initZ);
		}
		
		initY = initY + building.height;
		initY = (initY < 3) ? 3 : initY;
		
		this.random   = world.rand;
		this.world    = world;
		this.building = building;
		this.rotate   = rotate;
		this.initX    = initX;
		this.initY    = initY;
		this.initZ    = initZ;
		this.progress = progress;
		this.unities  = building.unities.toArray();
		
		this.dx = -1; 
		this.dz = 1;
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
		
		this.step = ( ((this.progress.iteration + 1) / ModGollumCoreLib.config.blockByTick) + 1) * ModGollumCoreLib.config.blockByTick;
		
		this.build();
		
	}
	
	protected void build() {
		
		if (this.progress.iteration < this.building.unities.size()) {
			placeBlockStone();
		}
		
		if (this.stepFinish()) {
			return;
		}
		
		if (
			this.progress.iteration >= this.building.unities.size() &&
			this.progress.iteration < this.building.unities.size() * 2
		) {
			placeBlock();
		}
		
		//////////////////////////////////
		// Ajoute les blocks aléatoires //
		//////////////////////////////////
		
//		for(GroupSubBuildings group: building.getRandomGroupSubBuildings()) {
//			
//			ListSubBuildings randomBuilding = group.get(random.nextInt(group.size ()));
//			
//			for (SubBuilding subBuilding : randomBuilding) {
//				
//				this.build (world, subBuilding.building, rotate, initX+subBuilding.x*dx, initY+subBuilding.y, initZ+subBuilding.z*dz, 0);
//				
//			}
//		}
		
		log.info("End building width matrix : "+building.name+" "+initX+" "+initY+" "+initZ);
		
	}
	
	private void placeBlockStone() {
		// Peut etre inutile
		for (int i = progress.iteration; i < building.unities.size() && !this.stepFinish() ; i++) {
			
			Unity3D unity3D = (Unity3D) this.unities[i];
			
			// Position réél dans le monde du block
			int finalX = initX + unity3D.x(rotate)*dx;
			int finalY = initY + unity3D.y(rotate);
			int finalZ = initZ + unity3D.z(rotate)*dz;
			
			world.setBlock(finalX, finalY, finalZ, Blocks.stone, 0, 0);
			
			progress.iteration++;
			
		}
	}
	
	private void placeBlock() {
		
		ArrayList<Unity3D> afters = new ArrayList<Unity3D>();

		for (int i = progress.iteration - building.unities.size(); i < building.unities.size() && !this.stepFinish(); i++) {
			
			Unity3D unity3D = (Unity3D) this.unities[i];
			Unity   unity   = unity3D.unity;
			
			// Position réél dans le monde du block
			int finalX = initX + unity3D.x(rotate)*dx;
			int finalY = initY + unity3D.y(rotate);
			int finalZ = initZ + unity3D.z(rotate)*dz;
			
			world.removeTileEntity(finalX, finalY, finalZ);
			
			if (
				unity.block instanceof BlockDoor  ||
				unity.block instanceof BlockBed   ||
				unity.block instanceof BlockChest ||
				unity.block instanceof BlockTorch ||
				unity.block instanceof BlockLever ||
				unity.block instanceof BlockSign
			) {
				afters.add(unity3D);
				world.setBlockToAir (finalX, finalY, finalZ);
			} else if (unity.block != null) {
				world.setBlock(finalX, finalY, finalZ, unity.block, unity.metadata, 0);
			} else {
				world.setBlockToAir (finalX, finalY, finalZ);
			}
			
			this.setOrientation (world, finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation), rotate);
			this.setContents    (world, random, finalX, finalY, finalZ, unity.contents);
			this.setExtra       (world, random, finalX, finalY, finalZ, unity.extra, initX, initY, initZ, rotate, building.maxX(rotate), building.maxZ(rotate));
			
			progress.iteration++;
			
		}
		
//		for (Unity3D unity3D : afters) {
//		
//			Unity unity = unity3D.unity;
//			
//			// Position réél dans le monde du block
//			int finalX = initX + unity3D.x(rotate)*dx;
//			int finalY = initY + unity3D.y(rotate);
//			int finalZ = initZ + unity3D.z(rotate)*dz;
//			
//			world.setBlock(finalX, finalY, finalZ, unity.block, unity.metadata, 0);
//			
//			this.setOrientation (world, finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation), rotate);
//			this.setContents    (world, random, finalX, finalY, finalZ, unity.contents);
//			this.setExtra       (world, random, finalX, finalY, finalZ, unity.extra, initX, initY, initZ, rotate, building.maxX(rotate), building.maxZ(rotate));
//		}
	
	}
	
	protected boolean stepFinish () {
		return progress.iteration >= step;
	}
	
	public boolean isFinish(SubBuilding subBuilding, Progress progress) {
		return this.isFinish(subBuilding.building, progress);
	}
	
	public boolean isFinish (Building building, Progress progress) {
		
		return 
			progress.iteration >= building.unities.size() * 2
		;
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
		
		Block block  = world.getBlock (x, y, z);
		
		if (block instanceof BlockContainer) {
			
			TileEntity te  = world.getTileEntity (x, y, z);
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
			handler.setExtra(block, world, random, x, y, z, extra, initX, initY, initZ, rotate, dx, dz, maxX, maxZ);
		}
		
	}
	
	/**
	 * Affecte l'orientation
	 */
	private void setOrientation(World world, int x, int y, int z, int orientation, int rotate) {
		
		Block block  = world.getBlock (x, y, z);
		int metadata = world.getBlockMetadata (x, y, z);
		
		for (BuildingBlockHandler handler : BuildingBlockRegistry.instance().getHandlers()) {
			handler.setOrientation(world, x, y, z, block, metadata, orientation, rotate);
		}
		
	}
	
	public static class Progress {
		
		public int iteration;
		
		public void readEntityFromNBT(NBTTagCompound nbtTagCompound) {
			
			this.iteration = nbtTagCompound.getInteger("iteration");
			
		}
		
		public NBTTagCompound writeEntityToNBT() {
			
			NBTTagCompound nbtTagCompound = new NBTTagCompound();
			
			nbtTagCompound.setInteger("iteration", this.iteration);
			
			return nbtTagCompound;
		}
		
	}
	
}
