package com.gollum.core.common.building.handler;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Building.Unity;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.world.World;

public class BlockDirectionalWithBit1BuildingHandler extends BuildingBlockHandler {

	@Override
	protected boolean mustApply (World world, int x, int y, int z, Unity unity) {
		return
			unity.block instanceof BlockLadder ||
			unity.block instanceof BlockFurnace ||
			unity.block instanceof BlockChest ||
			unity.block instanceof BlockDispenser ||
			unity.block instanceof BlockPistonBase
		;
	}
	
	@Override
	protected int applyMetadata(World world, int x, int y, int z, int metadata, Unity unity, int rotate) {
		
		int orientation = this.rotateOrientation(rotate, unity.orientation);
	
		if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 2; } else 
		if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
		if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 4; } else 
		if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 5; } else 
		{
			ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" name:"+unity.block.getUnlocalizedName()+" pos:"+x+","+y+","+z);
		}
		
		return metadata;
	}
	
}
