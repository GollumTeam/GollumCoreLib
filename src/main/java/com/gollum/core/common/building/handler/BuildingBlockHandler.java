package com.gollum.core.common.building.handler;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class BuildingBlockHandler {

	/**
	 * Affecte l'orientation
	 */
	public void setOrientation(World world, int x, int y, int z, Block block, int metadata, int orientation, int rotate) {
	}
	
	/**
	 * Insert les extras informations du block
	 * @param rotate	
	 */
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
	}
	
}
