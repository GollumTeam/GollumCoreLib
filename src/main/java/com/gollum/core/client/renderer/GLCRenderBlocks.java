package com.gollum.core.client.renderer;

import org.lwjgl.opengl.GL11;

import com.gollum.core.client.handlers.ISimpleBlockRenderingHandler;
import com.gollum.core.common.blocks.ISimpleBlockRendered;
import com.gollum.core.common.blocks.ISimpleBlockRenderedColored;
import com.gollum.core.tools.registry.RenderingRegistry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;

public class GLCRenderBlocks {
	
	public boolean useInventoryTint = true;
	
	public void renderBlockAsItem(Block block, int metadata, float light) {
		
		Tessellator tessellator = Tessellator.getInstance();
		
		if (this.useInventoryTint) {
			int color = 0xFFFFFF;
			if (block instanceof ISimpleBlockRenderedColored) {
				color = ((ISimpleBlockRenderedColored)block).getRenderColor(metadata);
			}
			
			float r = (float)(color >> 16 & 255) / 255.0F;
			float g = (float)(color >> 8 & 255) / 255.0F;
			float b = (float)(color & 255) / 255.0F;
			GL11.glColor4f(r * light, g * light, b * light, 1.0F);
		}
		
		if (block instanceof ISimpleBlockRendered) {
			int modelId = ((ISimpleBlockRendered) block).getGCLRenderType();
			ISimpleBlockRenderingHandler renderHandler = RenderingRegistry.getBlockHandler(modelId);
			if (renderHandler != null) {
				
				GL11.glPushMatrix();
				GL11.glScaled(0.7, 0.7, 0.7);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				renderHandler.renderInventoryBlock(block, metadata, modelId, this);
				GL11.glPopMatrix();
			}
		}
		
		
	}

}
