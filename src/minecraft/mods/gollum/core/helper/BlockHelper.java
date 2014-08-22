package mods.gollum.core.helper;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.context.ModContext;
import mods.gollum.core.mod.GollumMod;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockHelper implements IBlockHelper {
	
	// Pour chaque element natural. Utilise le fonctionnement naturel mais pas des helper
	// Une sorte de config
	// Par defaut le helper vas enregistrer le block, charger des texture perso ...
	public static boolean vanillaRegister = false;
	public boolean vanillaTexture = false;
	
	private GollumMod mod;
	private Block parent;
	private String registerName;

	public BlockHelper (Block parent, String registerName) {
		this.parent       = parent;
		this.registerName = registerName;
		this.mod          = ModContext.instance().getCurrent();
		
		if (!vanillaRegister) this.register();
	}

	@Override
	public BlockHelper getGollumHelper() {
		return this;
	}
	
	private void register () {
		this.parent.setUnlocalizedName(this.registerName);
		GameRegistry.registerBlock(this.parent, this.registerName);
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
		return ((IBlockHelper)this.parent).getRegisterName().toLowerCase();
	}
	
	/**
	* Charge une texture et affiche dans le log.
	* Utilise le register name comme prefixe sauf si useTextureKey est à false
	*
	* @param iconRegister
	* @param key
	* @return
	*/
	public Icon loadTexture(IconRegister iconRegister) {
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
	public Icon loadTexture(IconRegister iconRegister, String sufixe) {
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
	public Icon loadTexture(IconRegister iconRegister, String sufixe, boolean dontUseTextureKey) {
		
		String key = (dontUseTextureKey ?  "" : ((IBlockHelper)this.parent).getTextureKey ())+sufixe;
		String texture = this.mod.getModId().toLowerCase() + ":" + key;
		
		ModGollumCoreLib.log.debug ("Register icon " + texture + "\"");
		return iconRegister.registerIcon(texture);
	}
	
	/**
	 * Enregistre les textures
	 * Depuis la 1.5 on est obligé de charger les texture fichier par fichier
	 */
	@Override
	public void registerIcons(IconRegister iconRegister) {
		((IBlockHelper)this.parent).setIcon (this.loadTexture(iconRegister));
	}
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	@Override
	public IBlockHelper setIcon (Icon icon) {
		ModGollumCoreLib.log.warning("setIcon don't be call by helper. It's stub");
		return this;
	}
}