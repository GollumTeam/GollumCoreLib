package com.gollum.core.tools.helper.blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.tools.helper.BlockHelper;
import com.gollum.core.tools.helper.IBlockHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
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
	
	/**
	 * Enregistrement du rendu du bloc. Appelé a la fin de l'Init
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender () {
		helper.registerRender();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubNames(HashMap<Integer, String> list) {
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs ctabs, List list) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		this.getSubNames(map);
		for (Entry<Integer, String> entry: map.entrySet()) {
			list.add(new ItemStack(item, 1, entry.getKey()));
		}
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
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		helper.breakBlock(world, pos, state);
		super.breakBlock(world, pos, state);
	}
	
	
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
	@SideOnly(Side.CLIENT)
	public IIcon getPistonExtensionTexture() {
		if (helper.vanillaTexture) return super.getPistonExtensionTexture();
		return this.iconTop;
	}
	*/
	
}
