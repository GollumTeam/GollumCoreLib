package com.gollum.core.tools.helper;

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
	
}