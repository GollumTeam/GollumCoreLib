package mods.gollum.core.helper.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.helper.logic.BlockLogic;
import mods.gollum.core.helper.logic.IBlockLogic;

public class BlockPistonExtension extends net.minecraft.block.BlockPistonExtension implements IBlockLogic {

	protected BlockLogic logic;
	
	public BlockPistonExtension (int id, String registerName)  {
		super(id);
		ModGollumCoreLib.log.info ("Create block id : " + id + " registerName : " + registerName);
		this.logic = new BlockLogic(this, registerName);
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
	
	@Override
	public Icon getIcon(int par1, int par2) {
		return (logic.naturalTexture) ? super.getIcon(par1, par2) : logic.getIcon(par1, par2);
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
