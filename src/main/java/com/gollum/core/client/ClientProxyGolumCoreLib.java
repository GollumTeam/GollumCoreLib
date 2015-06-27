package com.gollum.core.client;

import net.minecraftforge.common.MinecraftForge;

import com.gollum.core.common.CommonProxyGolumCoreLib;
import com.gollum.core.common.handlers.GuiScreenHandler;

public class ClientProxyGolumCoreLib extends CommonProxyGolumCoreLib {

	public void registerEvents () {
		super.registerEvents ();
		MinecraftForge.EVENT_BUS.register(new GuiScreenHandler());
	}

	public boolean isRemote() {
		return true;
	}
	
}
