package mods.gollum.core.tools.helper;

import java.util.List;
import java.util.TreeSet;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public interface IBlockMetadataHelper {
	
	public void getSubBlocks(int id, CreativeTabs ctabs, List list);
	
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
	
	public void registerIcons(IconRegister iconRegister);
	
	public Icon getIcon(int side, int metadata);
	
}