package mods.gollum.core.common.config;

import mods.gollum.core.ModGollumCoreLib;


public class ConfigGollumCoreLib extends Config<ConfigGollumCoreLib> {

	public ConfigGollumCoreLib () {
		this.setRelativePath(ModGollumCoreLib.MODID+"/");
	}
	
	@ConfigProp (info = "Log display level (DEBUG, INFO, MESSAGE, WARNING, SEVERE, NONE)")
	public String level = "MESSAGE";

	@ConfigProp public int numberLogFilesUse = 3;
	@ConfigProp public int test1 = 1;
	@ConfigProp public int test2 = 3;
	@ConfigProp public int test3 = 3;
	@ConfigProp public int test4 = 4;
	@ConfigProp public int test5 = 5;
	@ConfigProp public int test6 = 6;
	@ConfigProp public int test7 = 7;
	@ConfigProp public int test8 = 8;
	@ConfigProp public int test9 = 9;
	@ConfigProp public int testA = 00;
	@ConfigProp public int testB = 11;
	@ConfigProp public int testC = 22;
	@ConfigProp public int testD = 33;
	@ConfigProp public int testE = 44;
	@ConfigProp public int testF = 55;
	@ConfigProp public int testG = 66;
	@ConfigProp public int testY = 77;
	@ConfigProp public int testJ = 88;
	@ConfigProp public boolean devTools = false;
			
	@ConfigProp (info = "Display version checker message")
	public boolean versionChecker = true;
	
}
