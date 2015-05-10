package com.gollum.core.client;

import net.minecraftforge.common.MinecraftForge;

import com.gollum.core.common.CommonProxyGolumCoreLib;

public class ClientProxyGolumCoreLib extends CommonProxyGolumCoreLib {

	public void registerEvents () {
		super.registerEvents ();
	}

	public boolean isRemote() {
		return true;
	}
	
}
