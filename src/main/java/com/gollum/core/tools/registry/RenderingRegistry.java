package com.gollum.core.tools.registry;

import mods.rbd.client.render.mobs.RenderFaery;
import mods.rbd.common.entities.EntityFireFaery;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderingRegistry {
	
	public static int registerBlockHandler(ISimpleBlockRenderingHandler renderer) {
		int id = cpw.mods.fml.client.registry.RenderingRegistry.getNextAvailableRenderId();
		cpw.mods.fml.client.registry.RenderingRegistry.registerBlockHandler(id, renderer);
		return id;
	}
	

	public static void registerEntityRenderingHandler(Class<? extends Entity> entityClass, Render renderer) {
		cpw.mods.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(entityClass, renderer);
	}
}
