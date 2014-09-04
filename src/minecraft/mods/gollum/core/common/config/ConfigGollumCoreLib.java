package mods.gollum.core.common.config;

import mods.gollum.core.ModGollumCoreLib;


public class ConfigGollumCoreLib extends Config<ConfigGollumCoreLib> {

	public ConfigGollumCoreLib () {
		this.setRelativePath(ModGollumCoreLib.MODID+"/");
	}
	
	@ConfigProp (info = "Log display level (DEBUG, INFO, WARNING, SEVERE, NONE)")
	public String level = "WARNING";
	
	@ConfigProp public int numberLogFilesUse = 3;
	@ConfigProp public boolean devTools = false;
			
	@ConfigProp (info = "Display version checker message")
	public boolean versionChecker = true;

	@ConfigProp(group = "Blocks Ids")
	public int blockSpawnerID = 1243;
	
	@ConfigProp(group = "Items Ids")
	public int itemKeyID = 5600;
	
}
