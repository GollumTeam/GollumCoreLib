package com.gollum.core.common.building;

import static com.gollum.core.ModGollumCoreLib.logger;

import java.util.ArrayList;
import java.util.Iterator;

import com.gollum.core.common.building.Building.EnumRotate;
import com.gollum.core.common.building.Building.GroupSubBuildings;
import com.gollum.core.common.building.Building.ListSubBuildings;
import com.gollum.core.common.building.Building.SubBuilding;
import com.gollum.core.common.building.Building.Unity;
import com.gollum.core.common.building.Building.Unity.Content;
import com.gollum.core.common.building.Building.Unity3D;
import com.gollum.core.common.building.handler.BuildingBlockHandler;
import com.gollum.core.tools.registered.RegisteredObjects;
import com.gollum.core.tools.registry.BuildingBlockRegistry;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class Builder {
	
	public static ArrayList<BuilderRunnable> currentBuilds = new ArrayList<BuilderRunnable>();
	
	private static Builder _instance = null;
	
	public static Builder instance() {
		if (_instance == null) {
			_instance = new Builder();
		}
		return _instance;
	}
	
	protected Builder() {
	}
	
	public void build(World world, SubBuilding subBuilding, boolean isStaff) {
		this.build(world, subBuilding.building, EnumRotate.getByIndex((subBuilding.facing.getHorizontalIndex()+2) % 4), new BlockPos (subBuilding.x, subBuilding.y, subBuilding.z), isStaff);
	}
	
	public void build(World world, Building building, EnumRotate rotate, BlockPos initPos) {
		this.build(world, building, rotate, initPos, false);
	}
	
	public void build(World world, Building building, EnumRotate rotate, BlockPos initPos, boolean isStaff) {
		
		BuilderRunnable thread = new BuilderRunnable(world, building, rotate, initPos, isStaff);
		thread.start();
		Builder.currentBuilds.add(thread);
	}
	
	/**
	 * Retourne le block
	 * @param x
	 * @param z
	 * @param rotate
	 * @param maxZ
	 * @return
	 */
	public static int getRotatedX(int x, int z, EnumRotate rotate, int maxX, int maxZ) {
		if (rotate == EnumRotate.ROTATED_90) {
			return z;
		}
		if (rotate == EnumRotate.ROTATED_180) {

			int newX = getRotatedX (x, z, EnumRotate.ROTATED_90, maxX, maxZ);
			int newZ = getRotatedZ (x, z, EnumRotate.ROTATED_90, maxX, maxZ);
			
			return getRotatedX (newX, newZ, EnumRotate.ROTATED_90, maxX, maxZ);
		}
		if (rotate == EnumRotate.ROTATED_270) {

			int newX = getRotatedX (x, z, EnumRotate.ROTATED_180, maxX, maxZ);
			int newZ = getRotatedZ (x, z, EnumRotate.ROTATED_180, maxX, maxZ);
			
			return getRotatedX (newX, newZ, EnumRotate.ROTATED_90, maxX, maxZ);
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
	public static int getRotatedZ(int x, int z, EnumRotate rotate, int maxX, int maxZ) {
		if (rotate == EnumRotate.ROTATED_90) {
			return maxX - x -1;
		}
		if (rotate == EnumRotate.ROTATED_180) {

			int newX = getRotatedX (x, z, EnumRotate.ROTATED_90, maxX, maxZ);
			int newZ = getRotatedZ (x, z, EnumRotate.ROTATED_90, maxX, maxZ);
			
			return getRotatedZ (newX, newZ, EnumRotate.ROTATED_90, maxX, maxZ);
		}
		if (rotate == EnumRotate.ROTATED_270) {

			int newX = getRotatedX (x, z, EnumRotate.ROTATED_180, maxX, maxZ);
			int newZ = getRotatedZ (x, z, EnumRotate.ROTATED_180, maxX, maxZ);
			
			return getRotatedZ (newX, newZ, EnumRotate.ROTATED_90, maxX, maxZ);
		}
		return z;
	}
	
	public static class BuilderRunnable extends Thread {
		
		class Waiter {
			public boolean       isBuilderThreadWait = false;
			public boolean       isMainThreadWait    = false;
			private Object       waiterBuilderThread = new Object();;
			private Object       waiterMainThread    = new Object();
			
			public void waitMainThread() {
				try {
					synchronized (this.waiterMainThread) {
						this.isMainThreadWait = true;
						this.waiterMainThread.wait();
					}
				} catch (Exception e) {
					logger.severe("Error on wait main thread", e);
				}
			}
			public void notiyMainThread() {
				try {
					synchronized (this.waiterMainThread) {
						this.isMainThreadWait = false;
						this.waiterMainThread.notify();
					}
				} catch (Exception e) {
					logger.severe("Error on notify main thread", e);
				}
			}
			
			public void waitBuilderThread() {
				try {
					synchronized (this.waiterBuilderThread) {
						this.isBuilderThreadWait = true;
						this.waiterBuilderThread.wait();
					}
				} catch (Exception e) {
					logger.severe("Error on wait builder thread", e);
				}
			}
			public void notiyBuilderThread() {
				try {
					synchronized (this.waiterBuilderThread) {
						this.isBuilderThreadWait = false;
						this.waiterBuilderThread.notify();
					}
				} catch (Exception e) {
					logger.severe("Error on notify builder thread", e);
				}
			}
		}
		
		WorldServer world;
		Building building;
		EnumRotate rotate;
		BlockPos initPos;
		
		
		private Waiter waiter = new Waiter();
		
		private long time = 0;
		private boolean isStaff = false;
		private int placeBlockCount = 0;
		private long timeDisplayProgress = System.currentTimeMillis();
		
		
		public WorldServer getWorld() {
			return this.world;
		}
		
		public EnumRotate getRotate() {
			return this.rotate;
		}
		
		public Building getBuilding() {
			return this.building;
		}

		public BlockPos getInitPos() {
			return this.initPos;
		}
		
		public BuilderRunnable(World world, Building building, EnumRotate rotate, BlockPos initPos, boolean isStaff) {
			this.world    = (WorldServer) world;
			this.building = building;
			this.rotate   = rotate;
			this.initPos    = initPos;
			this.isStaff  = isStaff;
		}
		
		public void run() {
			this.run(true);
		}
		protected void run(boolean reTop) {

			if (reTop) {
				this.waiter.waitBuilderThread();
			}
			
			try {
				
				logger.info("Create building width matrix : "+building.name+" "+initPos);
				
				initPos= initPos.add(0, building.height, 0);
				if (reTop) {
					initPos = (initPos.getY() < 3) ? new BlockPos(initPos.getX(), 3, initPos.getZ()) : initPos;
				}

				logger.debug ("Building placeBlocksStone : "+building.name+" "+initPos+"...");
				this.placeBlockStone();
				logger.debug ("Building placeBlocksStone : "+building.name+" "+initPos+" OK");
				logger.debug ("Building placeBlocks : "+building.name+" "+initPos+"...");
				this.placeBlocks();
				logger.debug ("Building placeBlocks : "+building.name+" "+initPos+" OK");
				logger.debug ("Building placeAfterBlocks : "+building.name+initPos+"...");
				this.placeAfterBlock();
				logger.debug ("Building placeAfterBlocks : "+building.name+" "+initPos+" OK");
				logger.debug ("Building placeBlockRandom : "+building.name+initPos);
				this.placeBlockRandom();
				logger.debug ("Building placeBlockRandom : "+building.name+" "+initPos+" OK");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			logger.info("End create building width matrix : "+building.name+initPos);

			if (reTop) {
	        	this.waiter.notiyMainThread();
			}
		}
		
		
		private void lock () {
			
			try  {
				long lantency = System.currentTimeMillis() - this.time;
				if (lantency > 200) {
					if (lantency >  500 && this.time != 0) {
						logger.warning("Latency of builder is gretter that 300 milliseconds. lantency = "+lantency);
					}
					
					this.pauseBuilderThread();
					this.time = System.currentTimeMillis();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void pauseMainThread() {
			if (!this.waiter.isBuilderThreadWait) {
				logger.debug ("Dont pause main thread, thread builder not wait");
				return;
			}
			try {
//				synchronized (this.waiter) {
					logger.debug ("Notify builder thread and pause main thread");
					this.waiter.notiyBuilderThread();
					this.waiter.waitMainThread();
//				}
			} catch (Exception e) {
				logger.severe("Error on pauseMainThread", e);
			}
		}
		
		protected void pauseBuilderThread() {
			try {
				logger.debug ("Notify main thread and pause builder thread");
//				synchronized (this.waiter) {
					this.waiter.notiyMainThread();
					this.waiter.waitBuilderThread();
//				}
			} catch (Exception e) {
				logger.severe("Error on pauseBuilderThread", e);
			}
		}
		
		private boolean setBlock (BlockPos pos, IBlockState state) {
			if (pos.getY() < 3) {
				return false;
			}
			try {
				return world.setBlockState(pos, state, 0);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		private void placeBlockStone() {
			// Peut etre inutile
			for (Unity3D unity3D : building.unities) {
				
				// Position réél dans le monde du block
				BlockPos finalPos = initPos.add(unity3D.x(rotate)*rotate.dx, unity3D.y(rotate), unity3D.z(rotate)*rotate.dz);
				
				this.setBlock(finalPos, Blocks.STONE.getDefaultState());
			}
		}
		
		private void placeBlocks() {
			
			ArrayList<Unity3D> placed = new ArrayList<Unity3D>();
			
			Iterator<Unity3D> i = building.unities.iterator();
			while (i.hasNext()) {
				
				this.lock();
				
				Unity3D unity3D = i.next();
				Unity unity = unity3D.unity;
				
				// Position réél dans le monde du block
				BlockPos finalPos = initPos.add(unity3D.x(rotate)*rotate.dx, unity3D.y(rotate), unity3D.z(rotate)*rotate.dz);
				
				boolean isPlaced = false;
				
				world.removeTileEntity(finalPos);
				
				if (unity.after || (unity.state != null && BuildingBlockRegistry.instance().isAfterBlock(unity.state.getBlock()))) {
					this.setBlock (finalPos, Blocks.AIR.getDefaultState());
				} else 
				if (unity.state != null) {
					isPlaced = this.setBlock (finalPos, this.getBlockState(finalPos, unity));
				} else {
					this.setBlock (finalPos, Blocks.AIR.getDefaultState());
				}
				placed.add(unity3D);
				
				if (isPlaced) {
					try {
						this.setContents    (finalPos, unity.contents);
						this.setExtra       (finalPos, unity, building.maxX(rotate), building.maxZ(rotate));
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if (System.currentTimeMillis() - this.timeDisplayProgress > 5000) {
					this.timeDisplayProgress = System.currentTimeMillis();
					logger.message("Building step 1 progress "+building.name+" : " + ( (float)this.placeBlockCount / (float)building.unities.size() * 100.0F  ) + "%");
				}
			}

			for (Unity3D unity3D : placed) {

				this.lock();
				
				Unity unity = unity3D.unity;
				
				// Position réél dans le monde du block
				BlockPos finalPos = initPos.add(unity3D.x(rotate)*rotate.dx, unity3D.y(rotate), unity3D.z(rotate)*rotate.dz);
				
				world.notifyNeighborsOfStateChange(finalPos, unity.state != null ? unity.state.getBlock() : Blocks.AIR, this.isStaff);
				if (this.isStaff) {
					world.markAndNotifyBlock(
						finalPos,
						world.getChunkFromBlockCoords(finalPos),
						world.getBlockState(finalPos),
						world.getBlockState(finalPos),
						3
					);
				}
			}
		}
		

		private void placeAfterBlock() {
			
			ArrayList<Unity3D> placed = new ArrayList<Unity3D>();
			
			for (Unity3D unity3D : building.unities) {

				this.lock();
				
				Unity unity = unity3D.unity;
				
				if (!unity.after && unity.state != null && !BuildingBlockRegistry.instance().isAfterBlock(unity.state.getBlock())) {
					continue;
				}
				
				boolean isPlaced = false;
				
				// Position réél dans le monde du block
				BlockPos finalPos = initPos.add(unity3D.x(rotate)*rotate.dx, unity3D.y(rotate), unity3D.z(rotate)*rotate.dz);
				
				if (unity.state != null) {
					isPlaced = this.setBlock (finalPos, this.getBlockState(finalPos, unity));
				} else {
					this.setBlock (finalPos, Blocks.AIR.getDefaultState());
				}
				placed.add(unity3D);
				
				if (isPlaced) {
					try {
						this.setContents    (finalPos, unity.contents);
						this.setExtra       (finalPos, unity, building.maxX(rotate), building.maxZ(rotate));
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if (System.currentTimeMillis() - this.timeDisplayProgress > 1000) {
					this.timeDisplayProgress = System.currentTimeMillis();
					logger.message("Building step 2 progress "+building.name+" : " + ( (float)this.placeBlockCount / (float)building.unities.size() * 100.0F  ) + "%");
				}
			}
			
			for (Unity3D unity3D : placed) {

				this.lock();
				
				Unity unity = unity3D.unity;
				
				// Position réél dans le monde du block
				BlockPos finalPos = initPos.add(unity3D.x(rotate)*rotate.dx, unity3D.y(rotate), unity3D.z(rotate)*rotate.dz);
				
				world.notifyNeighborsOfStateChange(finalPos, unity.state != null ? unity.state.getBlock() : Blocks.AIR, this.isStaff);
				if (this.isStaff) {
					world.markAndNotifyBlock(
						finalPos,
						world.getChunkFromBlockCoords(finalPos),
						world.getBlockState(finalPos),
						world.getBlockState(finalPos),
						3
					);
				}
			}
		}
		
		private void placeBlockRandom() {
			
			for(GroupSubBuildings group: building.getRandomGroupSubBuildings()) {
				
				ListSubBuildings randomBuilding = group.get(world.rand.nextInt(group.size ()));
				
				for (SubBuilding subBuilding : randomBuilding) {
					
					BuilderRunnable thread = new BuilderRunnable(
						this.world,
						subBuilding.building, 
						this.rotate,
						new BlockPos(
							this.initPos.getX()+subBuilding.x*this.rotate.dx,
							this.initPos.getY()+subBuilding.y,
							this.initPos.getZ()+subBuilding.z*this.rotate.dz
						),
						this.isStaff
					);
					thread.waiter = this.waiter;
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
		private void setContents(BlockPos pos, ArrayList<ArrayList<Content>> contents) {
			
			IBlockState state  = world.getBlockState(pos);
			
			if (state != null && state.getBlock() instanceof BlockContainer) {
				
				TileEntity te  = world.getTileEntity (pos);
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
							if (content.item instanceof ItemPotion && content.type != null) {
								PotionType potiontype = RegisteredObjects.instance().getPotionType(content.type);
								if (potiontype != null) {
									itemStack = PotionUtils.addPotionToItemStack(itemStack, potiontype);
								}
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
		private void setExtra(BlockPos pos, Unity unity, int maxX, int maxZ) {
			for (BuildingBlockHandler handler : BuildingBlockRegistry.instance().getHandlers()) {
				handler.setExtra(this.world, pos, unity, this.initPos, this.rotate, maxX, maxZ);
			}
		}
		
		private IBlockState getBlockState(BlockPos pos, Unity unity) {
			IBlockState state = unity.state;
			for (BuildingBlockHandler handler : BuildingBlockRegistry.instance().getHandlers()) {
				state = handler.getBlockState(this.world, pos, state, unity, this.rotate);
			}
			return state;
		}
	}
	
}
