package com.gollum.core.inits;

import com.gollum.core.common.blocks.BlockProximitySpawn;

public class ModBlocks {
	
	public static BlockProximitySpawn PROXIMITY_SPAWN;
	
	public static void init() {
		ModBlocks.PROXIMITY_SPAWN = new BlockProximitySpawn ("proximityspawn");
	}
}
