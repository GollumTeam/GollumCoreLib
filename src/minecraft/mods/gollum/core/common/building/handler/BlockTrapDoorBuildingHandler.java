package mods.gollum.core.common.building.handler;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Building.Unity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.world.World;

public class BlockTrapDoorBuildingHandler extends BuildingBlockHandler {
	
	/**
	 * Affecte l'orientation
	 */
	@Override
	public void setOrientation(World world, int x, int y, int z, Block block, int metadata, int orientation, int rotate) {
		
		if (block instanceof BlockTrapDoor) {
			
			if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x8) + 3; } else 
			if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x8) + 1; } else 
			if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x8) + 2; } else 
			if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x8) + 0; } else 
			{
				ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" id:"+block.blockID+" pos:"+x+","+y+","+z);
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 0);
			return;
		}
		
	}
	
}
