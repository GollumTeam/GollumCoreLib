package mods.gollum.core.common.worldgenerator;

import java.io.IOException;
import java.util.ArrayList;
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
				
				for (Entry<String, BuildingConfig.Building> buildingEntry : group.buildings.entrySet()) {
					String buildingName                    = buildingEntry.getKey();
					BuildingConfig.Building configBuilding = buildingEntry.getValue();
					
					
					Building building = parser.parse (buildingName, modId);
					building.spawnHeight = configBuilding.spawnHeight;
					building.blocksSpawn = configBuilding.getBlocksSpawn();
					
					for (int dimention : configBuilding.dimentions) {
						ModGollumCoreLib.log.info("Register building : modId="+modId+", idGroup="+idGroup+", buildingName="+buildingName+", spawnRate="+configBuilding.spawnRate+", dimention="+dimention);
						worldGeneratorByBuilding.addBuilding(idGroup, building, configBuilding.spawnRate, dimention);
					}
				}
			}
		}
	}
	
}
