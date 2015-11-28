package com.gollum.core.tools.handler;

import java.util.ArrayList;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GCLArrayGuiHandler implements IGuiHandler {
	
	private ArrayList<IGuiHandler> guiHandlers;

	public GCLArrayGuiHandler(ArrayList<IGuiHandler> guiHandlers) {
		this.guiHandlers = guiHandlers;
	}

	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		
		Object object = null;
		
		for (IGuiHandler guiHandler : this.guiHandlers) {
			try {
				object = guiHandler.getServerGuiElement(id, player, world, x, y, z);
				if (object != null) {
					break;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return object;
	}

	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		
		Object object = null;
		
		for (IGuiHandler guiHandler : this.guiHandlers) {
			try {
				object = guiHandler.getClientGuiElement(id, player, world, x, y, z);
				if (object != null) {
					break;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return object;
	}
	
}
