package mods.gollum.core.tools.handler;

import java.util.Hashtable;

import mods.gollum.core.tools.registry.InventoryRegistry.GuiContainerInventoryClass;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GCLGuiHandler implements IGuiHandler {
	
	private Hashtable<Integer, GuiContainerInventoryClass> guiInventoryList;

	public GCLGuiHandler(Hashtable<Integer, GuiContainerInventoryClass> guiInventoryList) {
		this.guiInventoryList = guiInventoryList;
	}

	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		
		TileEntity te = world.getBlockTileEntity(x, y, z);
		Container container = null;

		try {
			if (world.blockExists(x, y, z) && te != null && guiInventoryList.containsKey(id)) {
				
				GuiContainerInventoryClass guiContainerInventoryClass = this.guiInventoryList.get(id);
				
				try {
					container = guiContainerInventoryClass.classContainer.getConstructor(IInventory.class, IInventory.class).newInstance(player.inventory, (IInventory)te);
				} catch (Exception e) {
					container = guiContainerInventoryClass.classContainer.getConstructor(IInventory.class, IInventory.class, int.class).newInstance(player.inventory, (IInventory)te, guiContainerInventoryClass.numColumns);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return container;
	}

	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity te = world.getBlockTileEntity(x, y, z);
		GuiContainer gui = null;

		try {
			if (world.blockExists(x, y, z) && te != null && guiInventoryList.containsKey(id)) {
				
				GuiContainerInventoryClass guiContainerInventoryClass = this.guiInventoryList.get(id);
				
				try {
					gui = guiContainerInventoryClass.classGuiContainer.getConstructor(IInventory.class, IInventory.class, Class.class).newInstance(
						player.inventory, (IInventory)te, guiContainerInventoryClass.classContainer
					);
				} catch (Exception e) {
					gui = guiContainerInventoryClass.classGuiContainer.getConstructor(IInventory.class, IInventory.class, Class.class, int.class).newInstance(
						player.inventory, (IInventory)te, guiContainerInventoryClass.classContainer, guiContainerInventoryClass.numColumns
					);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return gui;
	}
	
}
