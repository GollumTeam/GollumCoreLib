package com.gollum.core.client.renderer;

import static com.gollum.core.ModGollumCoreLib.log;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static com.gollum.core.ModGollumCoreLib.log;
import com.gollum.core.utils.reflection.Reflection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
	int syncProp = 0;
	boolean isInit = false;
	
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
		super.renderItemIntoGUI(stack, x, y);
	}
	
}
