package com.gollum.core.client.event;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderItemEvent extends Event {

	public RenderItem renderItem;
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
