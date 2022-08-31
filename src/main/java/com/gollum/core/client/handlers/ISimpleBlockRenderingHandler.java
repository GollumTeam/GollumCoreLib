package com.gollum.core.client.handlers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderItem;

public interface ISimpleBlockRenderingHandler {

	void renderInventoryBlock(Block block, int metadata, int modelID, RenderItem renderItem);

}
