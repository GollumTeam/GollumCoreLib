package com.gollum.core.tools.helper;

public interface IItemHelper {
	
	public ItemHelper getGollumHelper ();
	
	/**
	 * Enregistrement du item. Appelé a la fin du postInit
	 */
	public void register ();
	
	/**
	 * Nom d'enregistrement du mod
	 */
	public String getRegisterName();
	
	/* TODO
	public void registerIcons(IIconRegister iconRegister);
	*/
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	/* TODO
	public void setIcon (IIcon icon);
	*/
	
	/**
	 * Clef qui permet de générer le nom du fichier de texture 
	 * par rapport au register name en miniscule
	 * @return
	 */
	public String getTextureKey ();
	
	
	
}