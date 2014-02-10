package mods.gollum.core.creativetab;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
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
}
