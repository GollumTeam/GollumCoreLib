package com.gollum.core.client;

import com.gollum.core.common.CommonProxyGolumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;
//import com.gollum.core.common.handlers.GuiScreenHandler;
//import com.gollum.core.tools.registry.BlockRegistry;
//import com.gollum.core.tools.registry.ItemRegistry;
import com.gollum.core.inits.ModItems;
import com.gollum.core.tools.registry.BlockRegistry;
import com.gollum.core.tools.registry.ItemRegistry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxyGolumCoreLib extends CommonProxyGolumCoreLib {
	
	@Override
	public void registerObjectRenders() {
		
		GollumMod mod = ModContext.instance().getCurrent();

		BlockRegistry.instance().registerRenders();
		ItemRegistry .instance().registerRenders();
	}
	
	@Override
	public void registerEvents () {
		super.registerEvents ();
//		MinecraftForge.EVENT_BUS.register(new GuiScreenHandler());
	}
	
	@Override
	public boolean isRemote() {
		return true;
	}
	
}
