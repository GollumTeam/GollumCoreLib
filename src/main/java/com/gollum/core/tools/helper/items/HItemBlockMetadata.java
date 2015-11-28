package com.gollum.core.tools.helper.items;

import com.gollum.core.tools.helper.BlockHelper;
import com.gollum.core.tools.helper.BlockMetadataHelper;
import com.gollum.core.tools.helper.IBlockHelper;
import com.gollum.core.tools.helper.IBlockMetadataHelper;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class HItemBlockMetadata extends ItemBlock {
	
	protected Block block;
	
	public HItemBlockMetadata(int id, Block block) {
		super(id);
		this.setHasSubtypes(true);
		this.block = block;
	}
	
	/**
	 * Returns the unlocalized name of this item. This version accepts an
	 * ItemStack so different stacks can have different names based on their
	 * damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack itemStack) {
		
		int dammage = itemStack.getItemDamage();
		
		// Castage du helper
		BlockHelper         blockHelper         = ((IBlockHelper)         this.block).getGollumHelper ();
		BlockMetadataHelper blockMetadataHelper = ((IBlockMetadataHelper) this.block).getGollumHelperMetadata ();
		
		return this.getUnlocalizedName() + "." + blockMetadataHelper.getEnabledMetadata (dammage);
	}
	
	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1;
	}
}
