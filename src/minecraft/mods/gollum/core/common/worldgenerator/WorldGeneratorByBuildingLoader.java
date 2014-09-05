package mods.gollum.core.common.worldgenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Building;
import mods.gollum.core.common.building.BuildingParser;
import mods.gollum.core.common.building.ModBuildingParser;
import mods.gollum.core.common.config.ConfigBuildings;
import mods.gollum.core.common.config.container.BuildingConfig;
import cpw.mods.fml.common.Loader;

public class WorldGeneratorByBuildingLoader {
	
	private ArrayList<BuildingConfig> configs = new ArrayList<BuildingConfig>();;

	public WorldGeneratorByBuilding load() {
		
		WorldGeneratorByBuilding worldGeneratorByBuilding = new WorldGeneratorByBuilding ();

		this.loadBuildingList();
		this.addBuildingList(worldGeneratorByBuilding);
		
		return worldGeneratorByBuilding;
	}

	public void loadBuildingList () {
		
		
		ModBuildingParser parser = new ModBuildingParser ();
		
		for (String modId : Loader.instance().getIndexedModList().keySet()) {
			ModGollumCoreLib.log.info("Search buildings in mod : "+modId);
			
			BuildingConfig config = parser.parse (modId);
			if (config != null) {
				this.configs.add (config);
			}				
		}
		
	}
	
	private void addBuildingList(WorldGeneratorByBuilding worldGeneratorByBuilding) {
		
		BuildingParser parser = new BuildingParser ();
		
		for (BuildingConfig config : configs) {
			
			String modId = config.modId;
			
			for (Entry<String, BuildingConfig.Group> groupEntry : config.lists.entrySet()) {
				BuildingConfig.Group group = groupEntry.getValue();
				
				int idGroup = worldGeneratorByBuilding.addGroup(group.globalSpawnRate);
				
				for (Entry<String, HashMap<Integer, BuildingConfig.Building>> buildingEntry : group.buildings.entrySet()) {
					
					String                                    buildingName   = buildingEntry.getKey();
					HashMap<Integer, BuildingConfig.Building> configBuilding = buildingEntry.getValue();
					
					Building building = parser.parse (buildingName, modId);
					
					ModGollumCoreLib.log.info("Register building : modId="+modId+", idGroup="+idGroup+", buildingName="+buildingName);
					
					for (Entry<Integer, BuildingConfig.Building> entryBuildingInfos : configBuilding.entrySet()) {
						
						Integer                 dimention           = entryBuildingInfos.getKey();
						BuildingConfig.Building configBuildingInfos = entryBuildingInfos.getValue();
						
						ModGollumCoreLib.log.info(" - For dimention : "+dimention);
						ModGollumCoreLib.log.info(" -     spawnRate : "+configBuildingInfos.spawnRate);
						ModGollumCoreLib.log.info(" -     spawnHeight : "+configBuildingInfos.spawnHeight);
						
						if (configBuildingInfos.spawnRate > 0) { // USe param enabled for staff
							
							building.dimentionsInfos.put (dimention, new Building.DimentionSpawnInfos(configBuildingInfos.spawnRate, configBuildingInfos.spawnHeight, configBuildingInfos.getBlocksSpawn()));
							
							worldGeneratorByBuilding.addBuilding(idGroup, dimention, building);
						}
					}
					
				}
			}
		}
	}
	
}
