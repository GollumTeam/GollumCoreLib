package com.gollum.core.common.building.handler;

import com.gollum.core.common.blocks.BlockProximitySpawn;
import com.gollum.core.common.building.Building.EnumRotate;
import com.gollum.core.common.building.Building.Unity;
import com.gollum.core.common.tileentities.TileEntityBlockProximitySpawn;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockProximitySpawnBuildingHandler extends BuildingBlockHandler {
	
	@Override
	protected boolean mustApply (World world, BlockPos pos, Unity unity) {
		return 
			unity.state.getBlock() instanceof BlockProximitySpawn
		;
	}
	
	@Override
	protected void applyExtra(
		World world,
		BlockPos pos,
		Unity unity,
		BlockPos initPos,
		EnumRotate rotate,
		int maxX, int maxZ
	) {
		TileEntity te  = world.getTileEntity (pos);
		if (te instanceof TileEntityBlockProximitySpawn) {
			String entity = ""; try { entity = unity.extra.get("entity"); } catch (Exception e) {} entity = (entity != null) ? entity : "Chicken";
			((TileEntityBlockProximitySpawn) te).setModId (entity);
		}
	}
	
}
