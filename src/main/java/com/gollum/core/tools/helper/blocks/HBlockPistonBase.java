package com.gollum.core.tools.helper.blocks;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.tools.helper.BlockHelper;
import com.gollum.core.tools.helper.IBlockHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class HBlockPistonBase extends BlockPistonBase implements IBlockHelper {
	
	protected BlockHelper helper;
	
	protected boolean isStickyPiston;
	
	protected Icon iconTop;
	protected Icon iconOpen;
	protected Icon iconBottom;
	
	protected String suffixTop    = "_top";
	protected String suffixSticky = "_sticky";
	protected String suffixOpen   = "_open";
	protected String suffixBottom  = "_bottom";
	protected String suffixSide   = "_side";
	
	public HBlockPistonBase(int id, String registerName, boolean isStickyPiston)  {
		super(id, isStickyPiston);
		ModGollumCoreLib.log.info ("Create block registerName : " + registerName);
		this.helper = new BlockHelper(this, registerName);
		
		this.isStickyPiston = isStickyPiston;
	}
	
	public BlockHelper getGollumHelper () {
		return helper;
	}
	
	/**
	 * Enregistrement du block. Appelé a la fin du postInit
	 */
	public void register () {
		helper.register();
	}
	
	@Override
	public String getRegisterName() {
		return helper.getRegisterName();
	}
	
	/**
	 * Affect la class de l'objet qui servira item pour le block
	 * par default ItemBlock
	 * @param itemClass
	 */
	@Override
	public Block setItemBlockClass (Class<? extends ItemBlock> itemClass) {
		return helper.setItemBlockClass(itemClass);
	}
	
	/**
	 * Renvoie l'item en relation avec le block
	 */
	@Override
	public Item getBlockItem () {
		return helper.getBlockItem();
	}
	
	/**
	 * Libère les items de l'inventory
	 */
	public void breakBlockInventory(World world, int x, int y, int z, int oldBlockId) {
		helper.breakBlockInventory(world, x, y, z, oldBlockId);
	}
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	/**
	 * Enregistre les textures
	 * Depuis la 1.5 on est obligé de charger les texture fichier par fichier
	 */
	@Override
	public void registerIcons(IconRegister iconRegister) {
		
		if (helper.vanillaTexture) {
			super.registerIcons(iconRegister);
			return;
		};
		
		this.registerBlockIconsTop   (iconRegister);
		this.registerBlockIconsOpen  (iconRegister);
		this.registerBlockIconsBottom(iconRegister);
		this.registerBlockIconsSide  (iconRegister);
		
	}
	protected void registerBlockIconsTop(IconRegister iconRegister) {
		this.iconTop = helper.loadTexture(iconRegister, suffixTop + (this.isStickyPiston ? suffixSticky : ""));
	}
	protected void registerBlockIconsOpen(IconRegister iconRegister) {
		this.iconOpen = helper.loadTexture(iconRegister, suffixOpen);
	}
	protected void registerBlockIconsBottom(IconRegister iconRegister) {
		this.iconBottom = helper.loadTexture(iconRegister, suffixBottom);
	}
	protected void registerBlockIconsSide(IconRegister iconRegister) {
		this.blockIcon = helper.loadTexture(iconRegister, suffixSide);
	}
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	@Override
	public IBlockHelper setIcon (Icon icon) {
		this.blockIcon = icon;
		return this;
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
		
		if (helper.vanillaTexture) return super.getIcon(side, metadata);
		
		int orientation = getOrientation(metadata);
		if (orientation > 5) {
			return this.iconTop;
		}
		if (side == orientation) {
			if (
				(isExtended(metadata)) ||
				(this.minX > 0.0D) || (this.minY > 0.0D) || (this.minZ > 0.0D) ||
				(this.maxX < 1.0D) || (this.maxY < 1.0D) || (this.maxZ < 1.0D)
			) {
				return this.iconOpen;
			}
			
			return this.iconTop;
		}
		
		return side != Facing.oppositeSide[orientation] ? this.blockIcon : this.iconBottom;
	}
	
	@Override
	public String getTextureKey() {
		return helper.getTextureKey();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getPistonExtensionTexture() {
		if (helper.vanillaTexture) return super.getPistonExtensionTexture();
		return this.iconTop;
	}
	
}
