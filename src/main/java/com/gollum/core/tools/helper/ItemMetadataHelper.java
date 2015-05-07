package com.gollum.core.tools.helper;

import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemMetadataHelper extends ItemHelper implements IItemMetadataHelper {
	
	protected TreeSet<Integer>        listSubEnabled = new TreeSet<Integer>();
	protected TreeMap<Integer, IIcon> blockIcons     = new TreeMap<Integer, IIcon>();

	/////////////////
	// Contructeur //
	/////////////////
	
	public ItemMetadataHelper(Item parent, String registerName, int listSubBlock[]) {
		super (parent, registerName);
		
		for (int metadata : listSubBlock) {
			this.listSubEnabled.add (metadata);
		}
	}
	
	public ItemMetadataHelper(Item parent, String registerName, int numberSubBlock) {
		super (parent, registerName);
		
		for (int metadata = 0; metadata < numberSubBlock; metadata++) {
			this.listSubEnabled.add (metadata);
		}
	}
	
	////////////
	// Helper //
	////////////
	
	/**
	 * Returns the unlocalized name of this item. This version accepts an
	 * ItemStack so different stacks can have different names based on their
	 * damage or NBT.
	 */
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int subBlock = this.getEnabledMetadata(stack.getItemDamage());
		return this.parent.getUnlocalizedName() + "." + subBlock;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs ctabs, List list) {
		for (int metadata : this.listSubEnabled) {
			list.add(new ItemStack(item, 1, metadata));
		}
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
	public void registerIcons(IIconRegister iconRegister) {
		for (int metadata : this.listSubEnabled) {
			this.blockIcons.put(metadata, this.loadTexture(iconRegister, "_"+metadata));
		}
	}
	
	@Override
	public IIcon getIconFromDamage(int metadata) {
		
		int subBlock = this.getEnabledMetadata(metadata);
		if (this.blockIcons.containsKey(subBlock)) {
			return this.blockIcons.get(subBlock);
		}
		return null;
	}
}