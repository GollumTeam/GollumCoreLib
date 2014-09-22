package mods.gollum.core.common.config.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.config.IConfigMerge;
import mods.gollum.core.common.config.type.BuildingConfigType.Group.Building;
import mods.gollum.core.common.config.type.BuildingConfigType.Group.Building.Dimention;
import mods.gollum.core.tools.registered.RegisteredObjects;
import net.minecraft.block.Block;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonObjectNodeBuilder;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;

public class BuildingConfigType implements IConfigJsonClass, IConfigMerge {
	
	public static class Group {
		
		public Integer globalSpawnRate = null;
		public HashMap<String, Building> buildings = new HashMap<String, Building>();
		
		
		public static class Building {

			public boolean disabled = false;
			public HashMap<Integer, Dimention> dimentions = new HashMap<Integer, Dimention>();
			
			public static class Dimention {
				
				public Integer spawnRate = null;
				public Integer spawnHeight = null;
				public ArrayList<Block> blocksSpawn = new ArrayList<Block>();
				
			}
		}
		
	}
	
	public String modId = null;
	public HashMap<String, Group> lists = new HashMap<String, BuildingConfigType.Group>();
	
	public BuildingConfigType () {
	}
	
	public BuildingConfigType (JsonNode json, String modId) throws Exception {
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
	
	private HashMap<String, Building> readBuildings(JsonNode node) {
		
		
		HashMap<String, Building> rtn = new HashMap<String, Building>();
		
		Map<JsonStringNode, JsonNode> mapBuildings = node.getFields();
		for (Entry<JsonStringNode, JsonNode> entryBuilding : mapBuildings.entrySet()) {
			
			String   buildingName = entryBuilding.getKey().getText();
			JsonNode jsonBuilding = entryBuilding.getValue();
			
			Building building   = new Building ();
			building.disabled   = false; try { building.disabled = jsonBuilding.getBooleanValue("disabled"); } catch (Exception e) {}
			building.dimentions = this.readDimentions (jsonBuilding.getNode("dimentions"));
			
			rtn.put(buildingName, building);
		}
		
		return rtn;
	}
	
	private HashMap<Integer, Dimention> readDimentions(JsonNode node) {
		
		HashMap<Integer, Dimention> rtn = new HashMap<Integer, Dimention> ();
		
		Map<JsonStringNode, JsonNode> mapBuildings = node.getFields();
		for (Entry<JsonStringNode, JsonNode> entryDimention : mapBuildings.entrySet()) {
			
			Integer id = Integer.parseInt(entryDimention.getKey().getText());
			JsonNode jsonDimention = entryDimention.getValue();
			
			Dimention dimention = new Dimention();
			dimention.spawnRate   = Integer.parseInt(jsonDimention.getNumberValue("spawnRate"));
			dimention.spawnHeight = Integer.parseInt(jsonDimention.getNumberValue("spawnHeight"));
			
			for (JsonNode jsonBlock : jsonDimention.getNode("blocksSpawn").getElements()) {
				String key = jsonBlock.getText();
				try {
					Block b = RegisteredObjects.instance().getBlock(key);
					
					if (b != null) {
						dimention.blocksSpawn.add(b);
					} else {
						ModGollumCoreLib.log.severe("Error block not found : "+key);
					}
				} catch (Exception e) {
					ModGollumCoreLib.log.severe("Error block not found : "+key);
				}
			}
			
			rtn.put (id, dimention);
			
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
	
	private JsonNodeBuilder getJsonBuildings(HashMap<String, Building> buildings) {
		
		JsonObjectNodeBuilder jsonBuildings = JsonNodeBuilders.anObjectBuilder();
		
		
		for (Entry<String, Building> entryBuilding : buildings.entrySet()) {
			
			String   buildingName = entryBuilding.getKey();
			Building building     = entryBuilding.getValue();
			
			JsonObjectNodeBuilder jsonBuilding = JsonNodeBuilders.anObjectBuilder ()
				.withField("disabled", (building.disabled) ? JsonNodeBuilders.aTrueBuilder() : JsonNodeBuilders.aFalseBuilder())
				.withField("dimentions", this.getJsonDimentions (building.dimentions))
			;
			
			jsonBuildings.withField(buildingName, jsonBuilding);
		}
		
		return jsonBuildings;
	}
	
	private JsonNodeBuilder getJsonDimentions( HashMap<Integer, Dimention> dimentions) {

		JsonObjectNodeBuilder jsonDimentions = JsonNodeBuilders.anObjectBuilder();

		for (Entry<Integer, Dimention> entryDimentions : dimentions.entrySet()) {
			Integer dimentionId = entryDimentions.getKey();
			Dimention dimention = entryDimentions.getValue();
			
			JsonObjectNodeBuilder jsonDimention = JsonNodeBuilders.anObjectBuilder ()
				.withField("spawnHeight", JsonNodeBuilders.aNumberBuilder(dimention.spawnHeight.toString()))
				.withField("spawnRate", JsonNodeBuilders.aNumberBuilder(dimention.spawnRate.toString()))
				.withField("blocksSpawn", this.getJsonBlocksSpawn (dimention.blocksSpawn))
			;

			jsonDimentions.withField(dimentionId.toString(), jsonDimention);
		}
		
		return jsonDimentions;
	}

	private JsonNodeBuilder getJsonBlocksSpawn(ArrayList<Block> blocksSpawn) {
		JsonArrayNodeBuilder jsonBlocksSpawn = JsonNodeBuilders.anArrayBuilder();
		
		for (Block block : blocksSpawn) {
			try {
				jsonBlocksSpawn.withElement(JsonNodeBuilders.aStringBuilder(RegisteredObjects.instance().getRegisterName (block)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jsonBlocksSpawn;
	}
	
	
	public boolean equals (BuildingConfigType obj) {
		return false;
	}

	@Override
	public boolean merge(Object newValueObj) {
		
		
		BuildingConfigType newValue = (BuildingConfigType)newValueObj;
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
