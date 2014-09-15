package mods.gollum.core.common.building.handler;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Building.Unity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.world.World;

public class BlockDoorBuildingHandler extends BuildingBlockHandler {
	
	/**
	 * Affecte l'orientation
	 */
	@Override
	public void setOrientation(World world, int x, int y, int z, Block block, int metadata, int orientation, int rotate) {
		
		if (block instanceof BlockDoor) {
			
			if ((metadata & 0x8) != 0x8) {
				if (orientation == Unity.ORIENTATION_UP)    { metadata = (metadata & 0x4) + 3; } else 
				if (orientation == Unity.ORIENTATION_DOWN)  { metadata = (metadata & 0x4) + 1; } else 
				if (orientation == Unity.ORIENTATION_LEFT)  { metadata = (metadata & 0x4) + 2; } else 
				if (orientation == Unity.ORIENTATION_RIGTH) { metadata = (metadata & 0x4) + 0; } else 
				{
					ModGollumCoreLib.log.severe("Bad orientation : "+orientation+" name:"+block.getUnlocalizedName()+" pos:"+x+","+y+","+z);
				}
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metadata, 0);
			return;
		}
		
	}
	
}
