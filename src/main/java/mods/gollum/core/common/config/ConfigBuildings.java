package mods.gollum.core.common.config;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.config.type.BuildingConfigType;


public class ConfigBuildings extends Config {
	
	public ConfigBuildings (String modId, BuildingConfigType buildings) {
		super(modId);
		this.setRelativePath(ModGollumCoreLib.MODID+"/buildings");
		this.buildings = buildings;
	}

	@ConfigProp (group = "BuildingsList", info = "Spawn rate group between [0-10]", mcRestart=true)
	public BuildingConfigType buildings;
	
}
