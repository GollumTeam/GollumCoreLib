package com.gollum.core.tools.registry;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public class RenderingRegistry {
	
	public static int registerBlockHandler(ISimpleBlockRenderingHandler renderer) {
		int id = net.minecraftforge.fml.client.registry.RenderingRegistry.getNextAvailableRenderId();
		net.minecraftforge.fml.client.registry.RenderingRegistry.registerBlockHandler(id, renderer);
		return id;
	}
	
	public static void registerEntityRenderingHandler(Class<? extends Entity> entityClass, Render renderer) {
		net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(entityClass, renderer);
	}
}
