package com.gollum.core.client;

import net.minecraftforge.common.MinecraftForge;

import com.gollum.core.common.CommonProxyGolumCoreLib;
import com.gollum.core.tools.registry.SoundRegistry;

public class ClientProxyGolumCoreLib extends CommonProxyGolumCoreLib {

	public void registerEvents () {
		
		super.registerEvents ();
		
		MinecraftForge.EVENT_BUS.register(new SoundRegistry());
	}

	public boolean isRemote() {
		return true;
	}
	
}
