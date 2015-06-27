package com.gollum.core.common;

import net.minecraftforge.common.MinecraftForge;

import com.gollum.core.common.handlers.WorldHandler;
import com.gollum.core.common.handlers.WorldTickHandler;

import cpw.mods.fml.common.FMLCommonHandler;


public class CommonProxyGolumCoreLib {
	
	
	public void registerEvents () {
		MinecraftForge.EVENT_BUS.register(new WorldHandler());
		FMLCommonHandler.instance().bus().register(new WorldTickHandler());
	}

	public boolean isRemote() {
		return false;
	}
	
}
