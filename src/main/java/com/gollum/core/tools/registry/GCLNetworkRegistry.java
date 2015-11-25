package com.gollum.core.tools.registry;

import java.util.ArrayList;

import net.minecraftforge.fml.common.network.IGuiHandler;


public class GCLNetworkRegistry {
	
	private static GCLNetworkRegistry instance = new GCLNetworkRegistry();
	
	ArrayList<IGuiHandler> guiHandlers = new ArrayList<IGuiHandler>();
	
	public static GCLNetworkRegistry instance () {
		return instance;
	}

	public void registerGuiHandler(IGuiHandler guiHandler) {
		this.guiHandlers.add(guiHandler);
	}
	
	public ArrayList<IGuiHandler> getGuiHandlers () {
		ArrayList<IGuiHandler> guiHandlers = this.guiHandlers;
		this.guiHandlers = new ArrayList<IGuiHandler>();
		return guiHandlers;
	}
}
