package com.gollum.core.common.building;

import static com.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

import com.gollum.core.common.building.Building.GroupSubBuildings;
import com.gollum.core.common.building.Building.ListSubBuildings;
import com.gollum.core.common.building.Building.SubBuilding;
import com.gollum.core.common.building.Building.Unity;
import com.gollum.core.common.building.Building.Unity.Content;
import com.gollum.core.common.building.Building.Unity3D;
import com.gollum.core.common.building.handler.BuildingBlockHandler;
import com.gollum.core.tools.registry.BuildingBlockRegistry;
import com.gollum.core.utils.math.Integer3d;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
		
		
		public WorldServer getWorld() {
			return this.world;
		}
		
		public int getRotate() {
			return this.rotate;
		}
		
		public Building getBuilding() {
			return this.building;
		}

		public Integer3d getPosition() {
			return new Integer3d(this.initX, this.initY, this.initZ);
		}
		
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
				log.debug ("Building placeBlocks : "+building.name+" "+initX+" "+initY+" "+initZ);
				this.placeBlocks(dx, dz);
				log.debug ("Building placeAfterBlocks : "+building.name+" "+initX+" "+initY+" "+initZ);
				placeAfterBlock(dx, dz);
				log.debug ("Building placeBlockRandom : "+building.name+" "+initX+" "+initY+" "+initZ);
				this.placeBlockRandom(dx, dz);
				
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
		
		private boolean setBlock (int x,int y, int z, Block block, int metadata) {
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
		
		private void placeBlockStone(int dx, int dz) {
			// Peut etre inutile
			for (Unity3D unity3D : building.unities) {
				
				// Position réél dans le monde du block
				int finalX = initX + unity3D.x(rotate)*dx;
				int finalY = initY + unity3D.y(rotate);
				int finalZ = initZ + unity3D.z(rotate)*dz;
				
				this.lock();
				this.setBlock (finalX, finalY, finalZ, Blocks.stone, 0);
				
			}
		}
		
		private void placeBlocks(int dx, int dz) {
			ArrayList<Unity3D> placed = new ArrayList<Unity3D>();
			
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
				
				if (unity.after || BuildingBlockRegistry.instance().isAfterBlock(unity.block)) {
					this.setBlock (finalX, finalY, finalZ, Blocks.air, 0);
				} else 
				if (unity.block != null) {
					isPlaced = this.setBlock (finalX, finalY, finalZ, unity.block, this.getMetadata (finalX, finalY, finalZ, unity));
				} else {
					this.setBlock (finalX, finalY, finalZ, Blocks.air, 0);
				}
				
				if (isPlaced) {
					this.setContents    (finalX, finalY, finalZ, unity.contents);
					this.setExtra       (finalX, finalY, finalZ, unity, building.maxX(rotate), building.maxZ(rotate));
					
					placed.add(unity3D);
				}
				
				if (System.currentTimeMillis() - this.timeDisplayProgress > 5000) {
					this.timeDisplayProgress = System.currentTimeMillis();
					log.message("Building step 1 progress "+building.name+" : " + ( (float)this.placeBlockCount / (float)building.unities.size() * 100.0F  ) + "%");
				}
			}
			
			for (Unity3D unity3D : placed) {
				
				this.lock();
					
				boolean isPlaced = false;
				
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
		

		private void placeAfterBlock(int dx, int dz) {
			
			ArrayList<Unity3D> placed = new ArrayList<Unity3D>();
			
			for (Unity3D unity3D : building.unities) {
				
				Unity unity = unity3D.unity;
				
				if (!unity.after && !BuildingBlockRegistry.instance().isAfterBlock(unity.block)) {
					continue;
				}
				
				boolean isPlaced = false;
				
				// Position réél dans le monde du block
				int finalX = initX + unity3D.x(rotate)*dx;
				int finalY = initY + unity3D.y(rotate);
				int finalZ = initZ + unity3D.z(rotate)*dz;
				
				if (unity.block != null) {
//					log.debug("Place after block : "+RegisteredObjects.instance().getRegisterName(unity.block));
					isPlaced = this.setBlock (finalX, finalY, finalZ, unity.block, this.getMetadata (finalX, finalY, finalZ, unity));
				} else {
					this.setBlock (finalX, finalY, finalZ, Blocks.air, 0);
				}
				
				if (isPlaced) {
					this.setContents    (finalX, finalY, finalZ, unity.contents);
					this.setExtra       (finalX, finalY, finalZ, unity, building.maxX(rotate), building.maxZ(rotate));
					
					placed.add(unity3D);
				}
				
				if (System.currentTimeMillis() - this.timeDisplayProgress > 5000) {
					this.timeDisplayProgress = System.currentTimeMillis();
					log.message("Building step 2 progress "+building.name+" : " + ( (float)this.placeBlockCount / (float)building.unities.size() * 100.0F  ) + "%");
				}
			}
			
			for (Unity3D unity3D : placed) {
				
				this.lock();
					
				boolean isPlaced = false;
				
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
		private void setExtra(int x, int y, int z, Unity unity, int maxX, int maxZ) {
			
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
				handler.setExtra(block, world, world.rand, x, y, z, unity, initX, initY, initZ, rotate, dx, dz, maxX, maxZ);
			}
			
		}
		
		/**
		 * Affecte l'orientation
		 */
		private int getMetadata(int x, int y, int z, Unity unity) {
			int metadata = unity.metadata;
			for (BuildingBlockHandler handler : BuildingBlockRegistry.instance().getHandlers()) {
				metadata = handler.getMetadata(world, x, y, z, metadata, unity, rotate);
			}
			return metadata;
			
		}
	}
	
}
