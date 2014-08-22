package mods.gollum.core.registry;

import mods.jammyfurniture.common.render.InvRenderer;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderingRegistry {

	public static int registerBlockHandler(ISimpleBlockRenderingHandler invRenderer) {
		
		int id = cpw.mods.fml.client.registry.RenderingRegistry.getNextAvailableRenderId();
		cpw.mods.fml.client.registry.RenderingRegistry.registerBlockHandler(id, new InvRenderer());
		
		return id;
	}

}
