package com.gollum.core.common.building.handler;

import java.util.HashMap;

import com.gollum.core.common.building.Building.EnumRotate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class BuildingBlockHandler {
	
	/**
	 * Affecte l'orientation
	 */
	public void setOrientation(WorldServer world, BlockPos pos, IBlockState state, EnumRotate rotate) {
		if (this.mustApply(world, pos, state)) {
			this.applyOrientation(world, pos, state, rotate);
		}
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

	protected void applyOrientation(World world, BlockPos pos, IBlockState state, EnumRotate rotate) {
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
