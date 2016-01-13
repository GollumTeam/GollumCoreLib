package com.gollum.core.client.event;

import com.gollum.core.utils.math.Integer2d;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderItemIntoGuiEvent extends Event {

	public RenderItem renderItem;
	public ItemStack itemStack;
	public Integer2d pos;
	
	public RenderItemIntoGuiEvent(RenderItem renderItem, ItemStack itemStack, Integer2d pos) {
		this.renderItem = renderItem;
		this.itemStack = itemStack;
		this.pos = pos;
	}

	public static class Pre extends RenderItemIntoGuiEvent {
		
		public Pre(RenderItem renderItem, ItemStack itemStack, Integer2d pos) {
			super(renderItem, itemStack, pos);
		}
		public boolean isCancelable() {
			return true;
		}
	}
	
	public static class Post extends RenderItemIntoGuiEvent {
		
		public Post(RenderItem renderItem, ItemStack itemStack, Integer2d pos) {
			super(renderItem, itemStack, pos);
		}
	}
}
