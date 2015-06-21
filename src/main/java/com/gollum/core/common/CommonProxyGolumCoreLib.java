package com.gollum.core.common;

import net.minecraftforge.common.MinecraftForge;

import com.gollum.core.common.handlers.WorldHandler;
import com.gollum.core.common.handlers.WorldTickHandler;
import com.gollum.core.common.version.VersionChecker.EnterWorldHandler;

import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxyGolumCoreLib {
	
	public void registerEvents () {
		MinecraftForge.EVENT_BUS.register(new WorldHandler());
		TickRegistry.registerTickHandler(new WorldTickHandler(), Side.SERVER);
	}

	public boolean isRemote() {
		return false;
	}
	
}
