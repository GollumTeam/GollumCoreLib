package com.gollum.core.client;

import com.gollum.core.common.CommonProxyGolumCoreLib;
import com.gollum.core.common.handlers.GuiScreenHandler;
import com.gollum.core.tools.registry.BlockRegistry;
import com.gollum.core.tools.registry.ItemRegistry;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxyGolumCoreLib extends CommonProxyGolumCoreLib {
	
	@Override
	public void registerObjectRenders() {
		BlockRegistry.instance().registerRenders();
		ItemRegistry .instance().registerRenders();
	}
	
	@Override
	public void registerEvents () {
		super.registerEvents ();
		MinecraftForge.EVENT_BUS.register(new GuiScreenHandler());
	}
	
	@Override
	public boolean isRemote() {
		return true;
	}
	
}
