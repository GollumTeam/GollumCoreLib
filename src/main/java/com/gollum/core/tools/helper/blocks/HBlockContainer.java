package com.gollum.core.tools.helper.blocks;

import java.util.List;
import java.util.Map;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.tools.helper.BlockHelper;
import com.gollum.core.tools.helper.IBlockHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class HBlockContainer extends BlockContainer implements IBlockHelper {

	protected BlockHelper helper;
	
	public HBlockContainer (String registerName, Material material)  {
		super(material);
		ModGollumCoreLib.log.info ("Create block registerName : " + registerName);
		this.helper = new BlockHelper(this, registerName);
	}
	
	public BlockHelper getGollumHelper () {
		return helper;
	}
	
	////////////
	// States //
	////////////
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return helper.getStateFromMeta(meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return helper.getMetaFromState(state);
	}
	
	@Override
	public void getSubNames(Map<Integer, String> list) {
		helper.getSubNames(list);
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
	
	//////////////
	// Register //
	//////////////
	
	/**
	 * Enregistrement du block. Appelé a la fin du postInit
	 */
	@Override
	public void register () {
		helper.register();
	}
	
	/**
	 * Enregistrement du rendu du bloc. Appelé a la fin de l'Init
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender () {
		helper.registerRender();
	}
	
	
	/**
	 * Nom d'enregistrement du mod
	 */
	@Override
	public String getRegisterName() {
		return helper.getRegisterName();
	}
	

	////////////
	// Others //
	////////////
	
	/**
	 * Renvoie l'item en relation avec le block
	 */
	@Override
	public Item getBlockItem () {
		return helper.getBlockItem();
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs ctabs, List list) {
		helper.getSubBlocks(item, ctabs, list);
	}
	
	/**
	 * Libère les items de l'inventory
	 */
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		helper.breakBlock(world, pos, state);
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
		return helper.getPickBlock(target, world, pos, player);
	}
	
}
