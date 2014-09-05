package mods.gollum.core.tools.helper;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemMetadataHelper extends ItemHelper implements IItemMetadataHelper {
	
	private boolean listSubEnabled[] = new boolean[16];
	
	public ItemMetadataHelper(Item parent, String registerName) {
		super(parent, registerName);
		
		for (int i = 0; i < 16; i++) {
			this.listSubEnabled[i] = true;
		}
	}
	
	public ItemMetadataHelper(Item parent, String registerName, int listSubBlock[]) {
		this (parent, registerName);
		
		for (int i = 0; i < 16; i++) {
			this.listSubEnabled[i] = false;
		}
		for (int metadata : listSubBlock) {
			this.listSubEnabled[metadata] = true;
		}
	}
	
	public ItemMetadataHelper(Item parent, String registerName, int numberSubBlock) {
		this (parent, registerName);
		
		for (int i = 0; i < numberSubBlock; i++) {
			this.listSubEnabled[i] = true;
		}
		for (int i = numberSubBlock; i < 16; i++) {
			this.listSubEnabled[i] = false;
		}
	}
//	
//	/**
//	 * returns a list of blocks with the same ID, but different meta (eg: wood
//	 * returns 4 blocks)
//	 */
//	public void getSubBlocks(int id, CreativeTabs ctabs, List list) {
//		for (int i = 0; i < 16; i++) {
//			if (this.listSubEnabled[i]) {
//				list.add(new ItemStack(id, 1, i));
//			}
//		}
//	}
//	
//	public int getEnabledMetadata (int dammage) {
//		
//		int lastSubblock = -1;
//		int i = 0;
//		for (boolean isSub : this.listSubEnabled) {
//			if (i > dammage) {
//				break;
//			}
//			if (isSub) {
//				lastSubblock = i;
//			}
//			i++;
//		}
//		
//		return (lastSubblock == -1) ? dammage : lastSubblock;
//	}
//	
//	/**
//	 * Determines the damage on the item the block drops. Used in cloth and
//	 * wood.
//	 */
//	@Override
//	public int damageDropped(int dammage) {
//		return this.getEnabledMetadata(dammage);
//	}
//	
//	/**
//	 * Called when a user uses the creative pick block button on this block
//	 * 
//	 * @param target
//	 *            The full target the player is looking at
//	 * @return A ItemStack to add to the player's inventory, Null if nothing
//	 *         should be added.
//	 */
//	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
//		
//		int id = this.parent.idPicked(world, x, y, z);
//		
//		if (id == 0) {
//			return null;
//		}
//		
//		Item item = Item.itemsList[id];
//		if (item == null) {
//			return null;
//		}
//		
//		int dammage = this.parent.getDamageValue(world, x, y, z);
//		
//		return new ItemStack(id, 1, this.getEnabledMetadata (dammage));
//	}
//	
//	/**
//	 * Liste des metadata enabled pour le subtype
//	 */
//	public boolean[] listSubEnabled () {
//		return this.listSubEnabled;
//	}
}