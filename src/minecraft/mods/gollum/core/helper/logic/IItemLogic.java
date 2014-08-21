package mods.gollum.core.helper.logic;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public interface IItemLogic {
	
	/**
	 * Nom d'enregistrement du mod
	 */
	public String getRegisterName();
	
	public void registerIcons(IconRegister iconRegister);
	
	/**
	 * Clef qui permet de générer le nom du fichier de texture 
	 * par rapport au register name en miniscule
	 * @return
	 */
	public String getTextureKey ();
	
	
	
}