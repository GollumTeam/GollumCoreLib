package mods.gollum.core.common.creativetab;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GollumCreativeTabs extends CreativeTabs {
	
	private Block block = null;
	private Item item = null;

	public GollumCreativeTabs(String label) {
		super(label);
	}

	public void setIcon(Block block) {
		this.block = block;
	}

	public void setIcon(Item item) {
		this.item = item;
	}

	@Override
	public ItemStack getIconItemStack() {
		if (item != null) {
			return new ItemStack(item);
		}
		if (block != null) {
			return new ItemStack(block);
		}
		return super.getIconItemStack();
	}
	
	@Override
	public Item getTabIconItem() {
		if (item != null) {
			return item;
		}
		if (block != null) {
			return Item.getItemFromBlock(block);
		}
		return Item.getItemFromBlock(Blocks.grass);
	}
}
