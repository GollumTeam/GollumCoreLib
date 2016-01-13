package com.gollum.core.tools.helper.items;

import java.util.ArrayList;
import java.util.TreeSet;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class HItemBlockMetadata extends ItemBlock {
	
	public HItemBlockMetadata(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	public String getUnlocalizedName(ItemStack itemStack) {
		int dammage = itemStack.getItemDamage();
		return this.getUnlocalizedName() + "." + this.getEnabledMetadata (dammage);
	}
	
	public int getEnabledMetadata (int dammage) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		this.getSubItems(this, (CreativeTabs)null, list);
		TreeSet<Integer> listSubEnabled  = new TreeSet<Integer>();
		for (ItemStack is :list) {
			if (!listSubEnabled.contains(is.getItemDamage())) {
				listSubEnabled.add(is.getItemDamage());
			}
		}
		
		int lastSubblock = -1;
		for (Integer metadata : listSubEnabled) {
			if (metadata  > dammage) {
				break;
			}
			lastSubblock = metadata;			
		}
		
		return (lastSubblock == -1) ? dammage : lastSubblock;
	}
	
	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1;
	}
}
