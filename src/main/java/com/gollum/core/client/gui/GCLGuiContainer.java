package com.gollum.core.client.gui;

import org.lwjgl.opengl.GL11;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.container.GCLContainer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GCLGuiContainer extends GuiContainer {
	
	protected static final ResourceLocation texture = new ResourceLocation(ModGollumCoreLib.MODID.toLowerCase()+":gui/generic_inventory.png");
	
	protected IInventory inventoryPlayer;
	protected IInventory inventoryBlock;
	
	protected int numRows = 0;
	protected int numColumns;

	public GCLGuiContainer(IInventory inventoryPlayer, IInventory inventoryBlock, EntityPlayer player, int numColumns) {
		super(new GCLContainer (inventoryPlayer, inventoryBlock, player, numColumns));
		init(inventoryPlayer, inventoryBlock);
	}
	
	public GCLGuiContainer(IInventory inventoryPlayer, IInventory inventoryBlock, Class<? extends GCLContainer> containerClass) throws Exception {
		super(containerClass.getConstructor(IInventory.class, IInventory.class).newInstance(inventoryPlayer, inventoryBlock));
		init(inventoryPlayer, inventoryBlock);
	}
	
	public GCLGuiContainer(IInventory inventoryPlayer, IInventory inventoryBlock, Class<? extends GCLContainer> containerClass, int numColumns) throws Exception {
		super(containerClass.getConstructor(IInventory.class, IInventory.class, int.class).newInstance(inventoryPlayer, inventoryBlock, numColumns));
		init(inventoryPlayer, inventoryBlock);
	}

	private void init(IInventory inventoryPlayer, IInventory inventoryBlock) {
		this.inventoryPlayer = inventoryPlayer;
		this.inventoryBlock = inventoryBlock;
		
		this.allowUserInput = false;
		
		this.numColumns = ((GCLContainer)this.inventorySlots).getNumColumns ();
		this.numRows = (int)Math.ceil ((double)inventoryBlock.getSizeInventory() / (double)this.numColumns);
		
		this.ySize = 114 + this.numRows * GCLContainer.SIZE_ITEM;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {

		int base = GCLContainer.SIZE_BORDER_TOP + this.numRows*GCLContainer.SIZE_ITEM;
		int top = base-this.numRows*GCLContainer.SIZE_ITEM;
		
		int widthTop = GCLContainer.SIZE_BORDER_SIDE+GCLContainer.SIZE_ITEM*this.numColumns;
		int xTop = (this.width - widthTop) / 2;
		
		int posX = (GCLContainer.SIZE_ITEM * (9 - this.numColumns)) / 2;
		
		this.fontRendererObj.drawString(StatCollector.translateToLocal(this.inventoryBlock.getDisplayName().getUnformattedText()) , 8 + posX , top-13                                   , 0x404040);
		this.fontRendererObj.drawString(StatCollector.translateToLocal(this.inventoryPlayer.getDisplayName().getUnformattedText()), 8        , top+this.numRows*GCLContainer.SIZE_ITEM+6, 0x404040);
		
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		
		int base = GCLContainer.SIZE_BORDER_TOP + this.numRows*GCLContainer.SIZE_ITEM;
		int top = base-GCLContainer.SIZE_BORDER_TOP-this.numRows*GCLContainer.SIZE_ITEM-3;
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		
		int widthTop = GCLContainer.SIZE_BORDER_SIDE*2+GCLContainer.SIZE_ITEM*this.numColumns;
		int xTop = (this.width - widthTop) / 2;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		// player inventory
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(
			x, y+base, 
			0, 0, 
			this.xSize, GCLContainer.SIZE_PLAYER_INVENTORY
		);
		
		// top left
		this.drawTexturedModalRect(
			xTop, y+top, // Position in screen
			0, GCLContainer.SIZE_PLAYER_INVENTORY, // Position in image
			GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_BORDER_TOP
		);
		
		// top right
		this.drawTexturedModalRect(
			xTop + GCLContainer.SIZE_BORDER_SIDE + GCLContainer.SIZE_ITEM*this.numColumns, y + top, // Position in screen
			GCLContainer.SIZE_BORDER_SIDE+GCLContainer.SIZE_ITEM, GCLContainer.SIZE_PLAYER_INVENTORY, // Position in image
			GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_BORDER_TOP
		);
		
		
		for (int i = 0; i < this.numColumns; i++) {
			// top
			this.drawTexturedModalRect(
				xTop + GCLContainer.SIZE_BORDER_SIDE + GCLContainer.SIZE_ITEM*i, y+top, // Position in screen
				GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_PLAYER_INVENTORY, // Position in image
				GCLContainer.SIZE_ITEM, GCLContainer.SIZE_BORDER_TOP
			);
			
			// bottom
			this.drawTexturedModalRect(
				xTop + GCLContainer.SIZE_BORDER_SIDE + GCLContainer.SIZE_ITEM*i, y+top + GCLContainer.SIZE_BORDER_TOP+GCLContainer.SIZE_ITEM*this.numRows, // Position in screen
				GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_PLAYER_INVENTORY+GCLContainer.SIZE_BORDER_TOP+GCLContainer.SIZE_ITEM, // Position in image
				GCLContainer.SIZE_ITEM, GCLContainer.SIZE_BORDER_BOTTOM
			);
			
			int posX = xTop + GCLContainer.SIZE_BORDER_SIDE + GCLContainer.SIZE_ITEM*i;
			
			if (posX + GCLContainer.SIZE_ITEM >= x && posX <= x + this.xSize) {
				// jointure
				this.drawTexturedModalRect(
					Math.max(x, posX), y+top + GCLContainer.SIZE_BORDER_TOP+GCLContainer.SIZE_ITEM*this.numRows+4, // Position in screen
					GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_PLAYER_INVENTORY+GCLContainer.SIZE_BORDER_TOP, // Position in image
					GCLContainer.SIZE_ITEM+Math.min((x + this.xSize)-(posX + GCLContainer.SIZE_ITEM), 0), GCLContainer.SIZE_BORDER_BOTTOM-4 // Size in screen
				);
			}
		}
		
		int slot = 0;
		for (int i = 0; i < this.numRows; i++) {
			// left middle
			this.drawTexturedModalRect(
				xTop, y+top+GCLContainer.SIZE_BORDER_TOP + GCLContainer.SIZE_ITEM*i, 
				0, GCLContainer.SIZE_PLAYER_INVENTORY+GCLContainer.SIZE_BORDER_TOP, 
				GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_ITEM
			);
			
			
			for (int j = 0; j < this.numColumns; j++) {

				if (slot < inventoryBlock.getSizeInventory()) {
					// center
					this.drawTexturedModalRect(
						xTop+ GCLContainer.SIZE_BORDER_SIDE + GCLContainer.SIZE_ITEM*j, y+top+GCLContainer.SIZE_BORDER_TOP + GCLContainer.SIZE_ITEM * i, // Position in screen
						GCLContainer.SIZE_BORDER_SIDE*2+GCLContainer.SIZE_ITEM, GCLContainer.SIZE_PLAYER_INVENTORY, // Position in image
						GCLContainer.SIZE_ITEM, GCLContainer.SIZE_ITEM
					);
				} else {
					// center
					this.drawTexturedModalRect(
						xTop + GCLContainer.SIZE_BORDER_SIDE + GCLContainer.SIZE_ITEM*j, y+top+GCLContainer.SIZE_BORDER_TOP + GCLContainer.SIZE_ITEM * i, // Position in screen
						GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_PLAYER_INVENTORY+GCLContainer.SIZE_BORDER_TOP, // Position in image
						GCLContainer.SIZE_ITEM, GCLContainer.SIZE_ITEM
					);
				}

				slot++;
			}
			
			// right middle
			this.drawTexturedModalRect(
				xTop+GCLContainer.SIZE_BORDER_SIDE+GCLContainer.SIZE_ITEM*this.numColumns, y+top+GCLContainer.SIZE_BORDER_TOP + GCLContainer.SIZE_ITEM*i, 
				GCLContainer.SIZE_ITEM+GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_PLAYER_INVENTORY+GCLContainer.SIZE_BORDER_TOP, 
				GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_ITEM
			);
		}

		// bottom left
		this.drawTexturedModalRect(
			xTop, y+top + GCLContainer.SIZE_BORDER_TOP+GCLContainer.SIZE_ITEM*this.numRows, // Position in screen
			0, GCLContainer.SIZE_PLAYER_INVENTORY+GCLContainer.SIZE_BORDER_TOP+GCLContainer.SIZE_ITEM, // Position in image
			GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_BORDER_BOTTOM
		);
		
		// bottom right
		this.drawTexturedModalRect(
			xTop+GCLContainer.SIZE_BORDER_SIDE+GCLContainer.SIZE_ITEM*this.numColumns, y+top + GCLContainer.SIZE_BORDER_TOP+GCLContainer.SIZE_ITEM*this.numRows, // Position in screen
			GCLContainer.SIZE_BORDER_SIDE+GCLContainer.SIZE_ITEM, GCLContainer.SIZE_PLAYER_INVENTORY+GCLContainer.SIZE_BORDER_TOP+GCLContainer.SIZE_ITEM, // Position in image
			GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_BORDER_BOTTOM
		);
		
		// bottom left,
		this.drawTexturedModalRect(
			Math.max(x, xTop), y+top + GCLContainer.SIZE_BORDER_TOP+GCLContainer.SIZE_ITEM*this.numRows+4, // Position in screen
			0, GCLContainer.SIZE_PLAYER_INVENTORY+GCLContainer.SIZE_BORDER_TOP, // Position in image
			GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_BORDER_BOTTOM-4 // Size in screen
		);
		
		// bottom right
		this.drawTexturedModalRect(
			Math.min(x+this.xSize-GCLContainer.SIZE_BORDER_SIDE, xTop+GCLContainer.SIZE_BORDER_SIDE+GCLContainer.SIZE_ITEM*this.numColumns), y+top + GCLContainer.SIZE_BORDER_TOP+GCLContainer.SIZE_ITEM*this.numRows+3, // Position in screen
			GCLContainer.SIZE_BORDER_SIDE+GCLContainer.SIZE_ITEM, GCLContainer.SIZE_PLAYER_INVENTORY+GCLContainer.SIZE_BORDER_TOP, // Position in image
			GCLContainer.SIZE_BORDER_SIDE, GCLContainer.SIZE_BORDER_BOTTOM-3 // Size in screen
		);
	}

}