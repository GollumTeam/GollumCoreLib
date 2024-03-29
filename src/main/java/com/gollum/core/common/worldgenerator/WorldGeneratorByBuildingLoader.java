package com.gollum.core.common.worldgenerator;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.building.Building;
import com.gollum.core.common.building.BuildingParser;
import com.gollum.core.common.building.ModBuildingParser;
import com.gollum.core.common.config.type.BuildingConfigType;

import net.minecraftforge.fml.common.Loader;

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
			ModGollumCoreLib.logger.info("Search buildings in mod : "+modId);
			
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
					
					if (configBuilding.enabled) {
						
						Building building = parser.parse (buildingName, modId);
						
						for (Entry<Integer, BuildingConfigType.Group.Building.Dimention> entryDimentions : configBuilding.dimentions.entrySet()) {
							Integer                                     dimentionId     = entryDimentions.getKey();
							BuildingConfigType.Group.Building.Dimention configDimention = entryDimentions.getValue();
							
							ModGollumCoreLib.logger.info("Register building : modId="+modId+", idGroup="+idGroup+", buildingName="+buildingName);
							
							ModGollumCoreLib.logger.info(" - For dimention : "+dimentionId);
							ModGollumCoreLib.logger.info(" -     spawnRate : "+configDimention.spawnRate);
							ModGollumCoreLib.logger.info(" -     spawnMin  : "+configDimention.spawnMin);
							ModGollumCoreLib.logger.info(" -     spawnMax  : "+configDimention.spawnMax);
							
							building.dimentionsInfos.put (dimentionId, new Building.DimentionSpawnInfos(configDimention.spawnRate, configDimention.spawnMin, configDimention.spawnMax, configDimention.blocksSpawn, configDimention.biomes));
							
						}
						
						worldGeneratorByBuilding.addBuilding(idGroup, building);
					}
				}
			}
		}
	}
	
}
