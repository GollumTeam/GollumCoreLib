package com.gollum.core.tools.helper.blocks;

import static net.minecraft.block.BlockPistonBase.EXTENDED;
import static net.minecraft.block.BlockPistonBase.FACING;

import java.util.List;
import java.util.Map;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.tools.helper.BlockHelper;
import com.gollum.core.tools.helper.IBlockHelper;
import com.gollum.core.tools.helper.BlockHelper.PropertySubBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class HBlockContainer extends BlockContainer implements IBlockHelper {

	protected BlockHelper helper;
	
	public HBlockContainer (String registerName, Material material)  {
		super(material);
		ModGollumCoreLib.logger.info ("Create block registerName : " + registerName);
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

	@Override
	public PropertySubBlock getPropSubBlock(IBlockState state) {
		return this.helper.getPropSubBlock(state);
	}

	@Override
	public PropertyDirection getPropFacing(IBlockState state) {
		return this.helper.getPropFacing(state);
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
	// Events //
	////////////

	@Override public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    	return helper.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
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
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return helper.getPickBlock(state, target, world, pos, player);
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
	public void getSubBlocks(CreativeTabs ctab, NonNullList<ItemStack> items) {
		helper.getSubBlocks(ctab, items);
	}

	@Override
	public EnumFacing getOrientationForPlayer(BlockPos clickedBlock, Entity player) {
		return helper.getOrientationForPlayer(clickedBlock, player);
	}
}
