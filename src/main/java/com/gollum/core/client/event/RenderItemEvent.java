package com.gollum.core.client.event;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderItemEvent extends Event {
	
	public RenderItem renderItem;
	public ItemStack itemStack;
	
	public RenderItemEvent(RenderItem renderItem, ItemStack itemStack) {
		this.renderItem = renderItem;
		this.itemStack = new ItemStack(itemStack.getItem(), itemStack.stackSize, itemStack.getItemDamage());
	}
	
	public static class RenderItemIntoGUI extends RenderItemEvent {
		
		public RenderItemIntoGUI(RenderItem renderItem, ItemStack itemStack) {
			super(renderItem, itemStack);
		}
		
		public int x = 0;
		public int y = 0;
		
		public static class Pre extends RenderItemIntoGUI {
			
			
			public Pre(RenderItem renderItem, ItemStack itemStack, int x, int y) {
				super(renderItem, itemStack);
				this.x = x;
				this.y = y;
			}
			public boolean isCancelable() {
				return true;
			}
		}
		
		public static class Post extends RenderItemIntoGUI {
			
			public int x = 0;
			public int y = 0;
			
			public Post(RenderItem renderItem, ItemStack itemStack, int x, int y) {
				super(renderItem, itemStack);
				this.x = x;
				this.y = y;
			}
		}
	}
}
