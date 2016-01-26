package com.gollum.core.common.building.handler;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Building.Unity;

import net.minecraft.block.BlockLever;
import net.minecraft.world.World;

public class BlockLeverBuildingHandler extends BuildingBlockHandler {
	
	@Override
	protected boolean mustApply (World world, int x, int y, int z, Unity unity) {
		return unity.block instanceof BlockLever;
	}
	
	@Override
	protected int applyMetadata(World world, int x, int y, int z, int metadata, Unity unity, int rotate) {
		
		int orientation = this.rotateOrientation(rotate, unity.orientation);
		
		if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 4; } else 
		if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
		if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 2; } else 
		if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 1; } else 
		
		if (orientation == Unity.ORIENTATION_BOTTOM_VERTICAL)   { metadata = (metadata & 0x8) + 5; } else 
		if (orientation == Unity.ORIENTATION_BOTTOM_HORIZONTAL) { metadata = (metadata & 0x8) + 6; } else
		
		if (orientation == Unity.ORIENTATION_TOP_VERTICAL)   { metadata = (metadata & 0x8) + 7; } else 
		if (orientation == Unity.ORIENTATION_TOP_HORIZONTAL) { metadata = (metadata & 0x8) + 0; } else 
		{
			ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" name:"+unity.block.getUnlocalizedName()+" pos:"+x+","+y+","+z);
		}
		
		return metadata;
		
	}
	
}
