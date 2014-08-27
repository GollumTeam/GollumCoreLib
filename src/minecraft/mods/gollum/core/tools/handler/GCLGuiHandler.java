package mods.gollum.core.tools.handler;

import java.util.Hashtable;

import mods.gollum.core.ModGollumCoreLib;
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
				
				int i = 0;
				while (container == null) {
					
					switch (i) {
						case 0:
							try {
								container = guiContainerInventoryClass.classContainer.getConstructor(EntityPlayer.class, World.class, int.class, int.class, int.class).newInstance(player, world, x, y, z);
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								container = guiContainerInventoryClass.classContainer.getConstructor(IInventory.class, World.class, int.class, int.class, int.class).newInstance(player.inventory, world, x, y, z);
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								container = guiContainerInventoryClass.classContainer.getConstructor(IInventory.class, IInventory.class).newInstance(player.inventory, (IInventory)te);
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								container = guiContainerInventoryClass.classContainer.getConstructor(IInventory.class, IInventory.class, int.class).newInstance(player.inventory, (IInventory)te, guiContainerInventoryClass.numColumns);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
							
						default:
							throw new Exception("No valid constructeur with your Container for "+guiContainerInventoryClass.classContainer.getName());
					}
					i++;
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
				
				int i = 0;
				while (gui == null) {
					switch (i) {
						case 0:
							try {
								gui = (GuiContainer) guiContainerInventoryClass.classGuiContainer.getConstructor(EntityPlayer.class, World.class, int.class, int.class, int.class).newInstance(player, world, x, y, z);
							} catch (Exception e) {
							}
							break;
						case 1:
							try {
								gui = (GuiContainer) guiContainerInventoryClass.classGuiContainer.getConstructor(IInventory.class, World.class, int.class, int.class, int.class).newInstance(player.inventory, world, x, y, z);
							} catch (Exception e) {
							}
							break;
						case 2:
							try {
								gui = (GuiContainer) guiContainerInventoryClass.classGuiContainer.getConstructor(IInventory.class, IInventory.class).newInstance(player.inventory, (IInventory)te);
							} catch (Exception e) {
							}
							break;
						case 3:
							try {
								gui = (GuiContainer) guiContainerInventoryClass.classGuiContainer.getConstructor(IInventory.class, IInventory.class, int.class).newInstance(player.inventory, (IInventory)te, guiContainerInventoryClass.numColumns);
							} catch (Exception e) {
							}
							break;
							
						default:
							throw new Exception("No valid constructeur with your GuiContainer for "+guiContainerInventoryClass.classGuiContainer.getName());
					}
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return gui;
	}
	
}
