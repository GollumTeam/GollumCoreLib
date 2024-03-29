package com.gollum.core.common.config;

import static com.gollum.core.ModGollumCoreLib.logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.config.type.ConfigJsonType;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.log.Logger;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.tools.simplejson.Json;

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

public class ConfigLoader {
	
	private static final String extention = ".cfg";
	private static final String configDir = "config";
	private static final JdomParser parser = new JdomParser();
	private static final JsonFormatter formatter = new PrettyJsonFormatter(); 
	
	private static boolean firstLoading = true;
	public static HashMap<GollumMod, ConfigLoad> configLoaded = new HashMap<GollumMod, ConfigLoad>();
	public static HashMap<GollumMod, ArrayList<ConfigLoad>> subConfigLoaded = new HashMap<GollumMod, ArrayList<ConfigLoad>>();
	
	private boolean updateFile = false;
	private File dir;
	private String fileName;
	private Config config;
	private LinkedList<Field> configFields;
	private HashSet<String> groupList;
	
	public static class ConfigLoad {
		
		public GollumMod mod;
		public Config configDefault;
		public Config config;
		
		public ConfigLoad (GollumMod mod, Config config) {
			this.mod = mod;
			this.configDefault = (Config)config.clone();
			this.config = config;
		}
		
		public ArrayList<String> getCategories() {
			
			ArrayList<String> categories = new ArrayList<String>();
			
			try {
				for (Field f : this.config.getClass().getDeclaredFields()) {
					f.setAccessible(true);
					ConfigProp anno = f.getAnnotation(ConfigProp.class);
					if (anno != null && (!anno.dev() || ModGollumCoreLib.config.devTools)) {
						
						String group = anno.group().trim();
						if (group.equals("")) {
							group = "General";
						}
						if (!categories.contains(group)) {
							categories.add(group);
						}
					}
				}
			} catch (Throwable e)  {
				e.printStackTrace();
			}
			
			return categories;
		}

		public void saveValue (LinkedHashMap<String, Object> values) {
			for (Field f : this.config.getClass().getDeclaredFields()) {
				String name = f.getName();
				if (values.containsKey(name)) {
					Object value = values.get(name);
					try {
						f.set(this.config, value);
						logger.info("Save config \""+name+"\""+value.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Constructeur
	 */
	public ConfigLoader (Config config) {
		this(config, true);
	}
	
	/**
	 * Constructeur
	 */
	public ConfigLoader (Config config, boolean register) {
		
		if (register) {
			GollumMod mod = ModContext.instance().getCurrent();
			if (config.isMain() && !this.configLoaded.containsKey (mod)) {
				this.configLoaded.put(mod, new ConfigLoad(mod, config));
			}
		}
		
		this.dir = new File (((File)(FMLInjectionData.data()[6])).getAbsoluteFile(), this.configDir);
		this.dir = new File (this.dir, config.getRelativePath());
		
		if (!this.dir.exists()) {
			this.dir.mkdir();
		}
		
		this.config       = config;
		this.configFields = new LinkedList<Field>();
		this.groupList    = new HashSet<String>();
		this.fileName     = config.getFileName ()+this.extention;
		
		// Parse la class pour chercher les propriété à save en config
		Field[] fields = this.config.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ConfigProp.class)) {
				
				this.configFields.add(field);
				ConfigProp prop = (ConfigProp) field.getAnnotation(ConfigProp.class);
				this.groupList.add(prop.group());
				
				Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_DEBUG, "Field : "+field.getName()+" found group="+prop.group());
				
			}
		}
	}

	public static void addSubConfig (Config config) {
		addSubConfig(ModContext.instance().getCurrent(), config);
	}

	public static void addSubConfig (GollumMod mod, Config config) {
		if (!subConfigLoaded.containsKey(mod)) {
			subConfigLoaded.put(mod, new ArrayList<ConfigLoad>());
		}
		subConfigLoaded.get(mod).add(new ConfigLoad(mod, config));
	}
	
	public static ArrayList<ConfigLoad> getSubConfig (GollumMod Mod) {
		for (Entry<GollumMod, ArrayList<ConfigLoad>> entry : subConfigLoaded.entrySet()) {
			if (entry.getKey().equals(Mod)) {
				return entry.getValue();
			}
		}
		return new ArrayList<ConfigLoad>();
	}


	/**
	 * Renvoie les props de chaque group
	 * @param groupName
	 * @return
	 */
	public LinkedList<Field> getFieldByGroup(String groupName) {
		
		LinkedList<Field> list = new LinkedList<Field>();
		
		for (Field field : this.configFields) {
			ConfigProp prop = (ConfigProp) field.getAnnotation(ConfigProp.class);
			if (prop.group().equals(groupName)) {
				list.add(field);
			}
		}
		return list;
	}
	
	/**
	 * Charge la config
	 */
	public void loadConfig() {
		
		Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_INFO, "Read config : "+this.config.getRelativePath()+"/"+this.fileName);
		
		try {
			
			File configFile = new File(this.dir, this.fileName);
			HashMap<String, Field> types = new HashMap<String, Field>();
			
			for (Field field : this.configFields) {
				ConfigProp prop = (ConfigProp) field.getAnnotation(ConfigProp.class);
				types.put(field.getName(), field);
			}
			
			HashMap<String, Field> properties;
			
			if (configFile.exists()) {
				
				properties = parseConfig(configFile, types);
				
				for (String prop : types.keySet()) {
					
					try {
						if (properties.containsKey(prop)) {
							
							Field field = (Field) types.get(prop);
							Object obj = properties.get(prop);
							
							field.setAccessible(true);
							
							if (!obj.equals(field.get(this.config))) {
								
								Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_DEBUG, "Set field : "+field.getName()+", obj="+obj+", objType="+obj.getClass().getName());
								
								field.set(this.config, this.mergeValue(field.get(this.config), obj));
							}
						} else {
							Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_WARNING, this.fileName+"Propery : "+prop+" Not found");
							this.updateFile = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						this.updateFile = true;
					}
				}
				
			} else {
				this.updateFile = true;
			}
			
		} catch (Exception e) {
			this.updateFile = true;
			e.printStackTrace();
			Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_SEVERE, e.getMessage());
		}
		
		if (this.updateFile) {
			writeConfig();
		}
		this.updateFile = false;
	}
	
	private Object mergeValue (Object oldValue, Object newValue) {
		if (oldValue instanceof IConfigMerge) {
			if(!((IConfigMerge)oldValue).merge (newValue)) {
				this.updateFile = true;
			}
			return oldValue;
		}
		return newValue;
	}
	
	private Object parseConvert (Class classType, String prop) {
		
		String jsonStr = "{\"root\":"+prop+"}";
		try {
			JsonRootNode root = this.parser.parse(jsonStr);
			return this.parseConvert(classType, root.getNode("root"));
		} catch (Exception e) {
			Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_SEVERE, "Erreur read config : file="+this.dir+"/"+this.fileName+", prop="+prop+", json parsed="+jsonStr);
			e.printStackTrace();
			if (Logger.getLevel() <= Logger.LEVEL_DEBUG) {
				
			}
		}
		return null;
	}
	
	private Object parseConvert (Class classType, JsonNode json) throws Exception {
		
		Object value = null;
		
		if (classType.isAssignableFrom(String.class)) {
			value = json.getText();
		} else if (
			classType.isAssignableFrom(Long.TYPE) ||
			classType.isAssignableFrom(Long.class)
		) { 
			value = Long.parseLong(json.getNumberValue());
		} else if (
			classType.isAssignableFrom(Integer.TYPE) ||
			classType.isAssignableFrom(Integer.class)
		) { 
			value = Integer.parseInt(json.getNumberValue());
		} else if (
			classType.isAssignableFrom(Short.TYPE) ||
			classType.isAssignableFrom(Short.class)
			) { 
			value = Short.parseShort(json.getNumberValue());
		} else if (
			classType.isAssignableFrom(Byte.TYPE) ||
			classType.isAssignableFrom(Byte.class)
			) { 
			value = Byte.parseByte(json.getNumberValue());
		} else if (
			classType.isAssignableFrom(Character.TYPE) ||
			classType.isAssignableFrom(Character.class)
			) { 
			value = (char)((Byte.parseByte(json.getNumberValue())) & 0x00FF);
		} else if (
			classType.isAssignableFrom(Double.TYPE) ||
			classType.isAssignableFrom(Double.class)
			) { 
			value = Double.parseDouble(json.getNumberValue());
		} else if (
			classType.isAssignableFrom(Float.TYPE) ||
			classType.isAssignableFrom(Float.class)
			) { 
			value = Float.parseFloat(json.getNumberValue());
		} else  if (
			classType.isAssignableFrom(Boolean.TYPE) ||
			classType.isAssignableFrom(Boolean.class)
			) { 
			value = json.getBooleanValue();
			
		} else if (ConfigJsonType.class.isAssignableFrom(classType)) { 
				value = classType.newInstance();
				((ConfigJsonType)value).readConfig(Json.create(json, (Class<? extends Json>)classType));
				
		} else if (Json.class.isAssignableFrom(classType)) { 
			value = Json.create(json, (Class<? extends Json>)classType);
			
		} else if (classType.isArray()) {
			
			ArrayList<Object> tmp = new ArrayList<Object>();
			Class subClass = classType.getComponentType();
			
			for (JsonNode el : json.getElements()) {
				Object subValue = this.parseConvert (subClass, el);
				if (subValue != null) {
					tmp.add(subValue);
				}
			}
			
			value = Array.newInstance(subClass, tmp.size());
			for (int i = 0; i < tmp.size(); i++) {
				Array.set(value, i, tmp.get(i));
			}
		}
		
		return value;
	}
	
	
	/**
	 * Lit le fichier de config
	 * @param file
	 * @param types
	 * @return
	 * @throws Exception
	 */
	private HashMap parseConfig(File file, HashMap types) throws Exception {
		
		HashMap config = new HashMap();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String strLine;
		String name = null;
		String valueStr = "";
		
		while ((strLine = reader.readLine()) != null) {
			if (strLine.length() == 0) {
				continue;
			}
			if (strLine.startsWith(" ") || strLine.startsWith("\t")) {
				valueStr += '\n'+strLine.toString();
				continue;
			}
			if (!strLine.startsWith("#")) {
				int index = strLine.indexOf("=");
				if (index <= 0) {
					this.updateFile = true;
				}
				if (name != null) {
					saveConfigField(types, config, name, valueStr);
				}

				name = strLine.substring(0, index).trim();
				valueStr = strLine.substring(index + 1);
			}
		}
		saveConfigField(types, config, name, valueStr);
		
		reader.close();
		return config;
	}

	private void saveConfigField(HashMap types, HashMap config, String name, String valueStr) {
		if (!types.containsKey(name)) {
			this.updateFile = true;
		} else {
			Class classType = ((Field) types.get(name)).getType();
			Object value = this.parseConvert(classType, valueStr);
			
			if (value != null) {
				config.put(name, value);
			}
		}
	}
	
	private String toJsonValue (Field field) throws Exception {
		
		Object value = field.get(this.config);
		
		JsonRootNode json = JsonNodeFactories.object(
			JsonNodeFactories.field("root", this.toJsonValue (value))
		);
		
		String out = this.formatter.format(json);
		out = out.substring(1).trim().substring("\"root\":".length()).trim();
		out = out.substring(0, out.length() - ("}".length()));
		
		return out;
	}
	
	private JsonNode toJsonValue(Object value) {
		
		JsonNode node = null;
		if (value != null ) {
			
			if (value instanceof String)    { node = JsonNodeFactories.string ((String)value);              } else
			if (value instanceof Long)      { node = JsonNodeFactories.number ((Long)value);                } else
			if (value instanceof Integer)   { node = JsonNodeFactories.number ((Integer)value);             } else
			if (value instanceof Short)     { node = JsonNodeFactories.number ((Short)value);               } else
			if (value instanceof Byte)      { node = JsonNodeFactories.number ((Byte)value);                } else
			if (value instanceof Character) { node = JsonNodeFactories.number ((Character)value);           } else
			if (value instanceof Double)    { node = JsonNodeFactories.number (((Double)value).toString()); } else
			if (value instanceof Float)     { node = JsonNodeFactories.number (((Float)value).toString());  } else
			if (value instanceof Boolean)   { node = JsonNodeFactories.booleanNode((Boolean)value);         } else
				
			if (value instanceof ConfigJsonType) { 
				
				node = ((ConfigJsonType)value).writeConfig().argoJson ().build();
				
			} else 
			if (value instanceof Json) { 
				
				node = ((Json)value).argoJson().build();
				
			} else 
			if (value.getClass().isArray()) {
				
				ArrayList<JsonNode> childs = new ArrayList<JsonNode>();
				for (int i = 0; i< Array.getLength(value); i++) {
					childs.add(this.toJsonValue (Array.get(value, i)));
				}
				node = JsonNodeFactories.lazyArray(childs);
			}
		}
		
		return node;
	}
	
	/**
	 * Met à jour la config
	 */
	public ConfigLoader writeConfig() {
		
		File file = new File(this.dir, this.fileName);
		
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			
			for (String groupName : this.groupList) {
				
				String title = groupName;
				if (groupName.equals("")) {
					title = "General";
				}
				for (int i = 0; i < title.length() + 4 ; i++) out.write("#");
				out.write(System.getProperty("line.separator") + "# " + title + " #" + System.getProperty("line.separator"));
				for (int i = 0; i < title.length() + 4 ; i++) out.write("#");
				out.write(System.getProperty("line.separator")+System.getProperty("line.separator"));
				
				for (Field field : this.getFieldByGroup(groupName)) {
					
					ConfigProp prop = (ConfigProp) field.getAnnotation(ConfigProp.class);
					if (prop.info().length() != 0) {
						out.write("#" + prop.info() + System.getProperty("line.separator"));
					}
					
					String name = field.getName();
					
					try {
						
						String value = this.toJsonValue(field);
						out.write(name + "=" + value + System.getProperty("line.separator"));
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				out.write(System.getProperty("line.separator"));
				out.write(System.getProperty("line.separator"));
			}
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this;
	}

}