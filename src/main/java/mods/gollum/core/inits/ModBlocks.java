package mods.gollum.core.inits;

import mods.gollum.core.common.blocks.BlockProximitySpawn;

public class ModBlocks {
	
	public static BlockProximitySpawn blockProximitySpawn;
	
	public static void init() {
		ModBlocks.blockProximitySpawn = new BlockProximitySpawn ("BlockProximitySpawn");
	}
}
