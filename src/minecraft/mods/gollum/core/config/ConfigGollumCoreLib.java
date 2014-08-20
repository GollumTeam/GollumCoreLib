package mods.gollum.core.config;

import mods.gollum.core.ModGollumCoreLib;

public class ConfigGollumCoreLib extends Config {
	
	@ConfigProp (info = "Log display level (DEBUG, INFO, WARNING, SEVERE, NONE)")
	public static String level = "WARNING";
	public static int numberLogFilesUse = 3;
	
	@ConfigProp (info = "Display version checker message")
	public static boolean versionChecker = true;

	@ConfigProp(group = "Blocks Ids")
	public static int blockSpawnerID = 1243;
	
}
