package com.gollum.core.common.building;

import static com.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.gollum.core.common.building.Building.GroupSubBuildings;
import com.gollum.core.common.building.Building.ListSubBuildings;
import com.gollum.core.common.building.Building.SubBuilding;
import com.gollum.core.common.building.Building.Unity;
import com.gollum.core.common.building.Building.Unity.Content;
import com.gollum.core.common.building.Building.Unity3D;
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
import com.gollum.core.utils.math.Integer3d;

public class Builder {
	
	public static ArrayList<BuilderRunnable> currentBuilds = new ArrayList<BuilderRunnable>();
	
	public void build(World world, SubBuilding subBuilding, boolean isStaff) {
		this.build(world, subBuilding.building, subBuilding.orientation, subBuilding.x, subBuilding.y, subBuilding.z, isStaff);
	}
	
	public void build(World world, Building building, int rotate, int initX, int initY, int initZ) {
		this.build(world, building, rotate, initX, initY, initZ, false);
	}
	
	public void build(World world, Building building, int rotate, int initX, int initY, int initZ, boolean isStaff) {
		
		BuilderRunnable thread = new BuilderRunnable(world, building, rotate, initX, initY, initZ, isStaff);
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
	
	public static class BuilderRunnable extends Thread {
		
		WorldServer world;
		Building building;
		int rotate;
		int initX;
		int initY;
		int initZ;
		
		public ReentrantLock lockWorld = new ReentrantLock();
		public Object        waiter    = new Object();
		
		
		private Boolean waitForWorld = true;
		private long time = 0;
		private boolean isStaff = false;
		private int placeBlockCount = 0;
		private long timeDisplayProgress = System.currentTimeMillis();
		
		
		public int getRotate() {
			return this.rotate;
		}
		
		public Building getBuilding() {
			return this.building;
		}

		public Integer3d getPosition() {
			return new Integer3d(this.initX, this.initY, this.initZ);
		}
		
//		private class BlockPlacer extends Thread {
//			
//			private BuilderRunnable runnable;
//			private Unity3D unity3D;
//			private int dx;
//			private int dz;
//			
//			public BlockPlacer (BuilderRunnable runnable, Unity3D unity3D, int dx, int dz) {
//				this.runnable = runnable;
//				this.unity3D  = unity3D;
//				this.dx = dx;
//				this.dz = dz;
//			}
//			
//			public void run() {
//				
//				try {
//					
//					Unity unity = unity3D.unity;
//					
//					// Position réél dans le monde du block
//					int finalX = initX + unity3D.x(rotate)*dx;
//					int finalY = initY + unity3D.y(rotate);
//					int finalZ = initZ + unity3D.z(rotate)*dz;
//					
//					boolean isPlaced = false;
//					
//					world.removeTileEntity(finalX, finalY, finalZ);
//					
//	//				if (
//	//					unity.after  ||
//	//					unity.block instanceof BlockDoor  ||
//	//					unity.block instanceof BlockBed   ||
//	//					unity.block instanceof BlockChest ||
//	//					unity.block instanceof BlockTorch ||
//	//					unity.block instanceof BlockLever ||
//	//					unity.block instanceof BlockSign
//	//				) {
//	//					afters.add(unity3D);
//	//					isPlaced = this.runnable.setBlock (world, finalX, finalY, finalZ, Blocks.air, 0);
//	//				} else 
//					if (unity.block != null) {
//						isPlaced = this.runnable.setBlock (world, finalX, finalY, finalZ, unity.block, unity.metadata);
//					} else {
//						isPlaced = this.runnable.setBlock (world, finalX, finalY, finalZ, Blocks.air, 0);
//					}
//					
//					if (isPlaced) {
//						this.runnable.setOrientation (finalX, finalY, finalZ, this.runnable.rotateOrientation(rotate, unity.orientation));
//						this.runnable.setContents    (finalX, finalY, finalZ, unity.contents);
//						this.runnable.setExtra       (finalX, finalY, finalZ, unity.extra, building.maxX(rotate), building.maxZ(rotate));
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
		public BuilderRunnable(World world, Building building, int rotate, int initX, int initY, int initZ, boolean isStaff) {
			this.world    = (WorldServer) world;
			this.building = building;
			this.rotate   = rotate;
			this.initX    = initX;
			this.initY    = initY;
			this.initZ    = initZ;
			this.isStaff  = isStaff;
		}
		
		public void run() {
			this.run(true);
		}
		public void run(boolean reTop) {
			
			try {
				
				log.info("Create building width matrix : "+building.name+" "+initX+" "+initY+" "+initZ);
				
				initY = initY + building.height;
				if (reTop) {
					initY = (initY < 3) ? 3 : initY;
				}
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
				
//				this.placeBlockStone(dx, dz);
				log.debug ("Building placeBlock : "+building.name+" "+initX+" "+initY+" "+initZ);
				this.placeBlock(dx, dz);
				log.debug ("Building placeBlockRandom : "+building.name+" "+initX+" "+initY+" "+initZ);
				this.placeBlockRandom(dx, dz);
				log.debug ("Building notifyBlocks : "+building.name+" "+initX+" "+initY+" "+initZ);
				notifyBlocks(dx, dz);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			log.info("End create building width matrix : "+building.name+" "+initX+" "+initY+" "+initZ);
			
			this.unlockWorld();
		}
		
		public void dontWaitWorld () {
			synchronized (this.waitForWorld) {
				this.waitForWorld = false;
			}
		}
		
		private void lock () {
			
			boolean waitForWorld = true;
			synchronized (this.waitForWorld) {
				waitForWorld = this.waitForWorld;
			}
			try  {
				if (waitForWorld) {
					long lantency = System.currentTimeMillis() - this.time;
					if (lantency > 200) {
						if (lantency >  500 && this.time != 0) {
							log.warning("Latency of builder is gretter that 300 milliseconds. lantency = "+lantency);
						}
						this.unlockWorld();
//						log.debug ("Thread wait server");

						synchronized  (this.waiter) {
							this.waiter.wait();
						}
						this.lockWorld.lock();
						this.time = System.currentTimeMillis();
//						log.debug ("Thread is free lock world for 200ms");
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		synchronized public void unlockWorld () {
			try {
				if (this.lockWorld.isLocked()) {
					this.lockWorld.unlock();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		private boolean setBlock (World world, int x,int y, int z, Block block, int metadata) {
			if (y < 3) {
				return false;
			}
			try {
				return world.setBlock(x, y, z, block, metadata, 0);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		private void notifyBlocks(int dx, int dz) {
			for (Unity3D unity3D : building.unities) {
				
				Unity unity = unity3D.unity;
				
				// Position réél dans le monde du block
				int finalX = initX + unity3D.x(rotate)*dx;
				int finalY = initY + unity3D.y(rotate);
				int finalZ = initZ + unity3D.z(rotate)*dz;
				
				this.lock();
				world.notifyBlocksOfNeighborChange(finalX, finalY, finalZ, unity.block != null ? unity.block : Blocks.air);
//				if (this.isStaff ) {
					world.markBlockForUpdate(finalX, finalY, finalZ);
//				}
			}
		}
		
		private void placeBlockStone(int dx, int dz) {
			// Peut etre inutile
			for (Unity3D unity3D : building.unities) {
				
				// Position réél dans le monde du block
				int finalX = initX + unity3D.x(rotate)*dx;
				int finalY = initY + unity3D.y(rotate);
				int finalZ = initZ + unity3D.z(rotate)*dz;
				
				this.lock();
				this.setBlock (world, finalX, finalY, finalZ, Blocks.stone, 0);
				
			}
		}
		
		private void placeBlock(int dx, int dz) {
			ArrayList<Unity3D> afters = new ArrayList<Unity3D>();
			
//			ArrayList<BlockPlacer> threadsBlockSetter = new ArrayList<BlockPlacer>();
			
			Iterator<Unity3D> i = building.unities.iterator();
			while (i.hasNext()) {
				
				this.lock();
				
				Unity3D unity3D = i.next();
				Unity unity = unity3D.unity;
				
				// Position réél dans le monde du block
				int finalX = initX + unity3D.x(rotate)*dx;
				int finalY = initY + unity3D.y(rotate);
				int finalZ = initZ + unity3D.z(rotate)*dz;
				
				boolean isPlaced = false;
				
				world.removeTileEntity(finalX, finalY, finalZ);
				
				// TODO Add register
				if (
					unity.after || BuildingBlockRegistry.instance().isAfterBlock(unity.block)
				) {
					afters.add(unity3D);
					isPlaced = this.setBlock (world, finalX, finalY, finalZ, Blocks.air, 0);
				} else 
				if (unity.block != null) {
					isPlaced = this.setBlock (world, finalX, finalY, finalZ, unity.block, unity.metadata);
				} else {
					isPlaced = this.setBlock (world, finalX, finalY, finalZ, Blocks.air, 0);
				}
				
				if (isPlaced) {
					this.setOrientation (finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation));
					this.setContents    (finalX, finalY, finalZ, unity.contents);
					this.setExtra       (finalX, finalY, finalZ, unity.extra, building.maxX(rotate), building.maxZ(rotate));
				}
				
//				for (int j = 0; j < 50; j++) {
//					if (i.hasNext()) {
//						Unity3D unity3D = i.next();
//						
//
//						// Position réél dans le monde du block
//						int finalX = initX + unity3D.x(rotate)*dx;
//						int finalZ = initZ + unity3D.z(rotate)*dz;
//						world.getChunkFromBlockCoords(finalX, finalZ); // Pre-Generate chunk
//						
//						BlockPlacer blockPlacer = new BlockPlacer(this, unity3D, dx, dz);
//						threadsBlockSetter.add(blockPlacer);
//						blockPlacer.start();
//						this.placeBlockCount++;
//					}
//				}
//				for (BlockPlacer thread : threadsBlockSetter) {
//					try {
//						thread.join();
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
				
				if (System.currentTimeMillis() - this.timeDisplayProgress > 5000) {
					this.timeDisplayProgress = System.currentTimeMillis();
					log.message("Building progress "+building.name+" : " + ( (float)this.placeBlockCount / (float)building.unities.size() * 100.0F  ) + "%");
				}
			}
			
			for (Unity3D unity3D : afters) {
				
				this.lock();
					
				boolean isPlaced = false;
				
				Unity unity = unity3D.unity;
				
				// Position réél dans le monde du block
				int finalX = initX + unity3D.x(rotate)*dx;
				int finalY = initY + unity3D.y(rotate);
				int finalZ = initZ + unity3D.z(rotate)*dz;
				
				isPlaced = this.setBlock (world, finalX, finalY, finalZ, unity.block, unity.metadata);
				
				if (isPlaced) {
					this.setOrientation (finalX, finalY, finalZ, this.rotateOrientation(rotate, unity.orientation));
					this.setContents    (finalX, finalY, finalZ, unity.contents);
					this.setExtra       (finalX, finalY, finalZ, unity.extra, building.maxX(rotate), building.maxZ(rotate));
				}
				
			}
		}
		
		private void placeBlockRandom(int dx, int dz) {
			
			for(GroupSubBuildings group: building.getRandomGroupSubBuildings()) {
				
				ListSubBuildings randomBuilding = group.get(world.rand.nextInt(group.size ()));
				
				for (SubBuilding subBuilding : randomBuilding) {
					
					BuilderRunnable thread = new BuilderRunnable(world, subBuilding.building, rotate, initX+subBuilding.x*dx, initY+subBuilding.y, initZ+subBuilding.z*dz, isStaff);
					thread.waiter    = this.waiter;
					thread.lockWorld = this.lockWorld;
					thread.run(false);
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
