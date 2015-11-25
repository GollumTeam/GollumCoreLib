package com.gollum.core.inits;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.tileentities.TileEntityBlockProximitySpawn;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
	
	public static void init() {
		GameRegistry.registerTileEntity(TileEntityBlockProximitySpawn.class, ModGollumCoreLib.MODID+":BlockProximitySpawn");
	}
}
