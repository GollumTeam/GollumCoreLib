package com.gollum.core.tools.helper.items;

import java.util.List;
import java.util.Map;

import com.gollum.core.tools.helper.IBlockHelper;
import com.gollum.core.tools.helper.IItemHelper;
import com.gollum.core.tools.helper.ItemHelper;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HItemBlock extends ItemBlock implements IItemHelper {

	protected ItemHelper helper;
	
	public HItemBlock(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.helper = new ItemHelper(this, ((IBlockHelper)block).getRegisterName());
	}

	@Override
	public void register() {
		// Register by block
	}

	@Override
	public ItemHelper getGollumHelper() {
		return this.helper;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender() {
		// Register by block
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return helper.getUnlocalizedName(stack);
	}

	@Override
	public int getEnabledMetadata (int dammage) {
		return helper.getEnabledMetadata(dammage);
	}

	@Override
	public void getSubNames(Map<Integer, String> list) {
		((IBlockHelper)block).getSubNames(list);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		super.getSubItems(tab, items);
	}
	
	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}
}
