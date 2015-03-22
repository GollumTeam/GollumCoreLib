package mods.gollum.core.common.config;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.config.container.BuildingConfigType;


public class ConfigBuildings extends Config<ConfigBuildings> {
	
	public ConfigBuildings (String modId, BuildingConfigType buildings) {
		super(modId);
		this.setRelativePath(ModGollumCoreLib.MODID+"/buildings");
		this.buildings = buildings;
	}

	@ConfigProp (group = "Buildings List", info = "Spawn rate group between [0-10]")
	public BuildingConfigType buildings;
	
}
