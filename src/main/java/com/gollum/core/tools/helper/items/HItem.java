package com.gollum.core.tools.helper.items;

import java.util.List;
import java.util.Map;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.tools.helper.IItemHelper;
import com.gollum.core.tools.helper.ItemHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HItem extends Item implements IItemHelper {

	protected ItemHelper helper;
	
	public HItem (String registerName) {
		ModGollumCoreLib.log.info ("Create item registerName : " + registerName);
		this.helper = new ItemHelper(this, registerName);
	}
	
	public ItemHelper getGollumHelper () {
		return helper;
	}
	
	/**
	 * Enregistrement du item. Appelé a la fin du postInit
	 */
	public void register () {
		helper.register();
	}
	
	/**
	 * Enregistrement du rendu de l'item. Appelé a la fin de l'Init
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender () {
		helper.registerRender();
	}

	/**
	 * Nom d'enregistrement du mod
	 */
	@Override
	public String getRegisterName() {
		return helper.getRegisterName();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return helper.getUnlocalizedName(stack);
	}
	
	@Override
	public void getSubNames(Map<Integer, String> list) {
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs ctabs, List list) {
		helper.getSubItems(item, ctabs, list);
	}

	@Override
	public int getEnabledMetadata(int dammage) {
		return helper.getEnabledMetadata(dammage);
	}
	
}
