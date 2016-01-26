package com.gollum.core.common.building.handler;

import java.util.HashMap;

import com.gollum.core.common.building.Building.EnumRotate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class BuildingBlockHandler {
	
	public IBlockState getBlockState(WorldServer world, BlockPos pos, IBlockState state, EnumRotate rotate) {
		if (this.mustApply(world, pos, state)) {
			state = this.applyBlockState(world, pos, state, rotate);
		}
		return state;
	}

	/**
	 * Insert les extras informations du block
	 * @param rotate	
	 */
	public final void setExtra(
		World world,
		BlockPos pos,
		IBlockState state,
		HashMap<String, String> extra,
		BlockPos initPos,
		EnumRotate rotate,
		int maxX, int maxZ
	) {
		if (this.mustApply(world, pos, state)) {
			this.applyExtra(world, pos, state, extra, initPos, rotate, maxX, maxZ);
		}
	}
	
	protected boolean mustApply (World world, BlockPos pos, IBlockState state) {
		return false;
	}

	protected IBlockState applyBlockState(World world, BlockPos pos, IBlockState state, EnumRotate rotate) {
		return state;
	}
	
	protected void applyExtra(
		World world,
		BlockPos pos,
		IBlockState state,
		HashMap<String, String> extra,
		BlockPos initPos,
		EnumRotate rotate,
		int maxX, int maxZ
	) {
	}

	
}
