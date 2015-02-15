package com.gollum.core.common.config;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.config.type.BuildingConfigType;


public class ConfigBuildings extends Config {
	
	public ConfigBuildings (String modId, BuildingConfigType buildings) {
		super(modId);
		this.setRelativePath(ModGollumCoreLib.MODID+"/buildings");
		this.buildings = buildings;
	}

	@ConfigProp (
		group = "BuildingsList",
		info = "Spawn rate group between [0-10]",
		mcRestart = true,
		entryClass = "mods.gollum.core.client.gui.config.entry.BuildingEntry"
	)
	public BuildingConfigType buildings;
	
}
