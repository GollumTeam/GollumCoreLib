package com.gollum.core.tools.helper.blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.tools.helper.BlockHelper;
import com.gollum.core.tools.helper.IBlockHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HBlockPistonExtension extends BlockPistonExtension implements IBlockHelper {

	protected BlockHelper helper;
	
	public HBlockPistonExtension (String registerName)  {
		super();
		ModGollumCoreLib.log.info ("Create block registerName : " + registerName);
		this.helper = new BlockHelper(this, registerName);
	}
	
	public BlockHelper getGollumHelper () {
		return helper;
	}
	
	/**
	 * Enregistrement du block. Appelé a la fin du postInit
	 */
	public void register () {
		helper.register();
	}
	
	/**
	 * Nom d'enregistrement du mod
	 */
	@Override
	public String getRegisterName() {
		return helper.getRegisterName();
	}
	
	/**
	 * Enregistrement du rendu du bloc. Appelé a la fin de l'Init
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerRender () {
		helper.registerRender();
	}
	
	@Override
	public void getSubNames(HashMap<Integer, String> list) {
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs ctabs, List list) {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		this.getSubNames(map);
		for (Entry<Integer, String> entry: map.entrySet()) {
			list.add(new ItemStack(item, 1, entry.getKey()));
		}
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
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		helper.breakBlock(world, pos, state);
		super.breakBlock(world, pos, state);
	}
	
}
