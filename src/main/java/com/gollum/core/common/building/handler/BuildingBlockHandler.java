package com.gollum.core.common.building.handler;

import java.util.HashMap;

import com.gollum.core.common.building.Building.EnumRotate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import java.util.Random;

import com.gollum.core.common.building.Building;
import com.gollum.core.common.building.Building.Unity;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class BuildingBlockHandler {
	
	public IBlockState getBlockState(WorldServer world, BlockPos pos, IBlockState state, Unity unity, EnumRotate rotate) {
		if (this.mustApply(world, pos, unity)) {
			state = this.applyBlockState(world, pos, state, unity, rotate);
		}
		return state;
	}

	public final void setExtra(
		World world,
		BlockPos pos,
		Unity unity,
		BlockPos initPos,
		EnumRotate rotate,
		int maxX, int maxZ
	) {
		if (this.mustApply(world, pos, unity)) {
			this.applyExtra(world, pos, unity, initPos, rotate, maxX, maxZ);
		}
	}
	
	protected boolean mustApply (World world, BlockPos pos, Unity unity) {
		return false;
	}
	
	protected IBlockState applyBlockState(World world, BlockPos pos, IBlockState state, Unity unity, EnumRotate rotate) {
		return state;
	}
	
	protected void applyExtra(
		World world,
		BlockPos pos,
		Unity unity,
		BlockPos initPos,
		EnumRotate rotate,
		int maxX, int maxZ
	) {
	}

	
	
}
