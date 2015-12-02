package com.gollum.core.common.blocks;

import com.gollum.core.client.renderer.GLCRenderBlocks;

public interface ISimpleBlockRenderedColored extends ISimpleBlockRendered {
	
	/**
	 * @return return 0xFFFFFF for default
	 * @see GLCRenderBlocks:renderBlockAsItem
	 */
	public int getRenderColor(int metadata);
	
}
