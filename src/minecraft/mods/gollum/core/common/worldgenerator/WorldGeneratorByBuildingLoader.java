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
import mods.gollum.core.common.config.container.BuildingConfigType;
import cpw.mods.fml.common.Loader;

public class WorldGeneratorByBuildingLoader {
	
	private ArrayList<BuildingConfigType> configs = new ArrayList<BuildingConfigType>();;

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
			
			BuildingConfigType config = parser.parse (modId);
			if (config != null) {
				this.configs.add (config);
			}				
		}
		
	}
	
	private void addBuildingList(WorldGeneratorByBuilding worldGeneratorByBuilding) {
		
		BuildingParser parser = new BuildingParser ();
		
		for (BuildingConfigType config : configs) {
			
			String modId = config.modId;
			
			for (Entry<String, BuildingConfigType.Group> groupEntry : config.lists.entrySet()) {
				BuildingConfigType.Group group = groupEntry.getValue();
				
				int idGroup = worldGeneratorByBuilding.addGroup(group.globalSpawnRate);
				
				for (Entry<String, BuildingConfigType.Group.Building> buildingEntry : group.buildings.entrySet()) {
					
					String                            buildingName   = buildingEntry.getKey();
					BuildingConfigType.Group.Building configBuilding = buildingEntry.getValue();
					
					if (!configBuilding.disabled) {
						
						Building building = parser.parse (buildingName, modId);
						
						for (Entry<Integer, BuildingConfigType.Group.Building.Dimention> entryDimentions : configBuilding.dimentions.entrySet()) {
							Integer                                     dimentionId     = entryDimentions.getKey();
							BuildingConfigType.Group.Building.Dimention configDimention = entryDimentions.getValue();
							
							ModGollumCoreLib.log.info("Register building : modId="+modId+", idGroup="+idGroup+", buildingName="+buildingName);
							
							ModGollumCoreLib.log.info(" - For dimention : "+dimentionId);
							ModGollumCoreLib.log.info(" -     spawnRate : "+configDimention.spawnRate);
							ModGollumCoreLib.log.info(" -     spawnHeight : "+configDimention.spawnHeight);
							
							building.dimentionsInfos.put (dimentionId, new Building.DimentionSpawnInfos(configDimention.spawnRate, configDimention.spawnHeight, configDimention.blocksSpawn));
							
						}
						
						worldGeneratorByBuilding.addBuilding(idGroup, building);
					}
				}
			}
		}
	}
	
}
