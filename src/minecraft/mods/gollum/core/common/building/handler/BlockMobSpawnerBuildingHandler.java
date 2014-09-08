package mods.gollum.core.common.building.handler;

import java.util.HashMap;
import java.util.Random;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.blocks.BlockProximitySpawn;
import mods.gollum.core.common.building.Builder;
import mods.gollum.core.common.tileentities.TileEntityBlockSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

public class BlockMobSpawnerBuildingHandler extends BuildingBlockHandler {
	
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
		
		if (block instanceof BlockMobSpawner) {
			
			TileEntity te  = world.getBlockTileEntity (x, y, z);
			if (te instanceof TileEntityMobSpawner) {
				String entity = ""; try { entity = extra.get("entity"); } catch (Exception e) {} entity = (entity != null) ? entity : "Pigg";
				((TileEntityMobSpawner) te).getSpawnerLogic().setMobID(entity);
			}
		}
	}
	
}
