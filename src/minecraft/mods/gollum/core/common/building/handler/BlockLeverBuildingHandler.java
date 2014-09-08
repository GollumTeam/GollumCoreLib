package mods.gollum.core.common.building.handler;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Building.Unity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.world.World;

public class BlockLeverBuildingHandler extends BuildingBlockHandler {
	
	/**
	 * Affecte l'orientation
	 */
	@Override
	public void setOrientation(World world, int x, int y, int z, Block block, int metadata, int orientation, int rotate) {
		
		if (block instanceof BlockLever) {
			
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 4; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 1; } else 
			
			if (orientation == Unity.ORIENTATION_BOTTOM_VERTICAL)   { metadata = (metadata & 0x8) + 5; } else 
			if (orientation == Unity.ORIENTATION_BOTTOM_HORIZONTAL) { metadata = (metadata & 0x8) + 6; } else
			
			if (orientation == Unity.ORIENTATION_TOP_VERTICAL)   { metadata = (metadata & 0x8) + 7; } else 
			if (orientation == Unity.ORIENTATION_TOP_HORIZONTAL) { metadata = (metadata & 0x8) + 0; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 0);
			return;
		}
		
	}
	
}
