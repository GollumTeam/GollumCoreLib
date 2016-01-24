package com.gollum.core.common.building.handler;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Building.EnumRotate;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockTrapDoorBuildingHandler extends BuildingBlockHandler {

	@Override
	protected boolean mustApply (World world, BlockPos pos, IBlockState state) {
		return 
			state != null && state.getBlock() instanceof BlockTrapDoor
		;
	}
	
	@Override
	public void applyOrientation(World world, BlockPos pos, IBlockState state, EnumFacing facing, EnumRotate rotate) {
		
		try {
		} catch (Exception e) {
			ModGollumCoreLib.log.severe("Bad facing : "+facing.getName(), state, pos);
		}
		
		/* FIXME
		if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0xC) + 0; } else 
		if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0xC) + 1; } else 
		if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0xC) + 2; } else 
		if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0xC) + 3; } else 
		{
		}
		
		world.setBlockMetadataWithNotify(x, y, z, metadata, 0);
		return;
		*/
	}
	
}
