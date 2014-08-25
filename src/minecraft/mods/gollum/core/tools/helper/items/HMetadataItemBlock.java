package mods.gollum.core.tools.helper.items;

import mods.gollum.core.tools.helper.BlockHelper;
import mods.gollum.core.tools.helper.BlockMetadataHelper;
import mods.gollum.core.tools.helper.IBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class HMetadataItemBlock extends ItemBlock {
	
	Block block;
	
	public HMetadataItemBlock(int par1, Block block) {
		super(par1);
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
		BlockHelper blockHelper = ((IBlockHelper) this.block).getGollumHelper ();
		BlockMetadataHelper blockMetadataHelper = (BlockMetadataHelper)blockHelper;
		
		return this.getUnlocalizedName() + "." + blockMetadataHelper.getEnabledMetadata (dammage);
	}
	
	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	public int getMetadata(int par1) {
		return par1;
	}
}
