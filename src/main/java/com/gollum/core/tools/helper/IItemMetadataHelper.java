package com.gollum.core.tools.helper;

import java.util.List;
import java.util.TreeSet;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IItemMetadataHelper {
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs ctabs, List list);
	
	/**
	 * Liste des metadata enabled pour le subtype
	 */
	TreeSet<Integer> listSubEnabled();
	
	int getEnabledMetadata(int dammage);
	
	public String getUnlocalizedName(ItemStack stack);
	
	public IIcon getIconFromDamage(int metadata);
	
}