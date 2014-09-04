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
		public HashMap<String, Building> buildings = new HashMap<String, Building>();
	}
	
	public class Building {
		
		private FieldReobfKey fieldReobfKey = new FieldReobfKey();
		
		public Integer spawnRate = null;
		public ArrayList<Integer> dimentions = new ArrayList<Integer>();
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
	

	
	@Override
	public void readConfig(JsonNode json)  throws Exception {
		

		Map<JsonStringNode, JsonNode> map = json.getFields();
		for (Entry<JsonStringNode, JsonNode> entry : map.entrySet()) {
			String groupName   = entry.getKey().getText();
			JsonNode jsonGroup = entry.getValue();
			
			ModGollumCoreLib.log.debug("Building group :"+groupName);
			
			Group group = new Group();
			group.globalSpawnRate = Integer.parseInt(jsonGroup.getNumberValue("globalSpawnRate"));
			
			Map<JsonStringNode, JsonNode> mapGroup = jsonGroup.getNode("buildings").getFields();
			for (Entry<JsonStringNode, JsonNode> entryBuilding : mapGroup.entrySet()) {
				String buildingName   = entryBuilding.getKey().getText();
				JsonNode jsonBuilding = entryBuilding.getValue();
				
				Building building = new Building ();
				building.spawnRate = Integer.parseInt(jsonBuilding.getNumberValue("spawnRate"));
				building.spawnHeight = Integer.parseInt(jsonBuilding.getNumberValue("spawnHeight"));
				for (JsonNode jsonDimention : jsonBuilding.getNode("dimentions").getElements()) {
					building.dimentions.add(new Integer(jsonDimention.getNumberValue()));
				}
				for (JsonNode jsonBlock : jsonBuilding.getNode("blocksSpawn").getElements()) {
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
				
				group.buildings.put (buildingName, building);
			}
			
			lists.put(groupName, group);
		}
	}
	
	
	
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
	
	private JsonNodeBuilder getJsonBuildings(HashMap<String, Building> buildings) {
		

		JsonObjectNodeBuilder jsonBuildings = JsonNodeBuilders.anObjectBuilder();
		
		
		for (Entry<String, Building> entryBuilding : buildings.entrySet()) {
			String buildingName = entryBuilding.getKey();
			Building building   = entryBuilding.getValue();
			
			JsonObjectNodeBuilder jsonBuilding = JsonNodeBuilders.anObjectBuilder()
				.withField("spawnRate", JsonNodeBuilders.aNumberBuilder(building.spawnRate.toString()))
				.withField("dimentions", this.getJsonDimentions (building.dimentions))
				.withField("blocksSpawn", this.getJsonBlocksSpawn (building.blocksSpawn))
				.withField("spawnHeight", JsonNodeBuilders.aNumberBuilder(building.spawnHeight.toString()))
			;
			
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

	private JsonNodeBuilder getJsonDimentions(ArrayList<Integer> dimentions) {
		JsonArrayNodeBuilder jsonDimentions = JsonNodeBuilders.anArrayBuilder();
		
		for (Integer dimention : dimentions) {
			jsonDimentions.withElement(JsonNodeBuilders.aNumberBuilder(dimention.toString()));
		}
		
		return jsonDimentions;
	}

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
				
				for (Entry<String, Building> entryBuilding : group.buildings.entrySet()) {
					String buildingName = entryBuilding.getKey();
					Building building = entryBuilding.getValue();
					
					if (newGroup.buildings.containsKey(buildingName)) {
						group.buildings.put(buildingName, newGroup.buildings.get(buildingName));
					}
				}
				
			} 
		}
		
		return false;
	}
}
