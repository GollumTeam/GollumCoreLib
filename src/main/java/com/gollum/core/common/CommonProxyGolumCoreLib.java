package com.gollum.core.common;

import com.gollum.core.common.handlers.WorldHandler;
import com.gollum.core.common.handlers.WorldTickHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;


public class CommonProxyGolumCoreLib {
	
	
	public void registerEvents () {
		MinecraftForge.EVENT_BUS.register(new WorldHandler());
		MinecraftForge.EVENT_BUS.register(new WorldTickHandler());
	}

	public boolean isRemote() {
		return false;
	}

	public void registerObjectRenders() {
	}

	
}
