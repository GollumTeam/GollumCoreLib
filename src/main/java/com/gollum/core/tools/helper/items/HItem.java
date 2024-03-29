package com.gollum.core.tools.helper.items;

import java.util.List;
import java.util.Map;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.tools.helper.IItemHelper;
import com.gollum.core.tools.helper.ItemHelper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HItem extends Item implements IItemHelper {

	protected ItemHelper helper;
	
	public HItem (String registerName) {
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

	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender () {
		helper.registerRender();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return helper.getUnlocalizedName(stack);
	}
	
	@Override
	public void getSubNames(Map<Integer, String> list) {
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		helper.getSubItems(tab, items);
	}

	@Override
	public int getEnabledMetadata(int dammage) {
		return helper.getEnabledMetadata(dammage);
	}
	
}
