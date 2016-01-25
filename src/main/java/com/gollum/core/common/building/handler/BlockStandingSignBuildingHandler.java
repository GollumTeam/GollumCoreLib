package com.gollum.core.common.building.handler;

import static net.minecraft.block.BlockStandingSign.ROTATION;
import java.util.HashMap;

import com.gollum.core.common.building.Building.EnumRotate;

import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockStandingSignBuildingHandler extends BlockWallSignBuildingHandler {
	
	@Override
	protected boolean mustApply (World world, BlockPos pos, IBlockState state) {
		return 
			state != null && state.getBlock() instanceof BlockStandingSign
		;
	}
	
	@Override
	public void applyOrientation(World world, BlockPos pos, IBlockState state, EnumRotate rotate) {
		Integer rotation = state.getValue(ROTATION);
		world.setBlockState(pos, state.withProperty(ROTATION, (rotation + 4*rotate.rotate) % 0xF), 0);
	}
	
}
