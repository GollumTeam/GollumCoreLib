package com.gollum.core.tools.helper.items;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.tools.helper.IItemHelper;
import com.gollum.core.tools.helper.ItemHelper;

import net.minecraft.item.Item;
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
	
}
