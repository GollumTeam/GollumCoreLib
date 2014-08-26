package mods.gollum.core.client.gui;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.container.GCLContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GCLGuiContainer extends GuiContainer {
	
	public static int SIZE_PLAYER_INVENTORY = 101;
	public static int SIZE_ITEM = 18;
	public static int SIZE_BORDER_SIDE = 7;
	public static int SIZE_BORDER_TOP = 15;
	public static int SIZE_BORDER_BOTTOM = 7;
	
	protected static final ResourceLocation texture = new ResourceLocation(ModGollumCoreLib.MODID.toLowerCase()+":gui/generic_inventory.png");
	
	protected IInventory inventoryPlayer;
	protected IInventory inventoryBlock;
	
	protected int numRows = 0;
	protected int numColumns;

	public GCLGuiContainer(IInventory inventoryPlayer, IInventory inventoryBlock, int numColumns) {
		super(new GCLContainer (inventoryPlayer, inventoryBlock, numColumns));
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
		
		this.ySize = 114 + this.numRows * SIZE_ITEM;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {

		int base = SIZE_BORDER_TOP + this.numRows*SIZE_ITEM;
		int top = base-this.numRows*SIZE_ITEM;
		
		int widthTop = SIZE_BORDER_SIDE+SIZE_ITEM*this.numColumns+1;
		int xTop = (this.width - widthTop) / 2;
		
		this.fontRenderer.drawString(StatCollector.translateToLocal(this.inventoryBlock.getInvName()) , widthTop, top-13                      , 0x404040);
		this.fontRenderer.drawString(StatCollector.translateToLocal(this.inventoryPlayer.getInvName()), 8      , top+this.numRows*SIZE_ITEM+6, 0x404040);
		
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the
	 * items)
	 */
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		
		int base = SIZE_BORDER_TOP + this.numRows*SIZE_ITEM;
		int top = base-SIZE_BORDER_TOP-this.numRows*SIZE_ITEM-3;
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		
		int widthTop = SIZE_BORDER_SIDE*2+SIZE_ITEM*this.numColumns;
		int xTop = (this.width - widthTop) / 2;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		// player inventory
		this.mc.renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(
			x, y+base, 
			0, 0, 
			this.xSize, SIZE_PLAYER_INVENTORY
		);
		
		// top left
		this.drawTexturedModalRect(
			xTop, y+top, // Position in screen
			0, SIZE_PLAYER_INVENTORY, // Position in image
			SIZE_BORDER_SIDE, SIZE_BORDER_TOP // Size in screen
		);
		
		// top right
		this.drawTexturedModalRect(
			xTop + SIZE_BORDER_SIDE + SIZE_ITEM*this.numColumns, y + top, // Position in screen
			SIZE_BORDER_SIDE+SIZE_ITEM, SIZE_PLAYER_INVENTORY, // Position in image
			SIZE_BORDER_SIDE, SIZE_BORDER_TOP // Size in screen
		);
		
		
		for (int i = 0; i < this.numColumns; i++) {
			// top
			this.drawTexturedModalRect(
				xTop + SIZE_BORDER_SIDE + SIZE_ITEM*i, y+top, // Position in screen
				SIZE_BORDER_SIDE, SIZE_PLAYER_INVENTORY, // Position in image
				SIZE_ITEM, SIZE_BORDER_TOP // Size in screen
			);
			
			// bottom
			this.drawTexturedModalRect(
				xTop + SIZE_BORDER_SIDE + SIZE_ITEM*i, y+top + SIZE_BORDER_TOP+SIZE_ITEM*this.numRows, // Position in screen
				SIZE_BORDER_SIDE, SIZE_PLAYER_INVENTORY+SIZE_BORDER_TOP+SIZE_ITEM, // Position in image
				SIZE_ITEM, SIZE_BORDER_BOTTOM // Size in screen
			);
			
			int posX = xTop + SIZE_BORDER_SIDE + SIZE_ITEM*i;
			
			if (posX + SIZE_ITEM >= x && posX <= x + this.xSize) {
				// jointure
				this.drawTexturedModalRect(
					Math.max(x, posX), y+top + SIZE_BORDER_TOP+SIZE_ITEM*this.numRows+4, // Position in screen
					SIZE_BORDER_SIDE, SIZE_PLAYER_INVENTORY+SIZE_BORDER_TOP, // Position in image
					SIZE_ITEM+Math.min((x + this.xSize)-(posX + SIZE_ITEM), 0), SIZE_BORDER_BOTTOM-4 // Size in screen
				);
			}
		}
		
		int slot = 0;
		for (int i = 0; i < this.numRows; i++) {
			// left middle
			this.drawTexturedModalRect(
				xTop, y+top+SIZE_BORDER_TOP + SIZE_ITEM*i, 
				0, SIZE_PLAYER_INVENTORY+SIZE_BORDER_TOP, 
				SIZE_BORDER_SIDE, SIZE_ITEM
			);
			
			
			for (int j = 0; j < this.numColumns; j++) {

				if (slot < inventoryBlock.getSizeInventory()) {
					// center
					this.drawTexturedModalRect(
						xTop+ SIZE_BORDER_SIDE + SIZE_ITEM*j, y+top+SIZE_BORDER_TOP + SIZE_ITEM * i, // Position in screen
						SIZE_BORDER_SIDE*2+SIZE_ITEM, SIZE_PLAYER_INVENTORY, // Position in image
						SIZE_ITEM, SIZE_ITEM // Size in screen
					);
				} else {
					// center
					this.drawTexturedModalRect(
						xTop + SIZE_BORDER_SIDE + SIZE_ITEM*j, y+top+SIZE_BORDER_TOP + SIZE_ITEM * i, // Position in screen
						SIZE_BORDER_SIDE, SIZE_PLAYER_INVENTORY+SIZE_BORDER_TOP, // Position in image
						SIZE_ITEM, SIZE_ITEM // Size in screen
					);
				}

				slot++;
			}
			
			// right middle
			this.drawTexturedModalRect(
				xTop+SIZE_BORDER_SIDE+SIZE_ITEM*this.numColumns, y+top+SIZE_BORDER_TOP + SIZE_ITEM*i, 
				SIZE_ITEM+SIZE_BORDER_SIDE, SIZE_PLAYER_INVENTORY+SIZE_BORDER_TOP, 
				SIZE_BORDER_SIDE, SIZE_ITEM
			);
		}

		// bottom left
		this.drawTexturedModalRect(
			xTop, y+top + SIZE_BORDER_TOP+SIZE_ITEM*this.numRows, // Position in screen
			0, SIZE_PLAYER_INVENTORY+SIZE_BORDER_TOP+SIZE_ITEM, // Position in image
			SIZE_BORDER_SIDE, SIZE_BORDER_BOTTOM // Size in screen
		);
		
		// bottom right
		this.drawTexturedModalRect(
			xTop+SIZE_BORDER_SIDE+SIZE_ITEM*this.numColumns, y+top + SIZE_BORDER_TOP+SIZE_ITEM*this.numRows, // Position in screen
			SIZE_BORDER_SIDE+SIZE_ITEM, SIZE_PLAYER_INVENTORY+SIZE_BORDER_TOP+SIZE_ITEM, // Position in image
			SIZE_BORDER_SIDE, SIZE_BORDER_BOTTOM // Size in screen
		);
		
		// bottom left,
		this.drawTexturedModalRect(
			Math.max(x, xTop), y+top + SIZE_BORDER_TOP+SIZE_ITEM*this.numRows+4, // Position in screen
			0, SIZE_PLAYER_INVENTORY+SIZE_BORDER_TOP, // Position in image
			SIZE_BORDER_SIDE, SIZE_BORDER_BOTTOM-4 // Size in screen
		);
		
		// bottom right
		this.drawTexturedModalRect(
			Math.min(x+this.xSize-SIZE_BORDER_SIDE, xTop+SIZE_BORDER_SIDE+SIZE_ITEM*this.numColumns), y+top + SIZE_BORDER_TOP+SIZE_ITEM*this.numRows+3, // Position in screen
			SIZE_BORDER_SIDE+SIZE_ITEM, SIZE_PLAYER_INVENTORY+SIZE_BORDER_TOP, // Position in image
			SIZE_BORDER_SIDE, SIZE_BORDER_BOTTOM-3 // Size in screen
		);
	}

}