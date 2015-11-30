package com.gollum.core.client;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;

import com.gollum.core.client.renderer.GCLRenderItem;
import com.gollum.core.common.CommonProxyGolumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.handlers.GuiScreenHandler;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.inits.ModItems;
import com.gollum.core.tools.registry.BlockRegistry;
import com.gollum.core.tools.registry.ItemRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxyGolumCoreLib extends CommonProxyGolumCoreLib {
	
	@Override
	public void registerObjectRenders() {
		
//		BlockRegistry.instance().registerAll();
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
	
	public void overrideRenderItem() {
		try {
			log.message("Override RenderItem...");
			GCLRenderItem renderItem = new GCLRenderItem(Minecraft.getMinecraft().getRenderItem());
			for (Field f: Minecraft.class.getFields()) {
				f.setAccessible(true);
				if (f.getType() == RenderItem.class) {
					f.set(Minecraft.getMinecraft(), renderItem);
					log.message("Override RenderItem OK");
					break;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
