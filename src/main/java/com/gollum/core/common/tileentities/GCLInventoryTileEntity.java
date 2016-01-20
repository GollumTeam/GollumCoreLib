package com.gollum.core.common.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;

public abstract class GCLInventoryTileEntity extends TileEntityLockable implements ITickable, IInventory {

	protected ItemStack[] inventory;
	protected int maxSize;
	
	protected boolean playSoundChest = true;
	protected float volumeSoundOpenClosedInventory = 0.5F;
	protected String soundOpenInventory   = "random.chestopen";
	protected String soundClosedInventory = "random.chestclosed";
	
	// Ouverture des porte	
	private float doorOpenProgress     = 0.0F;
	private float prevDoorOpenProgress = 0.0F;
	private float doorSpeed            = 0.1F;

	/**
	 * Nombre de player utilisant le coffre
	 */
	protected int numUsingPlayers;
	
	public GCLInventoryTileEntity(int maxSize) {
		super();
		this.maxSize   = maxSize;
		this.inventory = new ItemStack[maxSize];
	}
	
	///////////////
	// Inventory //
	///////////////
	
	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}
	
	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int slot) {
		
		if (slot >= this.getSizeInventory ()) return null;
		
		return  this.inventory[slot];
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be
	 * 64, possibly will be extended. *Isn't this more of a set than a get?*
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int slot, int number) {
		
		if (slot >= this.getSizeInventory ()) return null;
		
		ItemStack itemStack = null;
		
		if (this.inventory[slot] != null) {
			number = Math.min (number, this.inventory[slot].stackSize);
			itemStack = this.inventory[slot].splitStack(number);
			
			if (this.inventory[slot].stackSize == 0) {
				this.inventory[slot] = null;
			}
		}
		
		return itemStack;
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {

		if (slot >= this.getSizeInventory ()) return null;
		
		ItemStack itemstack = null;

		if (this.inventory[slot] != null) {
			itemstack = this.inventory[slot];
			this.inventory[slot] = null;
			
		}
		return itemstack;
	}
	
	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int slot, ItemStack itemStack) {

		if (slot >= this.getSizeInventory ());
		
		this.inventory[slot] = itemStack;
		
		if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
			itemStack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			if (this.numUsingPlayers < 0) {
				this.numUsingPlayers = 0;
			}
			
			++this.numUsingPlayers;
			this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numUsingPlayers);
			this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
			this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
		}
	}
	
	@Override
	public void closeInventory (EntityPlayer player) {
		if (!player.isSpectator() &&  this.getBlockType() == this.worldObj.getBlockState(pos).getBlock()) {
			--this.numUsingPlayers;
			this.worldObj.addBlockEvent(this.pos, this.getBlockType(), 1, this.numUsingPlayers);
			this.worldObj.notifyNeighborsOfStateChange(this.pos, this.getBlockType());
			this.worldObj.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType());
		}
	}
	
	/**
	 * Called when a client event is received with the event number and
	 * argument, see World.sendClientEvent
	 */
	public boolean receiveClientEvent(int idEvent, int value) {
		if (idEvent == 1) {
			this.numUsingPlayers = value;
			return true;
		} else {
			return super.receiveClientEvent(idEvent, value);
		}
	}
	
	/**
	 * If this returns false, the inventory name will be used as an unlocalized
	 * name, and translated into the player's language. Otherwise it will be
	 * used directly.
	 */
	@Override
	public boolean hasCustomName() {
		return true;
	}
	
	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
		return true;
	}
	
	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		
		boolean rtn = false;
		if (this.worldObj.getTileEntity(this.pos) == this) {
			rtn = par1EntityPlayer.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
		}
		return rtn;
	}

	/**
	 * invalidates a tile entity
	 */
	@Override
	public void invalidate() {
		this.updateContainingBlockInfo();
		super.invalidate();
	}

	protected void playSoundClosedInventory() {
		double x = (double) this.pos.getX() + 0.5D;
		double y = (double) this.pos.getY() + 0.5D;
		double z = (double) this.pos.getZ() + 0.5D;
		this.worldObj.playSoundEffect(x, y, z, this.soundClosedInventory, this.volumeSoundOpenClosedInventory, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
	}

	protected void playSoundOpenInventory() {
		double x = (double) this.pos.getX() + 0.5D;
		double y = (double) this.pos.getY() + 0.5D;
		double z = (double) this.pos.getZ() + 0.5D;
		this.worldObj.playSoundEffect(x, y, z, this.soundOpenInventory, this.volumeSoundOpenClosedInventory, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
	}
	
	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < inventory.length; ++i) {
			inventory[i] = null;
		}
	}
	
	@Override
	public String getGuiID() {
		return null;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return null;
	}
	
	////////////
	// Update //
	////////////
	
	/**
	 * Allows the entity to update its state. Overridden in most subclasses,
	 * e.g. the mob spawner uses this to count ticks and creates a new spawn
	 * inside its implementation.
	 */
	public void update() {
		
		this.prevDoorOpenProgress = this.doorOpenProgress;
		
		// Son d'ouverture du coffre
		if (this.numUsingPlayers > 0 && this.doorOpenProgress == 0.0F && playSoundChest) {
			playSoundOpenInventory();
		}

		if (this.numUsingPlayers == 0 && this.doorOpenProgress > 0.0F || this.numUsingPlayers > 0 && this.doorOpenProgress < 1.0F) {
			
			// Dépalce la porte
			if (this.numUsingPlayers > 0) {
				this.doorOpenProgress += this.doorSpeed;
			} else {
				this.doorOpenProgress -= this.doorSpeed;
			}
			this.doorOpenProgress = (this.doorOpenProgress > 1.0F) ? 1.0F : this.doorOpenProgress;
			this.doorOpenProgress = (this.doorOpenProgress < 0.0F) ? 0.0F : this.doorOpenProgress;

			// Son de fermeture du coffre
			if (this.doorOpenProgress < 0.7F && this.prevDoorOpenProgress >= 0.7F && playSoundChest) {
				playSoundClosedInventory();
			}
		}
		
	}
	
	
	////////////////
	// Save datas //
	////////////////

	protected void readItems (NBTTagCompound nbtTagCompound, String tagName) {
		this.readItems(nbtTagCompound, tagName, false);
	}
	
	protected void readItems (NBTTagCompound nbtTagCompound, String tagName, boolean merge) {
		
		if (!nbtTagCompound.hasKey (tagName)) {
			return;
		}
		
		NBTTagList nbttaglist = nbtTagCompound.getTagList(tagName, Constants.NBT.TAG_COMPOUND);
		
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			try {
				NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
				int j = nbttagcompound.getByte("Slot");
				
				if (j >= 0 && j < this.inventory.length) {
					
					ItemStack itemStack = ItemStack.loadItemStackFromNBT(nbttagcompound);
					
					if (!merge || itemStack != null) {
						this.inventory[j] = itemStack;
					}
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		
		this.inventory = new ItemStack[this.maxSize];
		this.readItems(nbtTagCompound, "Items");
		
	}
	
	
	protected void writeItems (NBTTagCompound nbtTagCompound, String tagName, ItemStack[] inventory) {
		
		NBTTagList nbttaglist = new NBTTagList();
		
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			if (this.inventory[i] != null) {
				NBTTagCompound subNBTTagCompound = new NBTTagCompound();
				subNBTTagCompound.setByte("Slot", (byte) i);
				this.inventory[i].writeToNBT(subNBTTagCompound);
				nbttaglist.appendTag(subNBTTagCompound);
			}
		}

		nbtTagCompound.setTag(tagName, nbttaglist);
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		
		this.writeItems(nbtTagCompound, "Items", inventory);
		
	}
	
	////////////
	// Others //
	////////////
	
	public GCLInventoryTileEntity setDoorSpeed (float doorSpeed) {
		this.doorSpeed = doorSpeed;
		return this;
	}
	
	public float getDoorOpenProgress () {
		return this.doorOpenProgress;
	}
	
	public float getPreviousDoorOpenProgress () {
		return this.prevDoorOpenProgress;
	}

}