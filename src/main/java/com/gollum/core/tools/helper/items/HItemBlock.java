package com.gollum.core.tools.helper.items;

import java.util.HashMap;
import java.util.List;

import com.gollum.core.tools.helper.IBlockHelper;
import com.gollum.core.tools.helper.IItemHelper;
import com.gollum.core.tools.helper.ItemHelper;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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
	public ItemHelper getGollumHelper() {
		return this.helper;
	}

	@Override
	public String getRegisterName() {
		return helper.getRegisterName();
	}

	@Override
	public void getSubNames(HashMap<Integer, String> list) {
		((IBlockHelper)block).getSubNames(list);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs ctabs, List list) {
		super.getSubItems(item, ctabs, list);
	}
	
	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int metadata) {
		return metadata;
	}
}
