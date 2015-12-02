package com.gollum.core.tools.registry;

import java.util.HashMap;

import com.gollum.core.client.handlers.ISimpleBlockRenderingHandler;
import com.gollum.jammyfurniture.client.render.JFInventoryRenderer;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderingRegistry {

	private static RenderingRegistry instance = new RenderingRegistry();
	
	private int index = 0;
	private HashMap<Integer, ISimpleBlockRenderingHandler> blockRenderHandlers = new HashMap<Integer, ISimpleBlockRenderingHandler>();
	
	public static RenderingRegistry instance () {
		return instance;
	}
	
	@SideOnly(Side.CLIENT)
	public static int registerBlockHandler(ISimpleBlockRenderingHandler renderer) {
		instance.index++;
		instance.blockRenderHandlers.put(instance().index, renderer);
		return instance.index;
	}
	
	@SideOnly(Side.CLIENT)
	public static ISimpleBlockRenderingHandler getBlockHandler(int id) {
		return instance.blockRenderHandlers.get(id);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerEntityRenderingHandler(Class<? extends Entity> entityClass, Render renderer) {
		net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(entityClass, renderer);
	}
}
