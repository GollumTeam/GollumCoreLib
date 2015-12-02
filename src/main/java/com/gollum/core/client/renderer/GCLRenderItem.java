package com.gollum.core.client.renderer;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.lwjgl.opengl.GL11;

import com.gollum.core.client.event.RenderItemEvent;
import com.gollum.core.client.handlers.ISimpleBlockRenderingHandler;
import com.gollum.core.common.blocks.ISimpleBlockRendered;
import com.gollum.core.common.events.BuildingGenerateEvent;
import com.gollum.core.tools.registry.RenderingRegistry;
import com.gollum.core.utils.reflection.Reflection;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class GCLRenderItem extends RenderItem {
	
	private static GCLRenderItem instance = null;
	
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
	
	@Override
	public void renderItemIntoGUI(ItemStack stack, int x, int y) {
		
		RenderItemEvent.RenderItemIntoGUI event = new RenderItemEvent.RenderItemIntoGUI.Pre(this, stack, x, y);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.isCanceled()) {
			return;
		}
		x = event.x;
		y = event.y;
		stack = event.itemStack;
		
		Block block = Block.getBlockFromItem(event.itemStack.getItem());
		if (block instanceof ISimpleBlockRendered) {
			int id = ((ISimpleBlockRendered) block).getGCLRenderType();
			ISimpleBlockRenderingHandler renderHandler = RenderingRegistry.getBlockHandler(id);
			if (renderHandler != null) {
				// TODO metadata
				
				GL11.glEnable(GL11.GL_ALPHA_TEST);

//				if (block.getRenderBlockPass() != 0) {
//					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
//					GL11.glEnable(GL11.GL_BLEND);
//					OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//				} else {
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
					GL11.glDisable(GL11.GL_BLEND);
//				}
				
				GL11.glPushMatrix();
				GL11.glTranslatef((float)(x - 2), (float)(y + 3), -3.0F + this.zLevel);
				GL11.glScalef(10.0F, 10.0F, 10.0F);
				GL11.glTranslatef(1.0F, 0.5F, 1.0F);
				GL11.glScalef(1.0F, 1.0F, -1.0F);
				GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
				int l = stack.getItem().getColorFromItemStack(stack, 0);
				float f3 = (float)(l >> 16 & 255) / 255.0F;
				float f4 = (float)(l >> 8 & 255) / 255.0F;
				float f = (float)(l & 255) / 255.0F;

//				if (this.renderWithColor)
//				{
//					GL11.glColor4f(f3, f4, f, 1.0F);
//				}

				GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//				this.renderBlocksRi.useInventoryTint = this.renderWithColor;
//				this.renderBlocksRi.renderBlockAsItem(block, k, 1.0F);
//				this.renderBlocksRi.useInventoryTint = true;
				renderHandler.renderInventoryBlock(block, stack.getItemDamage(), id, new GLCRenderBlocks());

//				if (block.getRenderBlockPass() == 0)
//				{
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
//				}
				
				GL11.glPopMatrix();
				
			} else {
				log.severe("ISimpleBlockRenderingHandler with id "+id+" not found");
			}
		} else {
			super.renderItemIntoGUI(stack, x, y);
		}
		
		event = new RenderItemEvent.RenderItemIntoGUI.Post(this, stack, x, y);
		MinecraftForge.EVENT_BUS.post(event);
	}
	
}
