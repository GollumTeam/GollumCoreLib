package com.gollum.core.common.items;

import java.util.HashSet;
import java.util.Set;

import com.gollum.core.tools.helper.items.HItem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemWrench extends HItem {
	
	public ItemWrench(String registerName) {
		super(registerName);
		
		this.setFull3D();
		this.setMaxStackSize(1);
		this.setHarvestLevel("wrench", 0);
	}
	
	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Block block = world.getBlock(x, y, z);
		
		if (block == null) {
			return false;
		}
		
		if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
			player.swingItem();
			return !world.isRemote;
		}
		return false;
	}
	
}
