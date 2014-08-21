package mods.gollum.core.helper.items;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.helper.logic.IItemLogic;
import mods.gollum.core.helper.logic.ItemLogic;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public class Item extends net.minecraft.item.Item implements IItemLogic {

	protected ItemLogic logic;
	
	public Item (int id, String registerName) {
		super(id);
		ModGollumCoreLib.log.info ("Create item id : " + id + " registerName : " + registerName);
		this.logic = new ItemLogic(this, registerName);
	}
	
	/**
	 * Nom d'enregistrement du mod
	 */
	@Override
	public String getRegisterName() {
		return logic.getRegisterName();
	}
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		if (logic.naturalTexture) super.registerIcons(iconRegister); else logic.registerIcons(iconRegister);
	}
	
	/**
	 * Clef qui permet de générer le nom du fichier de texture 
	 * par rapport au register name en miniscule
	 * @return
	 */
	@Override
	public String getTextureKey() {
		return logic.getTextureKey();
	}
}
