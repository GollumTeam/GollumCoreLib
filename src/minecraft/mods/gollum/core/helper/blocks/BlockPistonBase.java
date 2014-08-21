package mods.gollum.core.helper.blocks;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.helper.logic.BlockLogic;
import mods.gollum.core.helper.logic.IBlockLogic;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Facing;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPistonBase extends net.minecraft.block.BlockPistonBase implements IBlockLogic {
	
	protected BlockLogic logic;
	
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
	
	public BlockPistonBase(int id, String registerName, boolean isSticky)  {
		super(id, isSticky);
		ModGollumCoreLib.log.info ("Create block id : " + id + " registerName : " + registerName);
		this.logic = new BlockLogic(this, registerName);
		
		this.isSticky = isSticky;
	}
	
	@Override
	public String getRegisterName() {
		return logic.getRegisterName();
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
		
		if (logic.naturalTexture) {
			super.registerIcons(iconRegister);
			return;
		};
		
		this.iconTop    = logic.loadTexture(iconRegister, suffixTop + (this.isSticky ? suffixSticky : ""));
		this.iconOpen   = logic.loadTexture(iconRegister, suffixOpen);
		this.iconBottom = logic.loadTexture(iconRegister, suffixBotom);
		this.iconSide   = logic.loadTexture(iconRegister, suffixSide);
	}
	
	@Override
	public Icon getIcon(int i, int j) {
		
		if (logic.naturalTexture) return super.getIcon(i, j);
		
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
		return logic.getTextureKey();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getPistonExtensionTexture() {
		if (logic.naturalTexture) return super.getPistonExtensionTexture();
		return this.iconSide;
	}
	
}
