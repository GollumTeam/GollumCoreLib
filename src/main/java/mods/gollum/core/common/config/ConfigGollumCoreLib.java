package mods.gollum.core.common.config;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.config.ConfigProp.Type;
import mods.gollum.core.common.config.type.ItemStackConfigType;
import mods.gollum.core.tools.simplejson.IJsonObjectDisplay;
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
	
	//CustomEntry
	@ConfigProp(group="Test", dev=true, entryClass="mods.gollum.core.client.gui.config.entry.ModIdEntry")
	public String customEntry = ModGollumCoreLib.MODID;
			
	// JsonObject
	@ConfigProp(group="Test", dev=true)
	public Json json1 = Json.create(
		new Json.EntryObject("field1", Json.create("Test")),
		new Json.EntryObject("field2", Json.create(1)),
		new Json.EntryObject("field3", Json.create(1.5))
	);

	// JsonArray
	@ConfigProp(group="Test", dev=true)
	public Json json2 = Json.create(
		Json.create("Test1"),
		Json.create("Test2"),
		Json.create("Test3")
	);

	@ConfigProp(group="Test", dev=true)
	public Json json3 = Json.create(
		Json.create(1),
		Json.create(2),
		Json.create(3)
	);
	

	@ConfigProp(group="Test", dev=true)
	public Json jsonDisplay = Json.create(
		new Json.EntryObject("field1", Json.create("Test")),
		new Json.EntryObject("field2", Json.create(1)),
		new Json.EntryObject("field3", Json.create(1.5))
	).addComplement(new IJsonObjectDisplay() {
		
		@Override
		public String display(Json json) {
			return json.child("field1").strValue()+" / "+json.child("field2")+" / "+json.child("field3");
		}
	});
	
	
	// Json
	@ConfigProp(group="Test", dev=true) public Json jsonStr    = Json.create("Test");
	@ConfigProp(group="Test", dev=true) public Json jsonLong   = Json.create(10L);
	@ConfigProp(group="Test", dev=true) public Json jsonInt    = Json.create((int)10);
	@ConfigProp(group="Test", dev=true) public Json jsonShort  = Json.create((short)10);
	@ConfigProp(group="Test", dev=true) public Json jsonByte   = Json.create((byte)10);
	@ConfigProp(group="Test", dev=true) public Json jsonDouble = Json.create(10.5D);
	@ConfigProp(group="Test", dev=true) public Json jsonFloat  = Json.create(10.5F);
	
	@ConfigProp(group="Test", dev=true, type=Type.SLIDER)
	public byte slider = 20;
	
	@ConfigProp(group="Test", dev=true, type=Type.SLIDER)
	public int[] ArSlider = new int[] { 20, 40 };
	
	@ConfigProp(group="Test", dev=true, type=Type.SLIDER)
	public double slider2 = 20.5;
	
	@ConfigProp(group="Test", dev=true, type=Type.ITEM)
	public String item = "minecraft:iron_axe";
	
	@ConfigProp(group="Test", dev=true, type=Type.BLOCK)
	public String block = "minecraft:tnt";
	
	@ConfigProp(group="Test", dev=true, type=Type.MOD)
	public String mod = ModGollumCoreLib.MODID;
	
	@ConfigProp(group="Test", dev=true, type=Type.MOD, newValue=ModGollumCoreLib.MODID)
	public String arMod[] = new String[] { ModGollumCoreLib.MODID };
	
	@ConfigProp(group="Test", dev=true) public ItemStackConfigType itemStack1     = new ItemStackConfigType("minecraft:planks", 10, 3);
	@ConfigProp(group="Test", dev=true) public ItemStackConfigType[] itemStackAr1 = new ItemStackConfigType[] { new ItemStackConfigType("minecraft:planks", 10, 3), new ItemStackConfigType("minecraft:planks", 10, 3) };
	
	
	@ConfigProp(group="Test", dev=true, worldRestart=true)
	public String worldRestart = "worldRestart";
	
	@ConfigProp(group="Test", dev=true, show=false)
	public String hide = "Value is Hide";
	
	@ConfigProp(group="Test", dev=true, minValue="7", maxValue="10")
	public String maxSize = "ABCDEFGHIJ";
	
	@ConfigProp(group="Test", dev=true) public String    s1 = "AAA";
	@ConfigProp(group="Test", dev=true) public long      l1 = 11111;
	@ConfigProp(group="Test", dev=true) public Long      l2 = 22223L;
	@ConfigProp(group="Test", dev=true) public int       i1 = 1111;
	@ConfigProp(group="Test", dev=true) public Integer   i2 = 2222;
	@ConfigProp(group="Test", dev=true) public short     w1 = 111;
	@ConfigProp(group="Test", dev=true) public Short     w2 = 222;
	@ConfigProp(group="Test", dev=true) public byte      b1 = 11;
	@ConfigProp(group="Test", dev=true) public Byte      b2 = 22;
	@ConfigProp(group="Test", dev=true) public float     f1 = 1.5F;
	@ConfigProp(group="Test", dev=true) public Float     f2 = 2.5F;
	@ConfigProp(group="Test", dev=true) public double    d1 = 1.3D;
	@ConfigProp(group="Test", dev=true) public Double    d2 = 2.3D;
	@ConfigProp(group="Test", dev=true) public boolean   z1 = true;
	@ConfigProp(group="Test", dev=true) public Boolean   z2 = false;
	
	@ConfigProp(group="Test", dev=true) public String   [] sAr1 = new String   [] { "AAA" , "BBB" , "CCC"  };
	@ConfigProp(group="Test", dev=true) public long     [] lAr1 = new long     [] { 11111 , 11111 , 11111  };
	@ConfigProp(group="Test", dev=true) public Long     [] lAr2 = new Long     [] { 22222L, 22222L, 22222L };
	@ConfigProp(group="Test", dev=true) public int      [] iAr1 = new int      [] { 1111  , 1111  , 1111   };
	@ConfigProp(group="Test", dev=true) public Integer  [] iAr2 = new Integer  [] { 2222  , 222   , 2222   };
	@ConfigProp(group="Test", dev=true) public short    [] wAr1 = new short    [] { 111   , 111   , 111    };
	@ConfigProp(group="Test", dev=true) public Short    [] wAr2 = new Short    [] { 222   , 222   , 222    };
	@ConfigProp(group="Test", dev=true) public byte     [] bAr1 = new byte     [] { 11    , 11    , 11     };
	@ConfigProp(group="Test", dev=true) public Byte     [] bAr2 = new Byte     [] { 22    , 22    , 22     };
	@ConfigProp(group="Test", dev=true) public float    [] fAr1 = new float    [] { 1.5F  , 1.5F  , 1.5F   };
	@ConfigProp(group="Test", dev=true) public Float    [] fAr2 = new Float    [] { 2.5F  , 2.5F  , 2.5F   };
	@ConfigProp(group="Test", dev=true) public double   [] dAr1 = new double   [] { 1.3D  , 1.3D  , 1.3D   };
	@ConfigProp(group="Test", dev=true) public Double   [] dAr2 = new Double   [] { 2.3D  , 2.3D  , 2.3D   };
	@ConfigProp(group="Test", dev=true) public boolean  [] zAr1 = new boolean  [] { true  , true  , true   };
	@ConfigProp(group="Test", dev=true) public Boolean  [] zAr2 = new Boolean  [] { false , false , false  };

	@ConfigProp(group="Test", dev=true, newValue="ABCDEFG")     public String[] arNewValue      = new String[] { "newValue"  , "newValue"  , "newValue"   };
	@ConfigProp(group="Test", dev=true, newValue="123")         public int[]    arNewValueInt   = new int[]    { 11          , 22          , 33           };
	@ConfigProp(group="Test", dev=true, newValue="12.34")       public float[]  arNewValueFloat = new float[]  { 11.34F      , 22.34F      , 33.34F       };
	@ConfigProp(group="Test", dev=true, isListLengthFixed=true) public String[] arFixed         = new String[] { "arFixed"   , "arFixed"   , "arFixed"    };
	@ConfigProp(group="Test", dev=true, minListLength="1")      public String[] arMinFixed      = new String[] { "arMinFixed", "arMinFixed", "arMinFixed" };
	@ConfigProp(group="Test", dev=true, maxListLength="5")      public String[] arMaxFixed     = new String[] { "arMaxFixed" , "arMaxFixed", "arMaxFixed" };
	@ConfigProp(group="Test", dev=true, minListLength="1", maxListLength="5")
	public String[] arMinMaxFixed = new String[] { "arMinMaxFixed" , "arMinMaxFixed" , "arMinMaxFixed" };
	
	
	@ConfigProp(
		group="Test", 
		dev=true,
		isListLengthFixed = true,
		pattern="\\d*"
	) 
	public String [] sArPattern = new String [] { "123" , "465" , "789" };
	
	@ConfigProp(
		group="Test",
		dev=true,
		pattern="\\d*"
	) 
	public String sPattern = "123";
}
