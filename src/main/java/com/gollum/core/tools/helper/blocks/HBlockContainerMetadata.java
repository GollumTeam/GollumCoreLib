package com.gollum.core.tools.helper.blocks;

import java.util.List;
import java.util.TreeSet;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.gollum.core.tools.helper.BlockMetadataHelper;
import com.gollum.core.tools.helper.IBlockMetadataHelper;
import com.gollum.core.tools.helper.items.HItemBlockMetadata;

public abstract class HBlockContainerMetadata extends HBlockContainer implements IBlockMetadataHelper {

	private BlockMetadataHelper helperMetadata;

	public HBlockContainerMetadata(int id, String registerName, Material material) {
		super(id, registerName, material);
		this.helperMetadata = new BlockMetadataHelper(this, registerName);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	public HBlockContainerMetadata(int id, String registerName, Material material, int listSubBlock[]) {
		super(id, registerName, material);
		this.helperMetadata = new BlockMetadataHelper(this, registerName, listSubBlock);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	public HBlockContainerMetadata(int id, String registerName, Material material, int numberSubBlock) {
		super(id, registerName, material);
		this.helperMetadata = new BlockMetadataHelper(this, registerName, numberSubBlock);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	@Override
	public BlockMetadataHelper getGollumHelperMetadata () {
		return helperMetadata;
	}
	
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@Override
	public void getSubBlocks(int id, CreativeTabs ctabs, List list) {
		this.helperMetadata.getSubBlocks(id, ctabs, list);
	}
	
	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	@Override
	public int damageDropped(int damage) {
		return (helper.vanillaDamageDropped) ? super.damageDropped(damage) : this.helperMetadata.damageDropped (damage);
	}
	
	/**
	 * Called when a user uses the creative pick block button on this block
	 * 
	 * @param target
	 *            The full target the player is looking at
	 * @return A ItemStack to add to the player's inventory, Null if nothing
	 *         should be added.
	 */
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return (helper.vanillaPicked) ? super.getPickBlock(target, world, x, y, z) : helperMetadata.getPickBlock(super.getPickBlock(null, world, x, y, z), world, x, y, z);
	}

	/**
	 * Liste des metadata enabled pour le subtype
	 */
	public TreeSet<Integer> listSubEnabled () {
		return this.helperMetadata.listSubEnabled();
	}
	
	public int getEnabledMetadata (int dammage) {
		return this.helperMetadata.getEnabledMetadata(dammage);
	}

	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		if (helper.vanillaTexture) super.registerIcons(iconRegister); else helperMetadata.registerIcons(iconRegister);
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
		return (helper.vanillaTexture) ? super.getIcon(side, metadata) : this.helperMetadata.getIcon(side, metadata);
	}
}
