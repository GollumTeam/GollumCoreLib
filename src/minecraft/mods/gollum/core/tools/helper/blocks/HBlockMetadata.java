package mods.gollum.core.tools.helper.blocks;

import java.util.List;
import java.util.TreeSet;

import mods.gollum.core.tools.helper.BlockMetadataHelper;
import mods.gollum.core.tools.helper.IBlockMetadataHelper;
import mods.gollum.core.tools.helper.items.HItemBlockMetadata;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class HBlockMetadata extends HBlock implements IBlockMetadataHelper {

	/////////////////
	// Contructeur //
	/////////////////
	
	public HBlockMetadata(int id, String registerName, Material material, Class itemClass) {
		super(id, registerName, material);
		this.helper = new BlockMetadataHelper(this, registerName);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	public HBlockMetadata(int id, String registerName, Material material, int listSubBlock[]) {
		super(id, registerName, material);
		this.helper = new BlockMetadataHelper(this, registerName, listSubBlock);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	public HBlockMetadata(int id, String registerName, Material material, int numberSubBlock) {
		super(id, registerName, material);
		this.helper = new BlockMetadataHelper(this, registerName, numberSubBlock);
		this.setItemBlockClass(HItemBlockMetadata.class);
	}
	
	////////////
	// Helper //
	////////////
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	public void getSubBlocks(int id, CreativeTabs ctabs, List list) {
		((IBlockMetadataHelper) this.helper).getSubBlocks(id, ctabs, list);
	}
	
	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	@Override
	public int damageDropped(int damage) {
		return (helper.vanillaDamageDropped) ? super.damageDropped(damage) : ((IBlockMetadataHelper) this.helper).damageDropped (damage);
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
		return (helper.vanillaPicked) ? super.getPickBlock(target, world, x, y, z) : ((IBlockMetadataHelper) helper).getPickBlock(target, world, x, y, z);
	}
	
	/**
	 * Liste des metadata enabled pour le subtype
	 */
	public TreeSet<Integer> listSubEnabled () {
		return ((IBlockMetadataHelper) this.helper).listSubEnabled();
	}
	
	public int getEnabledMetadata (int dammage) {
		return ((IBlockMetadataHelper) this.helper).getEnabledMetadata(dammage);
	}
	
	@Override
	public Icon getIcon(int side, int metadata) {
		return  (helper.vanillaTexture) ? super.getIcon(side, metadata) : ((IBlockMetadataHelper) this.helper).getIcon(side, metadata);
	}
}
