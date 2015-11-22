package com.gollum.core.common.config.type;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.config.IConfigMerge;
import com.gollum.core.common.config.type.BuildingConfigType.Group.Building;
import com.gollum.core.common.config.type.BuildingConfigType.Group.Building.Dimention;
import com.gollum.core.tools.registered.RegisteredObjects;
import com.gollum.core.tools.simplejson.Json;
import com.gollum.core.tools.simplejson.Json.EntryObject;
import com.gollum.core.tools.simplejson.JsonArray;
import com.gollum.core.tools.simplejson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

public class BuildingConfigType extends ConfigJsonType implements IConfigMerge {
	
	public static class Group {
		
		public Integer globalSpawnRate = null;
		public TreeMap<String, Building> buildings = new TreeMap<String, Building>();
		
		
		public static class Building {

			public boolean enabled = true;
			public TreeMap<Integer, Dimention> dimentions = new TreeMap<Integer, Dimention>();
			
			public static class Dimention {
				
				public Integer spawnRate = 1;
				public Integer spawnMin = 3;
				public Integer spawnMax = 256;
				public ArrayList<Block> blocksSpawn = new ArrayList<Block>();
				public ArrayList<BiomeGenBase> biomes = new ArrayList<BiomeGenBase>();
				
			}
		}
		
	}
	
	public String modId = null;
	public TreeMap<String, Group> lists = new TreeMap<String, BuildingConfigType.Group>();
	
	public BuildingConfigType () {
	}
	
	public BuildingConfigType (Json json, String modId) throws Exception {
		this.modId = modId;
		this.readConfig(json);
	}
	
	//////////
	// Read //
	//////////
	
	@Override
	public void readConfig(Json json) {
		
		for (Entry<String, Json> entry : json.allChildWithKey()) {
			String groupName = entry.getKey();
			Json   jsonGroup = entry.getValue();
			
			Group group = new Group();
			group.globalSpawnRate = jsonGroup.child("globalSpawnRate").intValue();
			group.buildings       = this.readBuildings (jsonGroup.child("buildings"));
			
			lists.put(groupName, group);
		}
		
	}
	
	private TreeMap<String, Building> readBuildings(Json json) {
		
		TreeMap<String, Building> rtn = new TreeMap<String, Building>();
		
		for (Entry<String, Json> entryBuilding : json.allChildWithKey()) {
			String buildingName = entryBuilding.getKey();
			Json   jsonBuilding = entryBuilding.getValue();
			
			Building building   = new Building ();
			building.enabled    = jsonBuilding.child("enabled").boolValue();
			building.dimentions = this.readDimentions (jsonBuilding.child("dimentions"));
			
			rtn.put(buildingName, building);
		}
		
		return rtn;
	}
	
	private TreeMap<Integer, Dimention> readDimentions(Json json) {
		
		TreeMap<Integer, Dimention> rtn = new TreeMap<Integer, Dimention> ();
		
		for (Entry<String, Json> entryDimention : json.allChildWithKey()) {
			
			Integer id            = Integer.parseInt(entryDimention.getKey());
			Json    jsonDimention = entryDimention.getValue();
			
			Dimention dimention = new Dimention();
			dimention.spawnRate   = jsonDimention.child("spawnRate").intValue();
			dimention.spawnMin    = jsonDimention.child("spawnMin").intValue();
			dimention.spawnMax    = jsonDimention.child("spawnMax").intValue();
			
			for (Json jsonBlock : jsonDimention.child("blocksSpawn").allChild()) {
				String key = jsonBlock.strValue();
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
			
			for (Json jsonBiome : jsonDimention.child("biomesFilter").allChild()) {
				String key = jsonBiome.strValue();
				try {
					BiomeGenBase b = RegisteredObjects.instance().getBiome(key);
					
					if (b != null) {
						dimention.biomes.add(b);
					} else {
						ModGollumCoreLib.log.severe("Error biome not found : "+key);
					}
				} catch (Exception e) {
					ModGollumCoreLib.log.severe("Error biome not found : "+key);
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
	public Json writeConfig() {
		Json builder = new JsonObject();
		

		for (Entry<String, Group> entry : this.lists.entrySet()) {
			String groupName = entry.getKey();
			Group group      = entry.getValue();
			
			Json jsonGroup = Json.create (
				new EntryObject ("globalSpawnRate", Json.create(group.globalSpawnRate)),
				new EntryObject ("buildings"      , this.getJsonBuildings (group.buildings))
			);
			
			builder.add(groupName, jsonGroup);
		}
		
		return builder;
	}
	
	private Json getJsonBuildings(TreeMap<String, Building> buildings) {
		
		Json jsonBuildings = new JsonObject();
		
		for (Entry<String, Building> entryBuilding : buildings.entrySet()) {
			
			String   buildingName = entryBuilding.getKey();
			Building building     = entryBuilding.getValue();
			
			Json jsonBuilding = Json.create (
				new EntryObject ("enabled"  , Json.create(building.enabled)),
				new EntryObject ("dimentions", this.getJsonDimentions (building.dimentions))
			);
			
			jsonBuildings.add(buildingName, jsonBuilding);
		}
		
		return jsonBuildings;
	}
	
	private Json getJsonDimentions(TreeMap<Integer, Dimention> dimentions) {

		Json jsonDimentions = new JsonObject();

		for (Entry<Integer, Dimention> entryDimentions : dimentions.entrySet()) {
			Integer dimentionId = entryDimentions.getKey();
			Dimention dimention = entryDimentions.getValue();
			
			Json jsonDimention = Json.create (
				new EntryObject ("spawnMin"   , Json.create(dimention.spawnMin)),
				new EntryObject ("spawnMax"   , Json.create(dimention.spawnMax)),
				new EntryObject ("spawnRate"  , Json.create(dimention.spawnRate)),
				new EntryObject ("blocksSpawn", this.getJsonBlocks (dimention.blocksSpawn)),
				new EntryObject ("biomesFilter", this.getJsonBiomes (dimention.biomes))
			);
			
			jsonDimentions.add(dimentionId.toString(), jsonDimention);
		}
		
		return jsonDimentions;
	}

	private Json getJsonBlocks(ArrayList<Block> blocksSpawn) {
		Json jsonBlocksSpawn = new JsonArray();
		
		for (Block block : blocksSpawn) {
			try {
				jsonBlocksSpawn.add( Json.create(RegisteredObjects.instance().getRegisterName (block)) );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jsonBlocksSpawn;
	}
	
	private Json getJsonBiomes(ArrayList<BiomeGenBase> biomes) {
		Json jsonBiomes = new JsonArray();
		
		for (BiomeGenBase biome : biomes) {
			try {
				jsonBiomes.add(Json.create(biome.biomeName) );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return jsonBiomes;
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
	
	@Override
	public Object clone() {
		BuildingConfigType cloned = (BuildingConfigType)super.clone();
		cloned.modId = this.modId;
		return cloned;
	}
	
}
