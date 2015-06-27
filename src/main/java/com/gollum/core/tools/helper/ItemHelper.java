package com.gollum.core.tools.helper;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.tools.helper.items.HItem;
import com.gollum.core.tools.registry.ItemRegistry;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemHelper implements IItemHelper {
	
	// Pour chaque element natural. Utilise le fonctionnement naturel mais pas des helper
	// Une sorte de config
	// Par defaut le helper vas enregistrer l'item, charger des texture perso ...
	public boolean vanillaRegister      = false;
	public boolean vanillaTexture       = false;
	
	protected GollumMod mod;
	protected Item parent;
	protected String registerName;

	public ItemHelper (Item item, String registerName) {
		this.parent       = item;
		this.registerName = registerName;
		this.mod          = ModContext.instance().getCurrent();
		
		if (!vanillaRegister) ItemRegistry.instance().add((HItem) this.parent);
	}

	@Override
	public ItemHelper getGollumHelper() {
		return this;
	}
	
	public void register () {
		this.parent.setUnlocalizedName(this.registerName);
		GameRegistry.registerItem (this.parent, this.getRegisterName (), this.mod.getModId());
	}
	
	/**
	 * Nom d'enregistrement du mod
	 */
	public String getRegisterName() {
		return registerName;
	}
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	/**
	 * Clef qui permet de générer le nom du fichier de texture 
	 * par rapport au register name en miniscule
	 * @return
	 */
	@Override
	public String getTextureKey () {
		return ((IItemHelper)this.parent).getRegisterName().toLowerCase();
	}
	
	/**
	* Charge une texture et affiche dans le log.
	* Utilise le register name comme prefixe sauf si useTextureKey est à false
	*
	* @param iconRegister
	* @param key
	* @return
	*/
	public IIcon loadTexture(IIconRegister iconRegister) {
		return this.loadTexture(iconRegister, "");
	}
	/**
	* Charge une texture et affiche dans le log.
	* Utilise le register name comme prefixe sauf si useTextureKey est à false
	*
	* @param iconRegister
	* @param key
	* @return
	*/
	public IIcon loadTexture(IIconRegister iconRegister, String sufixe) {
		return this.loadTexture(iconRegister, sufixe, false);
	}
	/**
	* Charge une texture et affiche dans le log.
	* Utilise le register name comme prefixe sauf si dontUseTextureKey est à false
	* 
	* @param iconRegister
	* @param key
	* @return
	*/
	public IIcon loadTexture(IIconRegister iconRegister, String sufixe, boolean dontUseTextureKey) {
		
		String key = (dontUseTextureKey ?  "" : ((IItemHelper)this.parent).getTextureKey ())+sufixe;
		String texture = this.mod.getModId().toLowerCase() + ":" + key;
		
		ModGollumCoreLib.log.debug ("Register icon " + texture + "\"");
		return iconRegister.registerIcon(texture);
	}
	
	/**
	 * Enregistre les textures
	 * Depuis la 1.5 on est obligé de charger les texture fichier par fichier
	 */
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		((IItemHelper)this.parent).setIcon (this.loadTexture(iconRegister));
	}
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	@Override
	public void setIcon (IIcon icon) {
		ModGollumCoreLib.log.warning("setIcon don't be call by helper. It's stub");
	}
}