package mods.gollum.core.tools.helper.blocks;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.tools.helper.BlockHelper;
import mods.gollum.core.tools.helper.IBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class HBlockPistonExtension extends BlockPistonExtension implements IBlockHelper {

	protected BlockHelper helper;
	
	public HBlockPistonExtension (int id, String registerName)  {
		super(id);
		ModGollumCoreLib.log.info ("Create block id : " + id + " registerName : " + registerName);
		this.helper = new BlockHelper(this, registerName);
	}
	
	public BlockHelper getGollumHelper () {
		return helper;
	}
	
	/**
	 * Nom d'enregistrement du mod
	 */
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
	
	/**
	 * Renvoie l'item en relation avec le block
	 */
	@Override
	public Item getBlockItem () {
		return helper.getBlockItem();
	}
	
	/**
	 * Libère les items de l'inventory
	 */
	public void breakBlockInventory(World world, int x, int y, int z, int oldBlodkID) {
		helper.breakBlockInventory(world, x, y, z, oldBlodkID);
	}
	
	//////////////////////////
	//Gestion des textures  //
	//////////////////////////
	
	@Override
	public void registerIcons(IconRegister iconRegister) {
		if (helper.vanillaTexture) super.registerIcons(iconRegister); else helper.registerIcons(iconRegister);
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
	
	/**
	 * Clef qui permet de générer le nom du fichier de texture 
	 * par rapport au register name en miniscule
	 * @return
	 */
	@Override
	public String getTextureKey() {
		return helper.getTextureKey();
	}
	
	
}
