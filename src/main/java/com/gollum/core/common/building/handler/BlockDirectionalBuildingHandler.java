package com.gollum.core.common.building.handler;

import com.gollum.core.common.building.Building.EnumRotate;
import com.gollum.core.common.building.Building.Unity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockTripWireHook;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDirectionalBuildingHandler extends BuildingBlockHandler {
	
	@Override
	protected boolean mustApply (World world, BlockPos pos, Unity unity) {
		Block block = (unity.state != null) ? unity.state.getBlock() : null;
		return
			block instanceof BlockDirectional  || 
			block instanceof BlockSign         ||
			block instanceof BlockLadder       ||
			block instanceof BlockFurnace      ||
			block instanceof BlockChest        ||
			block instanceof BlockDispenser    ||
			block instanceof BlockPistonBase   ||
			block instanceof BlockTorch        ||
			block instanceof BlockButton       ||
			block instanceof BlockDoor         ||
			block instanceof BlockTrapDoor     ||
			block instanceof BlockStairs       ||
			block instanceof BlockTripWireHook
		;
	}
	
	@Override
	protected IBlockState applyBlockState(World world, BlockPos pos, IBlockState state, Unity unity, EnumRotate rotate) {
		try {
			for (IProperty prop : (java.util.Set<IProperty>)state.getProperties().keySet()) {
				if (prop.getName().equals("facing")) {
					EnumFacing facing = (EnumFacing)state.getValue(prop);
					state = state.withProperty(prop, this.rotateFacing(rotate, facing));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
	}
	
	/**
	 * Retourne l'orientation retourner en fonction de la rotation
	 * @param rotate2
	 * @param orientation
	 * @return
	 */
	protected EnumFacing rotateFacing(EnumRotate rotate, EnumFacing facing) {
		if (facing != null) {
			for (int i = 0; i < rotate.rotate; i++) {
				facing = facing.rotateY();
			}	
		}
		return facing;
	}
	
}
