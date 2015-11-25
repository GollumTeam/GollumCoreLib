package com.gollum.core.common.items;

import java.util.HashSet;
import java.util.Set;

import com.gollum.core.tools.helper.items.HItem;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemWrench extends HItem {
	
	public Set<Class<? extends Block>> shiftRotations = new HashSet<Class<? extends Block>>();
	
	public ItemWrench(String registerName) {
		super(registerName);
		
		this.setFull3D();
		this.setMaxStackSize(1);
		this.shiftRotations.add(BlockLever.class);
		this.shiftRotations.add(BlockButton.class);
		this.shiftRotations.add(BlockChest.class);
		this.setHarvestLevel("wrench", 0);
	}

	private boolean isShiftRotation(Class<? extends Block> cls) {
		for (Class<? extends Block> shift : shiftRotations) {
			if (shift.isAssignableFrom(cls)) {
				return true;
			}
		}
		return false;
	}

    /**
     * This is called when the item is used, before the block is activated.
     * @param stack The Item Stack
     * @param player The Player that used the item
     * @param world The Current World
     * @param pos Target position
     * @param side The side of the target hit
     * @return Return true to prevent any further processing.
     */
	@Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		
		IBlockState state = world.getBlockState(pos);
		if (state == null) {
			return false;
		}
		
		Block block = state.getBlock();
		
		if (block == null) {
			return false;
		}

		if (player.isSneaking() != isShiftRotation(block.getClass())) {
			return false;
		}
		
		if (block.rotateBlock(world, pos, side)) {
			player.swingItem();
			return !world.isRemote;
		}
		return false;
	}
	
}
