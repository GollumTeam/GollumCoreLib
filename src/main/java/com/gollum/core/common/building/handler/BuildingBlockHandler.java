package com.gollum.core.common.building.handler;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class BuildingBlockHandler {
	
	/**
	 * Affecte l'orientation
	 */
	public final void setOrientation(World world, int x, int y, int z, Block block, int metadata, int orientation, int rotate) {
		if (this.mustApply(world, x, y, z, block)) {
			this.applyOrientation(world, x, y, z, block, metadata, orientation, rotate);
		}
	}

	/**
	 * Insert les extras informations du block
	 * @param rotate	
	 */
	public final void setExtra(
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
		if (this.mustApply(world, x, y, z, block)) {
			this.applyExtra(block, world, random, x, y, z, extra, initX, initY, initZ, rotate, dx, dz, maxX, maxZ);
		}
	}
	
	protected boolean mustApply (World world, int x, int y, int z, Block block) {
		return false;
	}

	protected void applyOrientation(World world, int x, int y, int z, Block block, int metadata, int orientation, int rotate) {
	}
	
	protected void applyExtra(
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
	}
	
}
