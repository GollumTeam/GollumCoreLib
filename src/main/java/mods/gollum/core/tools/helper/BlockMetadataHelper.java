package mods.gollum.core.tools.helper;

import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.Icon;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockMetadataHelper extends BlockHelper implements IBlockMetadataHelper {

	protected TreeSet<Integer>       listSubEnabled = new TreeSet<Integer>();
	protected TreeMap<Integer, IIcon> blockIcons     = new TreeMap<Integer, IIcon>();
	
	public BlockMetadataHelper(Block parent, String registerName) {
		super(parent, registerName);
		
		for (int metadata = 0; metadata < 16; metadata++) {
			this.listSubEnabled.add (metadata);
		}
	}
	
	public BlockMetadataHelper(Block parent, String registerName, int listSubBlock[]) {
		this (parent, registerName);
		
		for (int metadata : listSubBlock) {
			this.listSubEnabled.add (metadata);
		}
	}
	
	public BlockMetadataHelper(Block parent, String registerName, int numberSubBlock) {
		this (parent, registerName);
		
		for (int metadata = 0; metadata < numberSubBlock; metadata++) {
			this.listSubEnabled.add (metadata);
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
	@Override
	public int damageDropped(int dammage) {
		return this.getEnabledMetadata(dammage);
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
		
		ItemStack is = this.parent.getPickBlock(null, world, x, y, z);
		
		if (is == null) {
			return null;
		}
		
		int dammage = this.parent.getDamageValue(world, x, y, z);
		
		return new ItemStack(is.getItem(), 1, this.getEnabledMetadata (dammage));
	}
	
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
	
	/**
	 * Enregistre les textures
	 * Depuis la 1.5 on est obligé de charger les texture fichier par fichier
	 */
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		for (int metadata : this.listSubEnabled) {
			this.blockIcons.put(metadata, this.loadTexture(iconRegister, "_"+metadata));
		}
	}
	
	@Override
	public IIcon getIcon(int side, int metadata) {
		
		int subBlock = this.getEnabledMetadata(metadata);
		if (this.blockIcons.containsKey(subBlock)) {
			return this.blockIcons.get(subBlock);
		}
		return null;
	}
	
}