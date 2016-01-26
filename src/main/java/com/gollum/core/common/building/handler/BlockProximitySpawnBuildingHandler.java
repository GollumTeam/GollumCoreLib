package com.gollum.core.common.building.handler;

import java.util.HashMap;

import com.gollum.core.common.blocks.BlockProximitySpawn;
import com.gollum.core.common.building.Building.EnumRotate;
import com.gollum.core.common.tileentities.TileEntityBlockProximitySpawn;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockProximitySpawnBuildingHandler extends BuildingBlockHandler {
	
	@Override
	protected boolean mustApply (World world, BlockPos pos, IBlockState state) {
		return 
			state != null && state.getBlock() instanceof BlockProximitySpawn;
	}
	
	/**
	 * Insert les extras informations du block
	 * @param rotate	
	 */
	@Override
	protected void applyExtra(
		World world,
		BlockPos pos,
		IBlockState state,
		HashMap<String, String> extra,
		BlockPos initPos,
		EnumRotate rotate,
		int maxX, int maxZ
	) {
		TileEntity te  = world.getTileEntity (pos);
		if (te instanceof TileEntityBlockProximitySpawn) {
			String entity = ""; try { entity = extra.get("entity"); } catch (Exception e) {} entity = (entity != null) ? entity : "Chicken";
			((TileEntityBlockProximitySpawn) te).setModId (entity);
		}
	}
	
}
