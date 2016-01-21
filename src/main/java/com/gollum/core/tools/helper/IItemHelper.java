package com.gollum.core.tools.helper;

import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemHelper {
	
	public ItemHelper getGollumHelper ();
	
	/**
	 * Enregistrement du item. Appelé a la fin du postInit
	 */
	public void register ();
	
	/**
	 * Enregistrement du rendu de l'item. Appelé a la fin de l'Init
	 */
	@SideOnly(Side.CLIENT)
	public void registerRender ();
	
	/**
	 * Nom d'enregistrement du mod
	 */
	public String getRegisterName();
	
	public void getSubNames(Map<Integer, String> list);
	
	public void getSubItems(Item item, CreativeTabs ctabs, List list);

	int getEnabledMetadata(int dammage);
	
	public String getUnlocalizedName(ItemStack stack);
}