package com.gollum.core.client.renderer;

import static com.gollum.core.ModGollumCoreLib.logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.lwjgl.opengl.GL11;

import com.gollum.core.client.event.RenderItemEvent;
import com.gollum.core.client.event.RenderItemIntoGuiEvent;
import com.gollum.core.client.handlers.ISimpleBlockRenderingHandler;
import com.gollum.core.common.blocks.ISimpleBlockRendered;
import com.gollum.core.common.blocks.ISimpleBlockRenderedColored;
import com.gollum.core.tools.registry.RenderingRegistry;
import com.gollum.core.utils.math.Integer2d;
import com.gollum.core.utils.reflection.Reflection;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class GCLRenderItem extends RenderItem {
	
	private static GCLRenderItem instance = null;
	
	public static void override () {
		if (instance == null) {
			logger.message("Override RenderItem...");
			instance = new GCLRenderItem(Minecraft.getMinecraft().getRenderItem());
			
			try {
				for (Field f: Minecraft.class.getDeclaredFields()) {
					f.setAccessible(true);
					if (f.getType() == RenderItem.class) {
						f.set(Minecraft.getMinecraft(), instance);
						logger.message("Override RenderItem OK");
						return;
					}
				}
				logger.severe("Override RenderItem KO: "+RenderItem.class.getCanonicalName()+" not found in "+Minecraft.class.getCanonicalName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private RenderItem proxy;
	private boolean isInit = false;
	public boolean renderWithColor = true;
	
	public GCLRenderItem(RenderItem proxy) {
		super(getTextureManager(), getModelManager(), getItemColors());
		
		this.proxy = proxy;
		this.setProxyField();
		this.isInit = true;
	}
	
	protected static TextureManager getTextureManager() {
		return new TextureManager(Minecraft.getMinecraft().getResourceManager());
	}
	
	protected static ModelManager getModelManager () {
		return new ModelManager(Minecraft.getMinecraft().getTextureMapBlocks());
	}
	
	protected static ItemColors getItemColors () {
		return new ItemColors();
	}
	
	private void setProxyField() {
		try {
			for (Field f: RenderItem.class.getDeclaredFields()) {
				f.setAccessible(true);
				if((f.getModifiers() & Modifier.STATIC) != Modifier.STATIC) {
					if((f.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
						Reflection.setFinalField(f, this, f.get(this.proxy));
					} else {
						f.set(this, f.get(this.proxy));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void registerItem(Item itm, int subType, String identifier) {
		if (this.isInit) {
			super.registerItem(itm, subType, identifier);
		}
	}
	
	@Override
	public void renderItem(ItemStack stack, IBakedModel model) {
		
		
		RenderItemEvent event = new RenderItemEvent.Pre(this, stack, model);
		if (event.isCanceled()) {
			return;
		}
		stack = event.itemStack;
		model = event.model;
		
		if (!this.renderBlockAsItem(stack)) {
			super.renderItem(stack, model);
		}
		
		event = new RenderItemEvent.Post(this, stack, model);
		MinecraftForge.EVENT_BUS.post(event);
	}

	protected boolean renderBlockAsItem(ItemStack stack) {
		
		Block block = Block.getBlockFromItem(stack.getItem());
		int metadata = stack.getItemDamage();
		
		if (block instanceof ISimpleBlockRendered) {
			int modelId = ((ISimpleBlockRendered) block).getGCLRenderType();
			ISimpleBlockRenderingHandler renderHandler = RenderingRegistry.getBlockHandler(modelId);
			if (renderHandler != null) {
		
				Tessellator tessellator = Tessellator.getInstance();
		
				if (this.renderWithColor) {
					int color = 0xFFFFFF;
					if (block instanceof ISimpleBlockRenderedColored) {
						color = ((ISimpleBlockRenderedColored)block).getRenderColor(metadata);
					}
					
					float r = (float)(color >> 16 & 255) / 255.0F;
					float g = (float)(color >> 8 & 255) / 255.0F;
					float b = (float)(color & 255) / 255.0F;
					GL11.glColor4f(r, g, b, 1.0F);
				}
				
				GL11.glPushMatrix();
				GL11.glScaled(0.5, 0.5, 0.5);
				GL11.glRotated(90, 0, 1, 0);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				renderHandler.renderInventoryBlock(block, metadata, modelId, this);
				GL11.glPopMatrix();
				
				return true;
			}
		}
		return false;
		
	}

	@Override
	public void renderItemIntoGUI(ItemStack stack, int x, int y) {
		
		RenderItemIntoGuiEvent event = new RenderItemIntoGuiEvent.Pre(this, stack, new Integer2d(x, y));
		MinecraftForge.EVENT_BUS.post(event);  
		if (event.isCanceled()) {
			return;
		}
		x = event.pos.x;
		y = event.pos.y;
		stack = event.itemStack;
		
		super.renderItemIntoGUI(stack, x, y);
		
		event = new RenderItemIntoGuiEvent.Post(this, stack, new Integer2d(x, y));
		MinecraftForge.EVENT_BUS.post(event);
	}
}
