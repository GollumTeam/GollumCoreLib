package com.gollum.core.client.event;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderItemEvent extends Event {

	public net.minecraft.client.renderer.RenderItem renderItem;
	public ItemStack itemStack;
	public IBakedModel model;
	
	public RenderItemEvent(RenderItem renderItem, ItemStack itemStack, IBakedModel model) {
		this.renderItem = renderItem;
		this.itemStack = itemStack;
		this.model = model;
	}

	public static class Pre extends RenderItemEvent {
		
		public Pre(RenderItem renderItem, ItemStack itemStack, IBakedModel model) {
			super(renderItem, itemStack, model);
		}
		public boolean isCancelable() {
			return true;
		}
	}
	
	public static class Post extends RenderItemEvent {
		
		public Post(RenderItem renderItem, ItemStack itemStack, IBakedModel model) {
			super(renderItem, itemStack, model);
		}
	}
	
}
