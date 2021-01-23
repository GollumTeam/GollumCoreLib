package com.gollum.core.tools.helper.blocks;

import java.util.List;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.tools.helper.BlockHelper;
import com.gollum.core.tools.helper.BlockHelper.BoundsList;
import com.gollum.core.tools.helper.IBlockHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class HBlockPistonBase extends BlockPistonBase implements IBlockHelper {
	
	protected BlockHelper helper;
	
	protected boolean isStickyPiston;
	
	protected IIcon iconTop;
	protected IIcon iconOpen;
	protected IIcon iconBottom;
	
	protected String suffixTop    = "_top";
	protected String suffixSticky = "_sticky";
	protected String suffixOpen   = "_open";
	protected String suffixBottom  = "_bottom";
	protected String suffixSide   = "_side";
	
	public HBlockPistonBase(String registerName, boolean isSticky)  {
		super(isSticky);
		ModGollumCoreLib.log.info ("Create block registerName : " + registerName);
		this.helper = new BlockHelper(this, registerName);
		
		this.isStickyPiston = isSticky;
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
	public void breakBlockInventory(World world, int x, int y, int z, Block oldBlock) {
		helper.breakBlockInventory(world, x, y, z, oldBlock);
	}
	
	///////////////
	// Collision //
	///////////////
	
	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
	 * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
	 */
	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
		super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
	}
	
	/**
	 * Original addCollisionBoxesToList method
	 */
	@Override
	public void baseAddCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
		super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
	}
	
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
		super.setBlockBoundsBasedOnState(blockAccess, x, y, z);
	}
	
	/**
	 * Helper to define collision box
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param list
	 */
	@Override
	public void defineCollisionBox(IBlockAccess world, int x, int y, int z, BoundsList list) {
	}
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	/**
	 * Enregistre les textures
	 * Depuis la 1.5 on est obligé de charger les texture fichier par fichier
	 */
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		
		if (helper.vanillaTexture) {
			super.registerBlockIcons(iconRegister);
			return;
		};
		
		this.registerBlockIconsTop   (iconRegister);
		this.registerBlockIconsOpen  (iconRegister);
		this.registerBlockIconsBottom(iconRegister);
		this.registerBlockIconsSide  (iconRegister);
		
	}
	protected void registerBlockIconsTop(IIconRegister iconRegister) {
		this.iconTop = helper.loadTexture(iconRegister, suffixTop + (this.isStickyPiston ? suffixSticky : ""));
	}
	protected void registerBlockIconsOpen(IIconRegister iconRegister) {
		this.iconOpen = helper.loadTexture(iconRegister, suffixOpen);
	}
	protected void registerBlockIconsBottom(IIconRegister iconRegister) {
		this.iconBottom = helper.loadTexture(iconRegister, suffixBottom);
	}
	protected void registerBlockIconsSide(IIconRegister iconRegister) {
		this.blockIcon = helper.loadTexture(iconRegister, suffixSide);
	}
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	@Override
	public IBlockHelper setIcon (IIcon icon) {
		this.blockIcon = icon;
		return this;
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		
		if (helper.vanillaTexture) return super.getIcon(side, metadata);
		
		int orientation = getPistonOrientation(metadata);
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
	public IIcon getPistonExtensionTexture() {
		if (helper.vanillaTexture) return super.getPistonExtensionTexture();
		return this.iconTop;
	}
	
}
