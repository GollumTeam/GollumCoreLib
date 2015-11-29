package com.gollum.core.tools.helper;

import static com.gollum.core.tools.helper.blocks.HBlockMetadata.METADATA;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.TreeSet;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockMetadataHelper extends BlockHelper implements IBlockMetadataHelper {
	
	protected TreeSet<Integer> listSubEnabled = new TreeSet<Integer>();
	
	public BlockMetadataHelper(Block parent, String registerName) {
		super(parent, registerName);
		
		this.listSubEnabled.clear();
		for (int metadata = 0; metadata < 16; metadata++) {
			this.listSubEnabled.add (metadata);
		}
		this.setDefaultState(this.parent.getBlockState().getBaseState().withProperty(METADATA, 0));
	}
	
	public BlockMetadataHelper(Block parent, String registerName, int listSubBlock[]) {
		this (parent, registerName);
		
		this.listSubEnabled.clear();
		for (int metadata : listSubBlock) {
			this.listSubEnabled.add (metadata);
		}
	}
	
	public BlockMetadataHelper(Block parent, String registerName, int numberSubBlock) {
		this (parent, registerName);
		
		this.listSubEnabled.clear();
		for (int metadata = 0; metadata < numberSubBlock; metadata++) {
			this.listSubEnabled.add (metadata);
		}
	}
	
	protected void setDefaultState (IBlockState state) {
		
		try {
			Method method = null;
			for (Method m: Block.class.getDeclaredMethods()) {
				m.setAccessible(true);
				if (
					m.getReturnType() == void.class &&
					m.getGenericParameterTypes().length == 1 &&
					m.getGenericParameterTypes()[0].equals(IBlockState.class) &&
					(m.getModifiers() & Modifier.FINAL) == Modifier.FINAL
				) {
					method = m;
					break;
				}
			}
			method.invoke(this.parent, this.parent.getDefaultState().withProperty(METADATA, 0));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public BlockMetadataHelper getGollumHelperMetadata () {
		return this;
	}
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@Override
	public void getSubBlocks(Item item, CreativeTabs ctabs, List list) {
		for (int metadata : this.listSubEnabled) {
			list.add(new ItemStack(item, 1, metadata));
		}
	}
	
	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	/* TODO
	@Override
	public int damageDropped(int dammage) {
		return this.getEnabledMetadata(dammage);
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
		return this.parent.getPickBlock(target, world, x, y, z);
	}
	*/
	
	/* TODO
	public ItemStack getPickBlock(ItemStack is, World world, int x, int y, int z) {
		
		if (is == null) {
			return null;
		}
		
		int dammage = this.parent.getDamageValue(world, x, y, z);
		
		return new ItemStack(is.getItem(), 1, this.getEnabledMetadata (dammage));
	}
	*/
	
	/**
	 * Liste des metadata enabled pour le subtype
	 */
	@Override
	public TreeSet<Integer> listSubEnabled () {
		return this.listSubEnabled;
	}
	
	@Override
	public int getEnabledMetadata (int dammage) {
		
		int lastSubblock = -1;
		for (Integer metadata : this.listSubEnabled) {
			if (metadata  > dammage) {
				break;
			}
			lastSubblock = metadata;			
		}
		
		return (lastSubblock == -1) ? dammage : lastSubblock;
	}
	
}