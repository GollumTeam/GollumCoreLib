package com.gollum.core.inits;

import com.gollum.core.common.blocks.BlockProximitySpawn;

public class ModBlocks {
	
	public static BlockProximitySpawn blockProximitySpawn;
	
	public static void init() {
		ModBlocks.blockProximitySpawn = new BlockProximitySpawn ("BlockProximitySpawn");
	}
}
