package mods.gollum.core.tools.helper.blocks;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.tools.helper.BlockHelper;
import mods.gollum.core.tools.helper.IBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HBlockPistonBase extends BlockPistonBase implements IBlockHelper {
	
	protected BlockHelper helper;
	
	protected boolean isSticky;
	
	protected Icon iconTop;
	protected Icon iconOpen;
	protected Icon iconBottom;
	protected Icon iconSide;

	protected String suffixTop    = "_top";
	protected String suffixSticky = "_sticky";
	protected String suffixOpen   = "_open";
	protected String suffixBotom  = "_bottom";
	protected String suffixSide   = "_side";
	
	public HBlockPistonBase(int id, String registerName, boolean isSticky)  {
		super(id, isSticky);
		ModGollumCoreLib.log.info ("Create block id : " + id + " registerName : " + registerName);
		this.helper = new BlockHelper(this, registerName);
		
		this.isSticky = isSticky;
	}
	
	public BlockHelper getGollumHelper () {
		return helper;
	}
	
	@Override
	public String getRegisterName() {
		return helper.getRegisterName();
	}
	
	/**
	 * Affect la class de l'objet qui servira item pour le block
	 * par default ItemBlock
	 * @param itemClass
	 */
	@Override
	public Block setItemBlockClass (Class<? extends ItemBlock> itemClass) {
		return helper.setItemBlockClass(itemClass);
	}
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	/**
	 * Enregistre les textures
	 * Depuis la 1.5 on est obligé de charger les texture fichier par fichier
	 */
	@Override
	public void registerIcons(IconRegister iconRegister) {
		
		if (helper.vanillaTexture) {
			super.registerIcons(iconRegister);
			return;
		};
		
		this.iconTop    = helper.loadTexture(iconRegister, suffixTop + (this.isSticky ? suffixSticky : ""));
		this.iconOpen   = helper.loadTexture(iconRegister, suffixOpen);
		this.iconBottom = helper.loadTexture(iconRegister, suffixBotom);
		this.iconSide   = helper.loadTexture(iconRegister, suffixSide);
	}
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	@Override
	public IBlockHelper setIcon (Icon icon) {
		this.blockIcon = icon;
		return this;
	}
	
	@Override
	public Icon getIcon(int i, int j) {
		
		if (helper.vanillaTexture) return super.getIcon(i, j);
		
		int k = getOrientation(j);
		if (k > 5) {
			return this.iconTop;
		}
		if (i == k) {
			if (
				(isExtended(j)) ||
				(this.minX > 0.0D) || (this.minY > 0.0D) || (this.minZ > 0.0D) ||
				(this.maxX < 1.0D) || (this.maxY < 1.0D) || (this.maxZ < 1.0D)
			) {
				return this.iconOpen;
			}
			
			return this.iconTop;
		}
		
		return i != Facing.oppositeSide[k] ? this.iconSide : this.iconBottom;
	}
	
	@Override
	public String getTextureKey() {
		return helper.getTextureKey();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getPistonExtensionTexture() {
		if (helper.vanillaTexture) return super.getPistonExtensionTexture();
		return this.iconSide;
	}
	
}
