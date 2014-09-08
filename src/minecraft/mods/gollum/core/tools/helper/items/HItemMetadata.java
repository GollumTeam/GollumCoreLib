package mods.gollum.core.tools.helper.items;

import java.util.List;
import java.util.TreeSet;

import mods.gollum.core.tools.helper.IItemMetadataHelper;
import mods.gollum.core.tools.helper.ItemHelper;
import mods.gollum.core.tools.helper.ItemMetadataHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HItemMetadata extends HItem implements IItemMetadataHelper  {

	protected ItemHelper helper;
	
	/////////////////
	// Contructeur //
	/////////////////
	
	public HItemMetadata(int id, String registerName, int listSubBlock[]) {
		super(id, registerName);
		this.helper = new ItemMetadataHelper(this, registerName, listSubBlock);
	}
	
	public HItemMetadata(int id, String registerName, int numberSubBlock) {
		super(id, registerName);
		this.helper = new ItemMetadataHelper(this, registerName, numberSubBlock);
	}
	
	////////////
	// Helper //
	////////////

	@Override
	public int getMetadata(int metadata) {
		return metadata;
	}
	
	/**
	 * Returns the unlocalized name of this item. This version accepts an
	 * ItemStack so different stacks can have different names based on their
	 * damage or NBT.
	 */
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return ((IItemMetadataHelper) this.helper).getUnlocalizedName(stack);
	}
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs ctabs, List list) {
		((IItemMetadataHelper) this.helper).getSubItems(id, ctabs, list);
	}
	
	/**
	 * Liste des metadata enabled pour le subtype
	 */
	@Override
	public TreeSet<Integer> listSubEnabled () {
		return ((IItemMetadataHelper) this.helper).listSubEnabled();
	}
	
	@Override
	public int getEnabledMetadata (int dammage) {
		return ((IItemMetadataHelper) this.helper).getEnabledMetadata(dammage);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage (int metadata) {
		return  (helper.vanillaTexture) ? super.getIconFromDamage(metadata) : ((IItemMetadataHelper) this.helper).getIconFromDamage (metadata);
	}
	
}
