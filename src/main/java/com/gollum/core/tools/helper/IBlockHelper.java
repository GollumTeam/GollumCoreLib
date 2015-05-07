package com.gollum.core.tools.helper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public interface IBlockHelper {
	
	public BlockHelper getGollumHelper ();
	
	/**
	 * Enregistrement du block. Appelé a la fin du postInit
	 */
	public void register ();
	
	/**
	 * Nom d'enregistrement du mod
	 */
	public String getRegisterName();
	
	public void registerIcons(IconRegister iconRegister);
	
	/**
	 * Setter de l'icon de l'objet
	 * @param icon
	 */
	public IBlockHelper setIcon (Icon icon);
	
	/**
	 * Clef qui permet de générer le nom du fichier de texture 
	 * par rapport au register name en miniscule
	 * @return
	 */
	public String getTextureKey ();
	
	/**
	 * Affect la class de l'objet qui servira item pour le block
	 * par default ItemBlock
	 * @param itemClass
	 * @return 
	 */
	public Block setItemBlockClass (Class<? extends ItemBlock> itemClass);
	
	/**
	 * Renvoie l'item en relation avec le block
	 */
	public Item getBlockItem ();
	
	/**
	 * Libère les items de l'inventory
	 */
	public void breakBlockInventory(World world, int x, int y, int z, Block oldBlock);

}