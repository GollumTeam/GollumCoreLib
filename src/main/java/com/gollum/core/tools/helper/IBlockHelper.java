package com.gollum.core.tools.helper;

import java.util.List;
import java.util.Map;

import com.gollum.core.tools.helper.BlockHelper.PropertySubBlock;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IBlockHelper {
	
	public BlockHelper getGollumHelper ();
	
	////////////
	// States //
	////////////
	
	public IBlockState getStateFromMeta(int meta);
	
	public int getMetaFromState(IBlockState state);
	
	public void getSubNames(Map<Integer, String> list);
	
	public PropertySubBlock getPropSubBlock(IBlockState state);
	
	public PropertyDirection getPropFacing(IBlockState state);
	
	//////////////
	// Register //
	//////////////
	
	/**
	 * Enregistrement du block. Appelé a la fin du postInit
	 */
	public void register ();
	
	/**
	 * Nom d'enregistrement du mod
	 */
	public String getRegisterName();
	
	/**
	 * Enregistrement du rendu du bloc. Appelé a la fin de l'Init
	 */
	@SideOnly(Side.CLIENT)
	public void registerRender();
	
	/**
	 * Affect la class de l'objet qui servira item pour le block
	 * par default ItemBlock
	 * @param itemClass
	 * @return 
	 */
	public Block setItemBlockClass (Class<? extends ItemBlock> itemClass);
	
	////////////
	// Events //
	////////////
	
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer);
	
	/**
	 * Libère les items de l'inventory
	 */
	public void breakBlock(World world, BlockPos pos, IBlockState state);

	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player);
	
	
	////////////
	// Others //
	////////////
	
	/**
	 * Renvoie l'item en relation avec le block
	 */
	public Item getBlockItem ();
	
	public void getSubBlocks(Item item, CreativeTabs ctabs, List list);
	
	public EnumFacing getOrientationForPlayer(BlockPos clickedBlock, Entity player);
	
}