package com.gollum.core.common.building.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.world.World;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Building.Unity;

public class BlockDirectionalWithBit1BuildingHandler extends BuildingBlockHandler {

	@Override
	protected boolean mustApply (World world, int x, int y, int z, Block block) {
		return
			block instanceof BlockLadder ||
			block instanceof BlockFurnace ||
			block instanceof BlockChest ||
			block instanceof BlockDispenser ||
			block instanceof BlockPistonBase
		;
	}
	
	@Override
	public void applyOrientation(World world, int x, int y, int z, Block block, int metadata, int orientation, int rotate) {
	
		if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 2; } else 
		if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
		if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 4; } else 
		if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 5; } else 
		{
			ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" name:"+block.getUnlocalizedName()+" pos:"+x+","+y+","+z);
		}
		
		world.setBlockMetadataWithNotify(x, y, z, metadata, 0);
		return;
	}
	
}
