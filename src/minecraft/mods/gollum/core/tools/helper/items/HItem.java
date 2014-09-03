package mods.gollum.core.tools.helper.items;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.tools.helper.IItemHelper;
import mods.gollum.core.tools.helper.ItemHelper;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class HItem extends net.minecraft.item.Item implements IItemHelper {

	protected ItemHelper helper;
	
	public HItem (int id, String registerName) {
		super(id);
		ModGollumCoreLib.log.info ("Create item id : " + id + " registerName : " + registerName);
		this.helper = new ItemHelper(this, registerName);
	}
	
	public ItemHelper getGollumHelper () {
		return helper;
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
	public void setIcon (Icon icon) {
		this.itemIcon = icon;
	}
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
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
