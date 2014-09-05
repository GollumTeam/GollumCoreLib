package mods.gollum.core.tools.helper.items;

import java.util.List;

import mods.gollum.core.tools.helper.IBlockMetadataHelper;
import mods.gollum.core.tools.helper.IItemMetadataHelper;
import mods.gollum.core.tools.helper.ItemHelper;
import mods.gollum.core.tools.helper.ItemMetadataHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HItemMetadata extends HItem implements IItemMetadataHelper  {

	protected ItemHelper helper;
	
	public HItemMetadata(int id, String registerName) {
		super(id, registerName);
		this.helper = new ItemMetadataHelper(this, registerName);
	}
	
	public HItemMetadata(int id, String registerName, int listSubBlock[]) {
		super(id, registerName);
		this.helper = new ItemMetadataHelper(this, registerName, listSubBlock);
	}
	
	public HItemMetadata(int id, String registerName, int numberSubBlock) {
		super(id, registerName);
		this.helper = new ItemMetadataHelper(this, registerName, numberSubBlock);
	}
	
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs ctabs, List list) {
		((IBlockMetadataHelper) this.helper).getSubBlocks(id, ctabs, list);
	}
	// TODO
//	@SideOnly(Side.CLIENT)
//	public Icon getIconFromDamage(int metadata) {
//		return metadata < type.length && metadata >= 0 ? IconArray[metadata] : IconArray[0];
//	}
}
