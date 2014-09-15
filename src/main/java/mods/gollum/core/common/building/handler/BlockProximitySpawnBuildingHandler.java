package mods.gollum.core.common.building.handler;

import java.util.HashMap;
import java.util.Random;

import mods.gollum.core.common.blocks.BlockProximitySpawn;
import mods.gollum.core.common.tileentities.TileEntityBlockProximitySpawn;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockProximitySpawnBuildingHandler extends BuildingBlockHandler {
	
	/**
	 * Insert les extras informations du block
	 * @param rotate	
	 */
	@Override
	public void setExtra(
		Block block,
		World world,
		Random random, 
		int x, int y, int z, 
		HashMap<String, String> extra,
		int initX, int initY, int initZ, 
		int rotate,
		int dx, int dz,
		int maxX, int maxZ
	) {
		
		if (block instanceof BlockProximitySpawn) {
			
			TileEntity te  = world.getTileEntity (x, y, z);
			if (te instanceof TileEntityBlockProximitySpawn) {
				String entity = ""; try { entity = extra.get("entity"); } catch (Exception e) {} entity = (entity != null) ? entity : "Chicken";
				((TileEntityBlockProximitySpawn) te).setModId (entity);
			}
		}
	}
	
}
