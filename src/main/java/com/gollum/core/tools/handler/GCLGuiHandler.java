package com.gollum.core.tools.handler;

import java.lang.reflect.Constructor;
import java.util.Hashtable;

import com.gollum.core.tools.registry.InventoryRegistry.GuiContainerInventoryClass;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
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
		
		boolean bE   = world.blockExists(x, y, z); // bE = Block Exist
		boolean tENN = te != null;                 // tENN = tileEntity not null
		
		try {
			
			if (guiInventoryList.containsKey(id)) {
				
				GuiContainerInventoryClass guiContainerInventoryClass = this.guiInventoryList.get(id);
				
				//InventoryPlayer
				
				for (Constructor constructor : guiContainerInventoryClass.classContainer.getConstructors()) {
					for (int i = 0 ; i < 10; i++) {

						try {
							switch (i) {
								case 0: container = (Container) constructor.newInstance(player, world, x, y, z, guiContainerInventoryClass.parameter);              break;
								case 1: container = (Container) constructor.newInstance(player, world, x, y, z);                                                    break;
								case 2: container = (Container) constructor.newInstance(player.inventory, world, x, y, z, guiContainerInventoryClass.parameter);    break;
								case 3: container = (Container) constructor.newInstance(player.inventory, world, x, y, z);                                          break;
								case 4: if (bE && tENN) container = (Container) constructor.newInstance(player.inventory, te, guiContainerInventoryClass.parameter);break;
								case 5: if (bE && tENN) container = (Container) constructor.newInstance(player.inventory, te);                                      break;
								default: break;
							}
						} catch (Exception e) {
						}
						
						if (container != null) {
							break;
						}
					}
					
					if (container != null) {
						break;
					}
				}
				if (container == null) {
					throw new Exception("No valid constructeur with your Container for "+guiContainerInventoryClass.classContainer.getName());
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
		
		boolean bE   = world.blockExists(x, y, z); // bE = Block Exist
		boolean tENN = te != null;                 // tENN = tileEntity not null
		
		try {
			if (guiInventoryList.containsKey(id)) {
				
				GuiContainerInventoryClass guiContainerInventoryClass = this.guiInventoryList.get(id);
				
				//InventoryPlayer
				
				for (Constructor constructor : guiContainerInventoryClass.classGuiContainer.getConstructors()) {
					for (int i = 0 ; i < 10; i++) {

						try {
							switch (i) {
								case 0: gui = (GuiContainer) constructor.newInstance(player, world, x, y, z, guiContainerInventoryClass.parameter);              break;
								case 1: gui = (GuiContainer) constructor.newInstance(player, world, x, y, z);                                                    break;
								case 2: gui = (GuiContainer) constructor.newInstance(player.inventory, world, x, y, z, guiContainerInventoryClass.parameter);    break;
								case 3: gui = (GuiContainer) constructor.newInstance(player.inventory, world, x, y, z);                                          break;
								case 4: if (bE && tENN) gui = (GuiContainer) constructor.newInstance(player.inventory, te, guiContainerInventoryClass.parameter);break;
								case 5: if (bE && tENN) gui = (GuiContainer) constructor.newInstance(player.inventory, te);                                      break;
								default: break;
							}
						} catch (Exception e) {
						}
						
						if (gui != null) {
							break;
						}
					}
					
					if (gui != null) {
						break;
					}
				}
				if (gui == null) {
					throw new Exception("No valid constructeur with your GuiContainer for "+guiContainerInventoryClass.classGuiContainer.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return gui;
	}
	
}
