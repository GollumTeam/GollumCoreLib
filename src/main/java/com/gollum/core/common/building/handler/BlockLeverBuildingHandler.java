package com.gollum.core.common.building.handler;

import com.gollum.core.common.building.Building.EnumRotate;
import com.gollum.core.common.building.Building.Unity;

import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
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
		/* FIXME
		
		if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 4; } else 
		if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
		if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 2; } else 
		if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 1; } else 
		
		if (orientation == Unity.ORIENTATION_BOTTOM_VERTICAL)   { metadata = (metadata & 0x8) + 5; } else 
		if (orientation == Unity.ORIENTATION_BOTTOM_HORIZONTAL) { metadata = (metadata & 0x8) + 6; } else
		
		if (orientation == Unity.ORIENTATION_TOP_VERTICAL)   { metadata = (metadata & 0x8) + 7; } else 
		if (orientation == Unity.ORIENTATION_TOP_HORIZONTAL) { metadata = (metadata & 0x8) + 0; } else 
		{
			ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" name:"+unity.state.getBlock().getUnlocalizedName()+" pos:"+x+","+y+","+z);
		}
		*/
		return state;
	}
	
}
