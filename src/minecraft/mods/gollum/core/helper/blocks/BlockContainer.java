package mods.gollum.core.helper.blocks;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.helper.BlockHelper;
import mods.gollum.core.helper.IBlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

public abstract class BlockContainer extends net.minecraft.block.BlockContainer implements IBlockHelper {

	protected BlockHelper helper;
	
	public BlockContainer (int id, String registerName, Material material)  {
		super(id, material);
		ModGollumCoreLib.log.info ("Create block id : " + id + " registerName : " + registerName);
		this.helper = new BlockHelper(this, registerName);
	}
	
	public BlockHelper getGollumHelper () {
		return helper;
	}
	
	/**
	 * Nom d'enregistrement du mod
	 */
	@Override
	public String getRegisterName() {
		return helper.getRegisterName();
	}
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		if (helper.naturalTexture) super.registerIcons(iconRegister); else helper.registerIcons(iconRegister);
	}
	
	@Override
	public Icon getIcon(int par1, int par2) {
		return (helper.naturalTexture) ? super.getIcon(par1, par2) : helper.getIcon(par1, par2);
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
