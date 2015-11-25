package com.gollum.core.tools.helper.blocks;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.tools.helper.BlockHelper;
import com.gollum.core.tools.helper.IBlockHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HBlockPistonBase extends BlockPistonBase implements IBlockHelper {
	
	protected BlockHelper helper;
	
	protected boolean isStickyPiston;

	/* TODO
	protected IIcon iconTop;
	protected IIcon iconOpen;
	protected IIcon iconBottom;
	*/
	
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
	/* TODO
	public void breakBlockInventory(World world, int x, int y, int z, Block oldBlock) {
		helper.breakBlockInventory(world, x, y, z, oldBlock);
	}
	*/
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	/**
	 * Enregistre les textures
	 * Depuis la 1.5 on est obligé de charger les texture fichier par fichier
	 */
	/* TODO
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
	*/
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	/* TODO
	@Override
	public IBlockHelper setIcon (IIcon icon) {
		this.blockIcon = icon;
		return this;
	}
	*/

	/* TODO
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
	*/
	
	@Override
	public String getTextureKey() {
		return helper.getTextureKey();
	}

	/* TODO
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getPistonExtensionTexture() {
		if (helper.vanillaTexture) return super.getPistonExtensionTexture();
		return this.iconTop;
	}
	*/
	
}
