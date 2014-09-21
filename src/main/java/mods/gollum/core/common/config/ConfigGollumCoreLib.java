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
	
	

	@ConfigProp(group="Test") public int     int1 = 1;
	@ConfigProp(group="Test") public Integer int2 = 2;
	@ConfigProp(group="Test") public float   f3   = 3.5F;
	@ConfigProp(group="Test") public Float   f4   = 4.5F;
	@ConfigProp(group="Test") public double  d5   = 5.3D;
	@ConfigProp(group="Test") public Double  d6   = 6.3D;
	@ConfigProp(group="Test") public boolean b7   = true;
	@ConfigProp(group="Test") public Boolean b8   = false;
	@ConfigProp(group="Test") public int[]     arInt1 = new int[]    { 1    , 1    , 1     };
	@ConfigProp(group="Test") public Integer[] arInt2 = new Integer[]{ 2    , 2    , 2     };
	@ConfigProp(group="Test") public float[]   arF3   = new float[]  { 3.5F , 3.5F , 3.5F  };
	@ConfigProp(group="Test") public Float[]   arF4   = new Float[]  { 4.5F , 4.5F , 4.5F  };
	@ConfigProp(group="Test") public double[]  arD5   = new double[] { 5.3D , 5.3D , 5.3D  };
	@ConfigProp(group="Test") public Double[]  arD6   = new Double[] { 6.3D , 6.3D , 6.3D  };
	@ConfigProp(group="Test") public boolean[] arB7   = new boolean[]{ true , true , true  };
	@ConfigProp(group="Test") public Boolean[] arB8   = new Boolean[]{ false, false, false };
	
	
}
