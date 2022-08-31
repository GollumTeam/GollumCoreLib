package com.gollum.core.client.event;

import com.gollum.core.utils.math.Integer2d;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderItem extends Event {
	
	public net.minecraft.client.renderer.RenderItem renderItem;
	public ItemStack itemStack;
	
	public Integer2d pos;
	
	public RenderItem(net.minecraft.client.renderer.RenderItem renderItem, ItemStack itemStack) {
		this.renderItem = renderItem;
		this.itemStack = itemStack;
	}
	
	public static class Pre extends RenderItem {
		
		
		public Pre(net.minecraft.client.renderer.RenderItem renderItem, ItemStack itemStack, Integer2d pos) {
			super(renderItem, itemStack);
			this.pos = pos;
		}
		public boolean isCancelable() {
			return true;
		}
	}
	
	public static class Post extends RenderItem {
		
		public int x = 0;
		public int y = 0;
		
		public Post(net.minecraft.client.renderer.RenderItem renderItem, ItemStack itemStack, Integer2d pos) {
			super(renderItem, itemStack);
			this.pos = pos;
		}
	}
}
