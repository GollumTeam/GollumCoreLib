package mods.gollum.core.inits;

import mods.gollum.core.common.tileentities.TileEntityBlockProximitySpawn;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModTileEntities {
	
	public static void init() {
		GameRegistry.registerTileEntity(TileEntityBlockProximitySpawn.class, "GollumCoreLib:BlockProximitySpawn");
	}
}
