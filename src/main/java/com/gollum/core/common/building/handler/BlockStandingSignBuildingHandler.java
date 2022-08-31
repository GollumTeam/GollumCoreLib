package com.gollum.core.common.building.handler;

import static net.minecraft.block.BlockStandingSign.ROTATION;
import java.util.HashMap;

import com.gollum.core.common.building.Building.EnumRotate;
import com.gollum.core.common.building.Building.Unity;

import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStandingSignBuildingHandler extends BlockWallSignBuildingHandler {
	
	@Override
	protected boolean mustApply (World world, BlockPos pos, Unity unity) {
		return 
			unity.state.getBlock() instanceof BlockStandingSign
		;
	}
	
	@Override
	protected IBlockState applyBlockState(World world, BlockPos pos, IBlockState state, Unity unity, EnumRotate rotate) {
		Integer rotation = state.getValue(ROTATION);
		return state.withProperty(ROTATION, (rotation + 4*rotate.rotate) % 0xF);
	}
	
}
