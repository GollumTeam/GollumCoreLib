package mods.gollum.core.helper.logic;

import javax.naming.Context;

import mods.gollum.core.context.ModContext;
import mods.gollum.core.mod.GollumMod;

public class BlockLogic implements IBlockLogic {
	//////////////////////////
	//Gestion des textures //
	//////////////////////////
//	/**
//	* Charge une texture et affiche dans le log
//	*
//	* @param iconRegister
//	* @param key
//	* @return
//	*/
//	public IIcon loadTexture(IIconRegister iconRegister, String key) {
//		ModContext.current.log.debug ("Register icon :\"" + key + "\"");
//		return iconRegister.registerIcon(key);
//	}
//	/**
//	* Enregistre les textures
//	* Depuis la 1.5 on est obligé de charger les texture fichier par fichier
//	*/
//	@Override
//	public void registerBlockIcons(IIconRegister iconRegister) {
//	this.textureFileTop = this.loadTexture(iconRegister, ModMorePistons.PATH_TEXTURES + "top");
//	this.textureFileTopSticky = this.loadTexture(iconRegister, ModMorePistons.PATH_TEXTURES + "top_sticky");
//	this.textureFileOpen = this.loadTexture(iconRegister, ModMorePistons.PATH_TEXTURES + this.texturePrefixe + "top");
//	this.textureFileBottom = this.loadTexture(iconRegister, ModMorePistons.PATH_TEXTURES + this.texturePrefixe + "bottom");
//	this.textureFileSide = this.loadTexture(iconRegister, ModMorePistons.PATH_TEXTURES + this.texturePrefixe + "side");
//	}
//	@Override
//	@SideOnly(Side.CLIENT)
//	public IIcon getPistonExtensionTexture() {
//	return this.isSticky ? this.textureFileTopSticky : this.textureFileTop;
//	}
//	@Override
//	public IIcon getIcon(int i, int j) {
//	int k = getPistonOrientation(j);
//	if (k > 5) {
//	return this.textureFileTopSticky;
//	}
//	if (i == k) {
//	if (
//	(isExtended(j)) ||
//	(this.minX > 0.0D) || (this.minY > 0.0D) || (this.minZ > 0.0D) ||
//	(this.maxX < 1.0D) || (this.maxY < 1.0D) || (this.maxZ < 1.0D)
//	) {
//	return this.textureFileOpen;
//	}
//	return this.isSticky ? this.textureFileTopSticky : this.textureFileTop;
//	}
//	return i != Facing.oppositeSide[k] ? this.textureFileSide : this.textureFileBottom;
//	}
//	}
//
//	class User 
//	    extends OtherClass 
//	    implements BusinessLogicInterface
//	{
//	    BusinessLogic logic = new BusinessLogic();
//
//	    @Override
//	    void method1() { logic.method1(); }
//
//	    @Override
//	    void method2() { logic.method2(); }
	}