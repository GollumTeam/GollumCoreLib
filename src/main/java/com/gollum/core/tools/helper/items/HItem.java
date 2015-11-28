package com.gollum.core.tools.helper.items;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.tools.helper.IItemHelper;
import com.gollum.core.tools.helper.ItemHelper;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

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
	 * Nom d'enregistrement du mod
	 */
	@Override
	public String getRegisterName() {
		return helper.getRegisterName();
	}
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	@Override
	public void setIcon (IIcon icon) {
		this.itemIcon = icon;
	}
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		if (helper.vanillaTexture) super.registerIcons(iconRegister); else helper.registerIcons(iconRegister);
	}
	
	/**
	 * Clef qui permet de générer le nom du fichier de texture 
	 * par rapport au register name en miniscule
	 * @return
	 */
	@Override
	public String getTextureKey() {
		return helper.getTextureKey();
	}
}
