package com.gollum.core.client.handlers;

import com.gollum.core.client.renderer.GLCRenderBlocks;

import net.minecraft.block.Block;

public interface ISimpleBlockRenderingHandler {

	void renderInventoryBlock(Block block, int metadata, int modelID, GLCRenderBlocks renderer);

}
