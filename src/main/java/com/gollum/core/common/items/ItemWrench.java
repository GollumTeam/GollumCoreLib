package com.gollum.core.common.items;

import com.gollum.core.inits.ModCreativeTab;
import com.gollum.core.tools.helper.items.HItem;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWrench extends HItem {
	
	public ItemWrench(String registerName) {
		super(registerName);
		
		this.setFull3D();
		this.setMaxStackSize(1);
		this.setHarvestLevel("wrench", 0);
		this.setCreativeTab(ModCreativeTab.DEV_TOOLS);
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
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		
		IBlockState state = world.getBlockState(pos);
		if (state == null) {
			return EnumActionResult.PASS;
		}
		
		Block block = state.getBlock();
		
		if (block == null) {
			return EnumActionResult.PASS;
		}
		
		if (block.rotateBlock(world, pos, side)) {
			player.swingArm(hand);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.SUCCESS;
	}
	
}
