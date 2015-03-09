package mods.gollum.core.tools.helper;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public interface IItemHelper {
	
	public ItemHelper getGollumHelper ();
	
	/**
	 * Nom d'enregistrement du mod
	 */
	public String getRegisterName();
	
	public void registerIcons(IconRegister iconRegister);
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	public void setIcon (Icon icon);
	
	/**
	 * Clef qui permet de générer le nom du fichier de texture 
	 * par rapport au register name en miniscule
	 * @return
	 */
	public String getTextureKey ();
	
	
	
}