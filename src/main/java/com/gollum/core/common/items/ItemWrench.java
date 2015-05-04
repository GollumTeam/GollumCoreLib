package com.gollum.core.common.items;

import java.util.HashSet;
import java.util.Set;

import static com.gollum.core.ModGollumCoreLib.log;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLever;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gollum.core.tools.helper.items.HItem;

public class ItemWrench extends HItem {
	
	public Set<Class<? extends Block>> shiftRotations = new HashSet<Class<? extends Block>>();
	
	public ItemWrench(String registerName) {
		super(registerName);

		setFull3D();
		setMaxStackSize(1);
		shiftRotations.add(BlockLever.class);
		shiftRotations.add(BlockButton.class);
		shiftRotations.add(BlockChest.class);
		setHarvestLevel("wrench", 0);
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
		Block block = world.getBlock(x, y, z);
		
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
	
	@Override
	public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
		return true;
	}
}
