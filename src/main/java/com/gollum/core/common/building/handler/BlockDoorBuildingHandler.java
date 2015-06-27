package com.gollum.core.common.building.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.world.World;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Building.Unity;

public class BlockDoorBuildingHandler extends BuildingBlockHandler {
	
	@Override
	protected boolean mustApply (World world, int x, int y, int z, Block block) {
		return block instanceof BlockDoor;
	}
	
	@Override
	public void applyOrientation(World world, int x, int y, int z, Block block, int metadata, int orientation, int rotate) {
		
		if ((metadata & 0x8) != 0x8) {
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x3) + 3; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x3) + 1; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x3) + 2; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x3) + 0; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" name:"+block.getUnlocalizedName()+" pos:"+x+","+y+","+z);
			}
		} else {
			metadata = metadata & 0x9;
		}
		
		world.setBlockMetadataWithNotify(x, y, z, metadata, 0);
		return;
		
	}
	
}
