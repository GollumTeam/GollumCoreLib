package com.gollum.core.common.building;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import argo.jdom.JdomParser;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.config.ConfigBuildings;
import com.gollum.core.common.config.ConfigLoader;
import com.gollum.core.common.config.type.BuildingConfigType;
import com.gollum.core.common.config.type.BuildingConfigType.Group;
import com.gollum.core.common.resources.ResourceLoader;
import com.gollum.core.tools.simplejson.Json;

public class ModBuildingParser {

	public static final String NAME_JSON = "buildings.json";
	public static final String DIR_BUILDING_ASSETS  = "buildings/";

	private JdomParser     parser         = new JdomParser();
	private ResourceLoader resourceLoader = new ResourceLoader();
	
	public BuildingConfigType parse(String modId) {
		
		if (!resourceLoader.assetExist(DIR_BUILDING_ASSETS+NAME_JSON, modId)) {
			ModGollumCoreLib.log.debug("No buildings Found : "+modId);
			return null;
		}
		
		try {
			
			InputStream isJson = resourceLoader.asset (DIR_BUILDING_ASSETS+NAME_JSON, modId);
			Json json  = Json.create (this.parser.parse(new InputStreamReader(isJson)));
			isJson.close();
			
			ConfigBuildings config = new ConfigBuildings(modId, new BuildingConfigType(json, modId));
			ConfigLoader.addSubConfig(config);
			
			try {
				config.loadConfig();
				
				
				for (Entry<String, Group> entry : config.buildings.lists.entrySet()) {
					String groupName = entry.getKey();
					Group group = entry.getValue();
					
					ModGollumCoreLib.log.debug("Final group building :"+groupName);
					
				}
				
				return config.buildings;
				
			} catch(Exception e) {
			}
			
			
			
		} catch (Exception e) {
			ModGollumCoreLib.log.severe("Erreur parsing buildings : "+modId);
			e.printStackTrace();
		}
		
		return null;
	}
}
