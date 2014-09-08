package mods.gollum.core.common.building;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.config.ConfigBuildings;
import mods.gollum.core.common.config.container.BuildingConfigType;
import mods.gollum.core.common.config.container.BuildingConfigType.Group;
import mods.gollum.core.common.resource.ResourceLoader;
import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;

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
			JsonRootNode json  = this.parser.parse(new InputStreamReader(isJson));
			isJson.close();
			
			ConfigBuildings config = new ConfigBuildings(modId, new BuildingConfigType(json, modId));
			
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
