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

//	    ModelLoader.setCustomModelResourceLocation(ModItems.INFOS, 0, new ModelResourceLocation(mod.getModId()+":infos", "inventory"));
//	    ModelLoader.setCustomModelResourceLocation(ModItems.BUILDING, 0, new ModelResourceLocation(mod.getModId()+":building", "inventory"));
//	    ModelLoader.setCustomModelResourceLocation(ModItems.BUILDING, 1, new ModelResourceLocation(mod.getModId()+":building", "inventory"));
//	    ModelLoader.setCustomModelResourceLocation(ModItems.BUILDING, 2, new ModelResourceLocation(mod.getModId()+":building", "inventory"));
//	    ModelLoader.setCustomModelResourceLocation(ModItems.BUILDING, 3, new ModelResourceLocation(mod.getModId()+":building", "inventory"));
//	    ModelLoader.setCustomModelResourceLocation(ModItems.BUILDING, 4, new ModelResourceLocation(mod.getModId()+":building", "inventory"));
//	    ModelLoader.setCustomModelResourceLocation(ModItems.WRENCH, 0, new ModelResourceLocation(mod.getModId()+":wrench", "inventory"));
	    
//		BlockRegistry.instance().registerRenders();
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
