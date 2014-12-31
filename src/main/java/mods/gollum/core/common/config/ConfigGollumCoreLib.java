package mods.gollum.core.common.config;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.config.ConfigProp.Type;
import mods.gollum.core.common.config.type.ItemStackConfigType;
import mods.gollum.core.tools.simplejson.Json;


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
	
	
	//////////////////
	// Test exemple //
	//////////////////
	
	
//	// JsonObject
//	@ConfigProp(group="Test")
//	public Json json1 = Json.create(
//		new Json.EntryObject("field1", Json.create("Test")),
//		new Json.EntryObject("field2", Json.create(1)),
//		new Json.EntryObject("field3", Json.create(1.5))
//	);
//
//	// JsonArray
//	@ConfigProp(group="Test")
//	public Json json2 = Json.create(
//		Json.create("Test1"),
//		Json.create("Test2"),
//		Json.create("Test3")
//	);
//	
//	@ConfigProp(group="Test")
//	public Json json3 = Json.create(
//		Json.create(1),
//		Json.create(2),
//		Json.create(3)
//	);
//	
//	
//	@ConfigProp(group="Test", type=Type.SLIDER)
//	public byte slider = 20;
//	
//	@ConfigProp(group="Test", type=Type.ITEM)
//	public String item = "minecraft:iron_axe";
//	
//	@ConfigProp(group="Test", type=Type.BLOCK)
//	public String block = "minecraft:tnt";
//	
//	@ConfigProp(group="Test", type=Type.MOD)
//	public String mod = ModGollumCoreLib.MODID;
//
//	@ConfigProp(group="Test") public  ItemStackConfigType itemStack1 = new ItemStackConfigType("minecraft:planks", 10, 3);
//	@ConfigProp(group="Test") public  ItemStackConfigType[] itemStackAr1 = new ItemStackConfigType[] { new ItemStackConfigType("minecraft:planks", 10, 3), new ItemStackConfigType("minecraft:planks", 10, 3) };
//	
	
	@ConfigProp(group="Test", show=false)
	public String hide = "Value is Hide";
	
	@ConfigProp(group="Test", minValue="7", maxValue="10")
	public String maxSize = "ABCDEFGHIJ";
	
	@ConfigProp(group="Test") public String    s1 = "AAA";
	@ConfigProp(group="Test") public long      l1 = 11111;
	@ConfigProp(group="Test") public Long      l2 = 22223L;
	@ConfigProp(group="Test") public int       i1 = 1111;
	@ConfigProp(group="Test") public Integer   i2 = 2222;
	@ConfigProp(group="Test") public short     w1 = 111;
	@ConfigProp(group="Test") public Short     w2 = 222;
	@ConfigProp(group="Test") public byte      b1 = 11;
	@ConfigProp(group="Test") public Byte      b2 = 22;
	@ConfigProp(group="Test") public float     f1 = 1.5F;
	@ConfigProp(group="Test") public Float     f2 = 2.5F;
	@ConfigProp(group="Test") public double    d1 = 1.3D;
	@ConfigProp(group="Test") public Double    d2 = 2.3D;
	@ConfigProp(group="Test") public boolean   z1 = true;
	@ConfigProp(group="Test") public Boolean   z2 = false;
	
	@ConfigProp(group="Test") public String   [] sAr1 = new String   [] { "AAA" , "BBB" , "CCC"  };
	@ConfigProp(group="Test") public long     [] lAr1 = new long     [] { 11111 , 11111 , 11111  };
	@ConfigProp(group="Test") public Long     [] lAr2 = new Long     [] { 22222L, 22222L, 22222L };
	@ConfigProp(group="Test") public int      [] iAr1 = new int      [] { 1111  , 1111  , 1111   };
	@ConfigProp(group="Test") public Integer  [] iAr2 = new Integer  [] { 2222  , 222   , 2222   };
	@ConfigProp(group="Test") public short    [] wAr1 = new short    [] { 111   , 111   , 111    };
	@ConfigProp(group="Test") public Short    [] wAr2 = new Short    [] { 222   , 222   , 222    };
	@ConfigProp(group="Test") public byte     [] bAr1 = new byte     [] { 11    , 11    , 11     };
	@ConfigProp(group="Test") public Byte     [] bAr2 = new Byte     [] { 22    , 22    , 22     };
	@ConfigProp(group="Test") public char     [] cAr1 = new char     [] { 1     , 1     , 1      };
	@ConfigProp(group="Test") public Character[] cAr2 = new Character[] { 2     , 2     , 2      };
	@ConfigProp(group="Test") public float    [] fAr1 = new float    [] { 1.5F  , 1.5F  , 1.5F   };
	@ConfigProp(group="Test") public Float    [] fAr2 = new Float    [] { 2.5F  , 2.5F  , 2.5F   };
	@ConfigProp(group="Test") public double   [] dAr1 = new double   [] { 1.3D  , 1.3D  , 1.3D   };
	@ConfigProp(group="Test") public Double   [] dAr2 = new Double   [] { 2.3D  , 2.3D  , 2.3D   };
	@ConfigProp(group="Test") public boolean  [] zAr1 = new boolean  [] { true  , true  , true   };
	@ConfigProp(group="Test") public Boolean  [] zAr2 = new Boolean  [] { false , false , false  };
	
//	@ConfigProp(
//		group="Test",
//		maxListLength = "5"
//	) 
//	public String [] sAr2 = new String [] { "AAA" , "BBB" , "CCC" };
//	
//	@ConfigProp(
//		group="Test",
//		isListLengthFixed = true,
//		pattern="\\d*"
//	) 
//	public String [] sAr3 = new String [] { "123" , "465" , "789" };
//	
//	@ConfigProp(
//		group="Test",
//		pattern="\\d*"
//	) 
//	public String s2 = "123";
}
