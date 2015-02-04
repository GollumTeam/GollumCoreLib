package mods.gollum.core.tools.registry;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderingRegistry {
	
	public static int registerBlockHandler(ISimpleBlockRenderingHandler renderer) {
		int id = cpw.mods.fml.client.registry.RenderingRegistry.getNextAvailableRenderId();
		cpw.mods.fml.client.registry.RenderingRegistry.registerBlockHandler(id, renderer);
		return id;
	}
}
