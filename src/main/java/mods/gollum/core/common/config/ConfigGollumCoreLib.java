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
	
	
	
	@ConfigProp(group="Test") public long    l1 = 1111;
	@ConfigProp(group="Test") public Long    l2 = 2222L;
	@ConfigProp(group="Test") public int     i1 = 111;
	@ConfigProp(group="Test") public Integer i2 = 222;
	@ConfigProp(group="Test") public short   s1 = 11;
	@ConfigProp(group="Test") public Short   s2 = 22;
	@ConfigProp(group="Test") public byte    b1 = 1;
	@ConfigProp(group="Test") public Byte    b2 = 2;
	@ConfigProp(group="Test") public float   f1 = 1.5F;
	@ConfigProp(group="Test") public Float   f2 = 2.5F;
	@ConfigProp(group="Test") public double  d1 = 1.3D;
	@ConfigProp(group="Test") public Double  d2 = 2.3D;
	@ConfigProp(group="Test") public boolean z1 = true;
	@ConfigProp(group="Test") public Boolean z2 = false;
	
	@ConfigProp(group="Test") public long   [] lAr1 = new long   [] { 1111 , 1111 , 1111  };
	@ConfigProp(group="Test") public Long   [] lAr2 = new Long   [] { 2222L, 2222L, 2222L };
	@ConfigProp(group="Test") public int    [] iAr1 = new int    [] { 111  , 111  , 111   };
	@ConfigProp(group="Test") public Integer[] iAr2 = new Integer[] { 222  , 222  , 222   };
	@ConfigProp(group="Test") public short  [] sAr1 = new short  [] { 11   , 11   , 11    };
	@ConfigProp(group="Test") public Short  [] sAr2 = new Short  [] { 22   , 22   , 22    };
	@ConfigProp(group="Test") public byte   [] bAr1 = new byte   [] { 1    , 1    , 1     };
	@ConfigProp(group="Test") public Byte   [] bAr2 = new Byte   [] { 2    , 2    , 2     };
	@ConfigProp(group="Test") public float  [] fAr1 = new float  [] { 1.5F , 1.5F , 1.5F  };
	@ConfigProp(group="Test") public Float  [] fAr2 = new Float  [] { 2.5F , 2.5F , 2.5F  };
	@ConfigProp(group="Test") public double [] dAr1 = new double [] { 1.3D , 1.3D , 1.3D  };
	@ConfigProp(group="Test") public Double [] dAr2 = new Double [] { 2.3D , 2.3D , 2.3D  };
	@ConfigProp(group="Test") public boolean[] zAr1 = new boolean[] { true , true , true  };
	@ConfigProp(group="Test") public Boolean[] zAr2 = new Boolean[] { false, false, false };
	
	
}
