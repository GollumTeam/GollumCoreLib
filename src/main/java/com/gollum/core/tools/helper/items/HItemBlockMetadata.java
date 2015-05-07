package com.gollum.core.tools.helper.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.gollum.core.tools.helper.BlockHelper;
import com.gollum.core.tools.helper.BlockMetadataHelper;
import com.gollum.core.tools.helper.IBlockHelper;
import com.gollum.core.tools.helper.IBlockMetadataHelper;

public class HItemBlockMetadata extends ItemBlock {
	
	protected int blockID;
	
	public HItemBlockMetadata(int id) {
		super(id);
		this.blockID = id;
		this.setHasSubtypes(true);
	}
	
	/**
	 * Returns the unlocalized name of this item. This version accepts an
	 * ItemStack so different stacks can have different names based on their
	 * damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack itemStack) {
		
		int dammage = itemStack.getItemDamage();
		
		// Castage du helper
		BlockHelper         blockHelper         = ((IBlockHelper)         Block.blocksList[this.blockID]).getGollumHelper ();
		BlockMetadataHelper blockMetadataHelper = ((IBlockMetadataHelper) Block.blocksList[this.blockID]).getGollumHelperMetadata ();
		
		return this.getUnlocalizedName() + "." + blockMetadataHelper.getEnabledMetadata (dammage);
	}
	
	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1;
	}
}
