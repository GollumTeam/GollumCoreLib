package mods.gollum.core.tools.helper;

import java.util.List;
import java.util.TreeSet;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public interface IBlockMetadataHelper {
	
	public BlockMetadataHelper getGollumHelperMetadata();
	
	public void getSubBlocks(Item item, CreativeTabs ctabs, List list);
	
	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	public int damageDropped(int dommage);

	/**
	 * Called when a user uses the creative pick block button on this block
	 * 
	 * @param target
	 *            The full target the player is looking at
	 * @return A ItemStack to add to the player's inventory, Null if nothing
	 *         should be added.
	 */
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z);
	
	/**
	 * Liste des metadata enabled pour le subtype
	 */
	public TreeSet<Integer> listSubEnabled ();
	
	public int getEnabledMetadata (int dammage);
	
	public void registerBlockIcons(IIconRegister iconRegister);
	
	public IIcon getIcon(int side, int metadata);
	
}