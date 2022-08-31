package com.gollum.core.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GCLContainer extends Container {
	
	public static final int SIZE_BORDER_BOTTOM = 7;
	public static final int SIZE_BORDER_TOP = 15;
	public static final int SIZE_BORDER_SIDE = 7;
	public static final int SIZE_ITEM = 18;
	public static final int SIZE_PLAYER_INVENTORY = 101;
	
	protected IInventory inventoryBlock;
	protected int numRows;
	protected int numColumns;
	
	public GCLContainer(IInventory inventoryPlayer, IInventory inventoryBlock, EntityPlayer player, int numColumns) {
		
		this.inventoryBlock = inventoryBlock;
		this.numColumns     = numColumns;
		this.numRows        = (int)Math.ceil ((double)inventoryBlock.getSizeInventory() / (double)this.numColumns);
		
		inventoryBlock.openInventory(player);
		
		int height = (this.numRows - 4) * SIZE_ITEM;
		int distance = 106;
		int i;
		int j;
		
		int widthTop = GCLContainer.SIZE_BORDER_SIDE*2+GCLContainer.SIZE_ITEM*this.numColumns;
		int xTop = (177 - widthTop) / 2;
		
		xTop = (xTop < 0) ? xTop-1 : xTop; // FIXE de position
		
		int slot = 0;
		for (i = 0; i < this.numRows; ++i) {
			for (j = 0; j < this.numColumns; ++j) {
				if (slot < inventoryBlock.getSizeInventory()) {
					this.addSlotToContainer(new Slot(
						inventoryBlock, slot, 
						GCLContainer.SIZE_BORDER_SIDE + j * SIZE_ITEM + 1 + xTop, 
						GCLContainer.SIZE_BORDER_TOP + i * SIZE_ITEM - 2
					));
				}
				slot++;
			}
		}
		
		for (i = 0; i < 3; ++i) {
			for (j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * SIZE_ITEM, distance + i * SIZE_ITEM + height));
			}
		}
		
		for (i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 58+ distance + height));
		}
	}
	
	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
		
		ItemStack itemStack = null;
		Slot slot = (Slot) this.inventorySlots.get(slotId);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemStack = itemstack1.copy();

			if (slotId < this.numRows * this.numColumns) {
				if (!this.mergeItemStack(itemstack1, this.numRows * this.numColumns, this.inventorySlots.size(), true)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, this.numRows * this.numColumns, false)) {
				return null;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}
		}
		
		return itemStack;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.inventoryBlock.isUsableByPlayer(player);
	}
	
	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer player) {
		
		super.onContainerClosed(player);
		this.inventoryBlock.closeInventory(player);
		
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
}
