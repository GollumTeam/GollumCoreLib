package mods.gollum.core.common.config.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.FieldReobfKey;
import mods.gollum.core.common.config.IConfigJsonClass;
import mods.gollum.core.common.config.IConfigMerge;
import net.minecraft.block.Block;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonObjectNodeBuilder;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;

public class BuildingConfig implements IConfigJsonClass, IConfigMerge {
	
	public class Group {
		public Integer globalSpawnRate = null;
		public HashMap<String, HashMap<Integer, Building>> buildings = new HashMap<String, HashMap<Integer, Building>>();
	}
	
	public class Building {
		
		private FieldReobfKey fieldReobfKey = new FieldReobfKey();
		
		public Integer spawnRate = null;
		public ArrayList<String> blocksSpawn = new ArrayList<String>();
		public Integer spawnHeight = null;
		
		
		public ArrayList<Block> getBlocksSpawn() {
			ArrayList<Block> blocks = new ArrayList<Block>();
			
			for (String blockSpawn : blocksSpawn) {
				Object obj;
				try {
					obj = this.fieldReobfKey.getTarget(blockSpawn);
				
					if (obj instanceof Block) {
						blocks.add((Block) obj);
					} else if (obj == null) {
						ModGollumCoreLib.log.severe("Error field is null : "+blockSpawn);
					} else {
						ModGollumCoreLib.log.severe("Error field is'nt block : "+blockSpawn);
					}
				} catch (Exception e) {
					ModGollumCoreLib.log.severe("Error field not found : "+blockSpawn);
				}
			}
			
			return blocks;
		}
	}

	
	private FieldReobfKey fieldReobfKey = new FieldReobfKey();
	public String modId = null;
	public HashMap<String, Group> lists = new HashMap<String, BuildingConfig.Group>();
	
	public BuildingConfig () {
	}
	
	public BuildingConfig (JsonNode json, String modId) throws Exception {
		this.modId = modId;
		this.readConfig(json);
	}
	
	
	//////////
	// Read //
	//////////
	
	@Override
	public void readConfig(JsonNode json)  throws Exception {
		

		Map<JsonStringNode, JsonNode> map = json.getFields();
		for (Entry<JsonStringNode, JsonNode> entry : map.entrySet()) {
			String groupName   = entry.getKey().getText();
			JsonNode jsonGroup = entry.getValue();
			
			ModGollumCoreLib.log.debug("Building group :"+groupName);
			
			Group group = new Group();
			group.globalSpawnRate = Integer.parseInt(jsonGroup.getNumberValue("globalSpawnRate"));
			group.buildings       = this.readBuildings (jsonGroup.getNode("buildings"));
			
			lists.put(groupName, group);
		}
	}
	
	private HashMap<String, HashMap<Integer, Building>> readBuildings(JsonNode node) {
		
		
		HashMap<String, HashMap<Integer, Building>> rtn = new HashMap<String, HashMap<Integer, Building>>();
		
		Map<JsonStringNode, JsonNode> mapBuildings = node.getFields();
		for (Entry<JsonStringNode, JsonNode> entryBuilding : mapBuildings.entrySet()) {
			
			String   buildingName   = entryBuilding.getKey().getText();
			JsonNode jsonDimentions = entryBuilding.getValue();
			
			HashMap<Integer, Building> rtnBuildingData = new HashMap<Integer, Building> ();
			
			// Lecture de chaque dimention
			for (JsonNode nodeDimentionInfoBuilding : jsonDimentions.getElements()) {
				
				Integer dimention = Integer.parseInt(nodeDimentionInfoBuilding.getNumberValue("dimention"));
				
				Building building = new Building ();
				building.spawnRate   = Integer.parseInt(nodeDimentionInfoBuilding.getNumberValue("spawnRate"));
				building.spawnHeight = Integer.parseInt(nodeDimentionInfoBuilding.getNumberValue("spawnHeight"));
				for (JsonNode jsonBlock : nodeDimentionInfoBuilding.getNode("blocksSpawn").getElements()) {
					String key = jsonBlock.getText();
					try {
						Object obj = this.fieldReobfKey.getTarget(key);
						
						if (obj instanceof Block) {
							building.blocksSpawn.add(key);
						} else if (obj == null) {
							ModGollumCoreLib.log.severe("Error field is null : "+key);
						} else {
							ModGollumCoreLib.log.severe("Error field is'nt block : "+key);
						}
					} catch (Exception e) {
						ModGollumCoreLib.log.severe("Error field not found : "+key);
					}
				}
				
				rtnBuildingData.put(dimention, building);
				
			}
			
			rtn.put(buildingName, rtnBuildingData);
			
		}
		
		return rtn;
	}
	
	///////////
	// Write //
	///////////
	
	@Override
	public JsonRootNode writeConfig() {
		JsonObjectNodeBuilder builder = JsonNodeBuilders.anObjectBuilder();
		
		for (Entry<String, Group> entry : this.lists.entrySet()) {
			String groupName = entry.getKey();
			Group group      = entry.getValue();
			
			JsonObjectNodeBuilder jsonGroup = JsonNodeBuilders.anObjectBuilder()
				.withField("globalSpawnRate", JsonNodeBuilders.aNumberBuilder(group.globalSpawnRate.toString()))
				.withField("buildings", this.getJsonBuildings (group.buildings))
			;
			
			builder.withField(groupName, jsonGroup);
		}
		
		
		
		return builder.build();
	}
	
	private JsonNodeBuilder getJsonBuildings(HashMap<String, HashMap<Integer, Building>> buildings) {
		

		JsonObjectNodeBuilder jsonBuildings = JsonNodeBuilders.anObjectBuilder();
		
		
		for (Entry<String, HashMap<Integer, Building>> entryBuilding : buildings.entrySet()) {
			String buildingName                 = entryBuilding.getKey();
			HashMap<Integer, Building> building = entryBuilding.getValue();
			
			
			JsonArrayNodeBuilder jsonBuilding = JsonNodeBuilders.anArrayBuilder();
			
			for (Entry<Integer, Building> entryBuildingInfo : building.entrySet()) {
				
				Integer  dimention    = entryBuildingInfo.getKey();
				Building buildingInfo = entryBuildingInfo.getValue();
				
				JsonObjectNodeBuilder nodeDimentionBuildingInfo = JsonNodeBuilders.anObjectBuilder()
					.withField("dimention", JsonNodeBuilders.aNumberBuilder(dimention.toString()))
					.withField("spawnRate", JsonNodeBuilders.aNumberBuilder(buildingInfo.spawnRate.toString()))
					.withField("spawnHeight", JsonNodeBuilders.aNumberBuilder(buildingInfo.spawnHeight.toString()))
					.withField("blocksSpawn", this.getJsonBlocksSpawn (buildingInfo.blocksSpawn))
				;
				
				jsonBuilding.withElement(nodeDimentionBuildingInfo);
			}
			
			jsonBuildings.withField(buildingName, jsonBuilding);
		}
		
		return jsonBuildings;
	}
	

	private JsonNodeBuilder getJsonBlocksSpawn(ArrayList<String> blocksSpawn) {
		JsonArrayNodeBuilder jsonBlocksSpawn = JsonNodeBuilders.anArrayBuilder();
		
		for (String block : blocksSpawn) {
			try {
				jsonBlocksSpawn.withElement(JsonNodeBuilders.aStringBuilder(this.fieldReobfKey.humanQualifiedName(block)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jsonBlocksSpawn;
	}
	
	
//
//	private JsonNodeBuilder getJsonDimentions(ArrayList<Integer> dimentions) {
//		JsonArrayNodeBuilder jsonDimentions = JsonNodeBuilders.anArrayBuilder();
//		
//		for (Integer dimention : dimentions) {
//			jsonDimentions.withElement(JsonNodeBuilders.aNumberBuilder(dimention.toString()));
//		}
//		
//		return jsonDimentions;
//	}

	public boolean equals (BuildingConfig obj) {
		return false;
	}

	@Override
	public boolean merge(Object newValueObj) {
		
		
		BuildingConfig newValue = (BuildingConfig)newValueObj;
		for (Entry<String, Group> entry : this.lists.entrySet()) {
			String groupName = entry.getKey();
			Group group = entry.getValue();
			
			// Si le le mod et la config on le meme groupe
			if (newValue.lists.containsKey(groupName)) {
				Group newGroup = newValue.lists.get(groupName);
				
				group.globalSpawnRate = newGroup.globalSpawnRate;
				
				for (Entry<String, HashMap<Integer, Building>> entryBuilding : group.buildings.entrySet()) {
					String buildingName = entryBuilding.getKey();
					HashMap<Integer, Building> building = entryBuilding.getValue();
					
					if (newGroup.buildings.containsKey(buildingName)) {
						group.buildings.put(buildingName, newGroup.buildings.get(buildingName));
					}
				}
				
			} 
		}
		
		return false;
	}
}
