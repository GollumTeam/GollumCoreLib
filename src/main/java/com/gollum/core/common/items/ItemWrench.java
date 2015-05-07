package com.gollum.core.common.items;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.gollum.core.tools.helper.items.HItem;

public class ItemWrench extends HItem {
	
	public Set<Class<? extends Block>> shiftRotations = new HashSet<Class<? extends Block>>();
	
	public ItemWrench(int id, String registerName) {
		super(id, registerName);

		this.setFull3D();
		this.setMaxStackSize(1);
		this.shiftRotations.add(BlockLever.class);
		this.shiftRotations.add(BlockButton.class);
		this.shiftRotations.add(BlockChest.class);
	}

	private boolean isShiftRotation(Class<? extends Block> cls) {
		for (Class<? extends Block> shift : shiftRotations) {
			if (shift.isAssignableFrom(cls)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		int id = world.getBlockId(x, y, z);
		Block block = Block.blocksList[id];
		
		if (block == null) {
			return false;
		}

		if (player.isSneaking() != isShiftRotation(block.getClass())) {
			return false;
		}
		
		if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
			player.swingItem();
			return !world.isRemote;
		}
		return false;
	}
	
}
