package com.gollum.core.client.renderer;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.lwjgl.opengl.GL11;

import com.gollum.core.client.event.RenderItemEvent;
import com.gollum.core.client.event.RenderItemIntoGuiEvent;
import com.gollum.core.client.handlers.ISimpleBlockRenderingHandler;
import com.gollum.core.common.blocks.ISimpleBlockRendered;
import com.gollum.core.tools.registry.RenderingRegistry;
import com.gollum.core.utils.math.Integer2d;
import com.gollum.core.utils.reflection.Reflection;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class GCLRenderItem extends RenderItem {
	
	private static GCLRenderItem instance = null;
	
	public boolean renderWithColor = true;
	public GLCRenderBlocks renderBlocksRi = new GLCRenderBlocks();
	
	protected GCLRenderItem(TextureManager textureManager, ModelManager modelManager) {
		super(textureManager, modelManager);
	}
	
	public static void override () {
		if (instance == null) {
			log.message("Override RenderItem...");
			instance = new GCLRenderItem(Minecraft.getMinecraft().getRenderItem());
			
			try {
				boolean found = false;
				for (Field f: Minecraft.class.getDeclaredFields()) {
					f.setAccessible(true);
					if (f.getType() == RenderItem.class) {
						f.set(Minecraft.getMinecraft(), instance);
						log.message("Override RenderItem OK");
						return;
					}
				}
				log.severe("Override RenderItem KO: "+RenderItem.class.getCanonicalName()+" not found in "+Minecraft.class.getCanonicalName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private RenderItem proxy;
	private boolean isInit = false;
	
	public GCLRenderItem(RenderItem proxy) {
		super(getResourceManager(), getTextureMapBlocks());
		this.proxy = proxy;
		this.setProxyField();
		this.isInit = true;
	}
	
	protected static TextureManager getResourceManager () {
		return new TextureManager(Minecraft.getMinecraft().getResourceManager());
	}
	
	protected static ModelManager getTextureMapBlocks () {
		return new ModelManager(Minecraft.getMinecraft().getTextureMapBlocks());
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
			this.registerItem(itm, subType, identifier);
		}
	}
	

	public void renderItem(ItemStack stack, IBakedModel model) {
		
		boolean rendered = false;
		
		RenderItemEvent event = new RenderItemEvent.Pre(this, stack);
		if (event.isCanceled()) {
			return;
		}
		stack = event.itemStack;
		
		Block block = Block.getBlockFromItem(stack.getItem());
		if (block instanceof ISimpleBlockRendered) {
			int modelId = ((ISimpleBlockRendered) block).getGCLRenderType();
			ISimpleBlockRenderingHandler renderHandler = RenderingRegistry.getBlockHandler(modelId);
			if (renderHandler != null) {
				this.renderBlocksRi.useInventoryTint = this.renderWithColor;
				this.renderBlocksRi.renderBlockAsItem(block, stack.getItemDamage(), 1.0F);
				this.renderBlocksRi.useInventoryTint = true;
				rendered = true;
			}
		}
		
		if (!rendered) {
			super.renderItem(stack, model);
		}
		
		event = new RenderItemEvent.Post(this, stack);
		MinecraftForge.EVENT_BUS.post(event);
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
		
		if (!this.renderBlockHandlerIntoGUI(stack, x, y, Block.getBlockFromItem(stack.getItem())) ) {
			super.renderItemIntoGUI(stack, x, y);
		}
		
		event = new RenderItemIntoGuiEvent.Post(this, stack, new Integer2d(x, y));
		MinecraftForge.EVENT_BUS.post(event);
	}

	
	protected boolean renderBlockHandlerIntoGUI(ItemStack stack, int x, int y, Block block) {
		
		if (true || !(block instanceof ISimpleBlockRendered)) {
			return false;
		}
		
		int modelId = ((ISimpleBlockRendered) block).getGCLRenderType();
		ISimpleBlockRenderingHandler renderHandler = RenderingRegistry.getBlockHandler(modelId);
		if (renderHandler != null) {
			
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
			GL11.glDisable(GL11.GL_BLEND);
			
			GL11.glPushMatrix();
			GL11.glTranslatef((float)(x - 2), (float)(y + 3), -3.0F + this.zLevel);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			int color = stack.getItem().getColorFromItemStack(stack, 0);
			float r = (float)(color  >> 16 & 255) / 255.0F;
			float g = (float)(color  >> 8 & 255) / 255.0F;
			float b = (float)(color & 255) / 255.0F;
			
			if (this.renderWithColor) {
				GL11.glColor4f(r, g, b, 1.0F);
			}
			
			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			this.renderBlocksRi.useInventoryTint = this.renderWithColor;
			this.renderBlocksRi.renderBlockAsItem(block, stack.getItemDamage(), 1.0F);
			this.renderBlocksRi.useInventoryTint = true;
			
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			
			GL11.glPopMatrix();
			
		} else {
			log.severe("ISimpleBlockRenderingHandler with id "+modelId+" not found");
		}
		
		return true;
	}
	
}
