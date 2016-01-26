package com.gollum.core.common.building.handler;

import com.gollum.core.common.blocks.BlockProximitySpawn;
import com.gollum.core.common.building.Building.Unity;
import com.gollum.core.common.tileentities.TileEntityBlockProximitySpawn;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockProximitySpawnBuildingHandler extends BuildingBlockHandler {
	
	@Override
	protected boolean mustApply (World world, int x, int y, int z, Unity unity) {
		return unity.block instanceof BlockProximitySpawn;
	}
	
	@Override
	public void applyExtra(
		World world,
		int x, int y, int z,
		Unity unity,
		int initX, int initY, int initZ,
		int rotate,
		int dx, int dz,
		int maxX, int maxZ
	) {
		
		TileEntity te  = world.getTileEntity (x, y, z);
		if (te instanceof TileEntityBlockProximitySpawn) {
			String entity = ""; try { entity = unity.extra.get("entity"); } catch (Exception e) {} entity = (entity != null) ? entity : "Chicken";
			((TileEntityBlockProximitySpawn) te).setModId (entity);
		}
	}
	
}
