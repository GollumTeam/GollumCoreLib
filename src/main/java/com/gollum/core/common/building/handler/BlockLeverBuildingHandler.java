package com.gollum.core.common.building.handler;

import com.gollum.core.common.building.Building.EnumRotate;
import com.gollum.core.common.building.Building.Unity;

import static net.minecraft.block.BlockLever.FACING;
import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.world.World;

public class BlockLeverBuildingHandler extends BuildingBlockHandler {
	
	@Override
	protected boolean mustApply (World world, BlockPos pos, Unity unity) {
		return 
			unity.state.getBlock() instanceof BlockLever
		;
	}
	
	@Override
	public IBlockState applyBlockState(World world, BlockPos pos, IBlockState state, Unity unity, EnumRotate rotate) {
		
		EnumOrientation orientation = state.getValue(FACING);
		return state.withProperty(FACING, this.rotateOrientation(rotate, orientation));
	}
	
	/**
	 * Retourne l'orientation retourner en fonction de la rotation
	 * @param rotate2
	 * @param orientation
	 * @return
	 */
	protected EnumOrientation rotateOrientation(EnumRotate rotate, EnumOrientation orientation) {
		if (orientation != null) {
			for (int i = 0; i < rotate.rotate; i++) {
				orientation = this.rotateY(orientation);
			}	
		}
		return orientation;
	}

	protected EnumOrientation rotateY(EnumOrientation orientation) {
		switch (orientation) {
			case NORTH:
				return EnumOrientation.EAST;
			case EAST:
				return EnumOrientation.SOUTH;
			case SOUTH:
				return EnumOrientation.WEST;
			case WEST:
				return EnumOrientation.NORTH;
				
			case DOWN_X:
				return EnumOrientation.DOWN_Z;
			case DOWN_Z:
				return EnumOrientation.DOWN_X;
			case UP_X:
				return EnumOrientation.UP_Z;
			case UP_Z:
				return EnumOrientation.UP_X;
			default:
				throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
		}
	}
	
}
