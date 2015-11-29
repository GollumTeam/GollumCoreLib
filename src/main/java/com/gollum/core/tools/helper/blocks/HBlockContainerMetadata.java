package com.gollum.core.tools.helper.blocks;

import static com.gollum.core.tools.helper.blocks.HBlockMetadata.METADATA;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.gollum.core.tools.helper.BlockMetadataHelper;
import com.gollum.core.tools.helper.IBlockMetadataHelper;
import com.gollum.core.tools.helper.items.HItemBlockMetadata;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public abstract class HBlockContainerMetadata extends HBlockContainer implements IBlockMetadataHelper {
	
	private BlockMetadataHelper helperMetadata;

	public HBlockContainerMetadata(String registerName, Material material) {
		super(registerName, material);
		this.helperMetadata = new BlockMetadataHelper(this, registerName);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	public HBlockContainerMetadata(String registerName, Material material, int listSubBlock[]) {
		super(registerName, material);
		this.helperMetadata = new BlockMetadataHelper(this, registerName, listSubBlock);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	public HBlockContainerMetadata(String registerName, Material material, int numberSubBlock) {
		super(registerName, material);
		this.helperMetadata = new BlockMetadataHelper(this, registerName, numberSubBlock);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	// TODO factorisé
	protected List<IProperty> getStateProperties() {
		ArrayList<IProperty> prop= new ArrayList<IProperty>();
		prop.add(METADATA);
		return prop;
	}
	
	@Override // TODO factorisé
	protected BlockState createBlockState() {
		List<IProperty> list = this.getStateProperties();
		IProperty table[] = new IProperty[list.size()];
		for (int i = 0; i < list.size(); i++) {
			table[i] = list.get(i);
		}
		return new BlockState(this, table);
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
	public void getSubBlocks(Item item, CreativeTabs ctabs, List list) {
		this.helperMetadata.getSubBlocks(item, ctabs, list);
	}
	
	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	/* TODO
	@Override
	public int damageDropped(int damage) {
		return (helper.vanillaDamageDropped) ? super.damageDropped(damage) : this.helperMetadata.damageDropped (damage);
	}
	*/
	
	/**
	 * Called when a user uses the creative pick block button on this block
	 * 
	 * @param target
	 *            The full target the player is looking at
	 * @return A ItemStack to add to the player's inventory, Null if nothing
	 *         should be added.
	 */
	/* TODO
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return (helper.vanillaPicked) ? super.getPickBlock(target, world, x, y, z) : helperMetadata.getPickBlock(super.getPickBlock(null, world, x, y, z), world, x, y, z);
	}
	*/
	
	/**
	 * Liste des metadata enabled pour le subtype
	 */
	@Override
	public TreeSet<Integer> listSubEnabled () {
		return ((IBlockMetadataHelper) this.helper).listSubEnabled();
	}
	
	public int getEnabledMetadata (int dammage) {
		return this.helperMetadata.getEnabledMetadata(dammage);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(METADATA, Integer.valueOf(this.getEnabledMetadata(meta)));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((Integer)state.getValue(METADATA)).intValue();
	}
	
}
