package com.gollum.core.inits;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.blocks.BlockProximitySpawn;

public class ModBlocks {
	
	public static BlockProximitySpawn blockProximitySpawn;
	
	public static void init() {
		ModBlocks.blockProximitySpawn = new BlockProximitySpawn (ModGollumCoreLib.config.blockSpawnerID, "BlockProximitySpawn");
	}
}
