package mods.gollum.core.common.config;

import mods.gollum.core.ModGollumCoreLib;


public class ConfigGollumCoreLib extends Config {

	public ConfigGollumCoreLib () {
		this.setRelativePath(ModGollumCoreLib.MODID+"/");
	}
	
	@ConfigProp (
		info = "Log display level (DEBUG, INFO, MESSAGE, WARNING, SEVERE, NONE)",
		validValues = { "DEBUG", "INFO", "MESSAGE", "WARNING", "SEVERE", "NONE" },
		mcRestart = true
	)
	public String level = "MESSAGE";

	@ConfigProp(minValue = "0", mcRestart = true)
	public int numberLogFilesUse = 3;
	
	@ConfigProp(mcRestart = true) 
	public boolean devTools = false;
			
	@ConfigProp (info = "Display version checker message")
	public boolean versionChecker = true;
	
}
