package mods.gollum.core.common.config;

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
import java.util.LinkedList;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.log.Logger;
import argo.format.CompactJsonFormatter;
import argo.format.JsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.FMLInjectionData;

public class ConfigLoader {

	private static final String extention = ".cfg";
	private static final String configDir = "config";
	private static final JdomParser parser = new JdomParser();
	private static final JsonFormatter formatter = new CompactJsonFormatter(); 
	
	private boolean updateFile = false;
	private File dir;
	private String fileName;
	private Config config;
	private LinkedList<Field> configFields;
	private HashSet<String> groupList;
	
	/**
	 * Constructeur
	 * @param clss
	 * @param dir
	 * @param fileName
	 */
	public ConfigLoader (Config config) {
		
		this.dir = new File (((File)(FMLInjectionData.data()[6])).getAbsoluteFile(), this.configDir);
		
		if (!this.dir.exists()) {
			this.dir.mkdir();
		}
		
		this.config       = config;
		this.configFields = new LinkedList<Field>();
		this.groupList    = new HashSet<String>();
		this.fileName     = config.getFileName ()+this.extention;
		
		Field[] fields = this.config.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ConfigProp.class)) {
				
				this.configFields.add(field);
				ConfigProp prop = (ConfigProp) field.getAnnotation(ConfigProp.class);
				this.groupList.add(prop.group());
			}
		}
	}
	
	/**
	 * Renvoie les props de chaque group
	 * @param groupName
	 * @return
	 */
	public LinkedList<Field> getFieldByGroup (String groupName) {
		
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

		Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_INFO, "Read config : "+this.fileName);
		
		try {
			
			File configFile = new File(this.dir, this.fileName);
			HashMap<String, Field> types = new HashMap<String, Field>();
			
			for (Field field : this.configFields) {
				ConfigProp prop = (ConfigProp) field.getAnnotation(ConfigProp.class);
				types.put( ((!prop.name().isEmpty()) ? prop.name() : field.getName()), field);
			}
			
			HashMap<String, Field> properties;
			
			if (configFile.exists()) {
				
				properties = parseConfig(configFile, types);
				
				for (String prop : properties.keySet()) {
					

					try {
						
						Field field = (Field) types.get(prop);
						Object obj = properties.get(prop);
						
						field.setAccessible(true);
						
						if (!obj.equals(field.get(null))) {
							
							Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_DEBUG, "Set field : "+field.getName()+", obj="+obj+", objType="+obj.getClass().getName());
							field.set(null, obj);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				for (String type : types.keySet()) {
					if (!properties.containsKey(type)) {
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
			updateConfig();
		}
		this.updateFile = false;
	}

	private Object parseConvert (Class classType, String prop) {
		
		String jsonStr = "{\"root\":"+prop+"}";
		try {
			JsonRootNode root = this.parser.parse(jsonStr);
			return this.parseConvert(classType, root.getNode("root"));
		} catch (Exception e) {
			Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_SEVERE, "Erreur read config : file="+this.fileName+", prop="+prop+", json parsed="+jsonStr);
			if (Logger.getLevel() <= Logger.LEVEL_DEBUG) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private Object parseConvert (Class classType, JsonNode json) throws Exception {
		
		Object value = null;
		
		if (classType.isAssignableFrom(String.class))       { value = json.getText();                            } else
		if (classType.isAssignableFrom(Long.TYPE))          { value = Long.parseLong(json.getNumberValue());     } else
		if (classType.isAssignableFrom(Integer.TYPE))       { value = Integer.parseInt(json.getNumberValue());   } else
		if (classType.isAssignableFrom(Short.TYPE))         { value = Short.parseShort(json.getNumberValue());   } else
		if (classType.isAssignableFrom(Byte.TYPE))          { value = Byte.parseByte(json.getNumberValue());     } else
		if (classType.isAssignableFrom(Double.TYPE))        { value = Double.parseDouble(json.getNumberValue()); } else
		if (classType.isAssignableFrom(Float.TYPE))         { value = Float.parseFloat(json.getNumberValue());   } else
		if (classType.isAssignableFrom(Boolean.TYPE))       { value = json.getBooleanValue();                    } else
		
		if (IConfigJsonClass.class.isAssignableFrom(classType)) { 
			value = classType.newInstance();
			((IConfigJsonClass)value).readConfig(json);
			
		} else if (IConfigClass.class.isAssignableFrom(classType)) { 
			value = classType.newInstance();
			((IConfigClass)value).readConfig(json.getText());
			
		} else if (classType.isArray()) {
			
			ArrayList<Object> tmp = new ArrayList<Object>();
			Class subClass = classType.getComponentType();
			
			for (JsonNode el : json.getElements()) {
				Object subValue = this.parseConvert (subClass, el);
				if (subValue != null) {
					tmp.add(subValue);
				}
			}
			
			Object[] table = (Object[]) Array.newInstance(subClass, tmp.size());
			for (int i = 0; i < tmp.size(); i++) {
				table[i] = tmp.get(i);
			}
			
			value = table;
			
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
		
		while ((strLine = reader.readLine()) != null) {
			
			if ((!strLine.startsWith("#")) && (strLine.length() != 0)) {
				
				int index = strLine.indexOf("=");
				
				if ((index <= 0) || (index == strLine.length())) {
					
					this.updateFile = true;
					
				} else {
					
					String name = strLine.substring(0, index).trim();
					String prop = strLine.substring(index + 1).trim();
					
					if (!types.containsKey(name)) {
						this.updateFile = true;
						
					} else {
						Class classType = ((Field) types.get(name)).getType();
						Object value = this.parseConvert(classType, prop);
						
						if (value != null) {
							Logger.log(ModGollumCoreLib.MODID, Logger.LEVEL_DEBUG, "Read "+this.fileName+" : "+name+":"+value);
							config.put(name, value);
						}
					}
				}
			}
		}
		
		reader.close();
		return config;
	}
	
	private String toJsonValue (Field field) throws Exception {
		
		JsonRootNode json = JsonNodeFactories.object(
			JsonNodeFactories.field("root", this.toJsonValue (field.get(null)))
		);
		
		String out = this.formatter.format(json).substring("{\"root\":".length());
		out = out.substring(0, out.length() - ("}".length()));
		return out;
	}
	
	private JsonNode toJsonValue(Object value) {
		
		JsonNode node = null;
		
		if (value instanceof String)       { node = JsonNodeFactories.string ((String)value);              } else
		if (value instanceof Long)         { node = JsonNodeFactories.number ((Long)value);                } else
		if (value instanceof Integer)      { node = JsonNodeFactories.number ((Integer)value);             } else
		if (value instanceof Short)        { node = JsonNodeFactories.number ((Short)value);               } else
		if (value instanceof Byte)         { node = JsonNodeFactories.number ((Byte)value);                } else
		if (value instanceof Double)       { node = JsonNodeFactories.number (((Double)value).toString()); } else
		if (value instanceof Float)        { node = JsonNodeFactories.number (((Float)value).toString());  } else
		if (value instanceof Boolean)      { node = JsonNodeFactories.booleanNode((Boolean)value);         } else
		
		if (value instanceof IConfigJsonClass) { 
			
			node = ((IConfigJsonClass)value).writeConfig();
			
		} else if (value instanceof IConfigClass) { 
			
			String strValue = ((IConfigClass)value).writeConfig();
			node = JsonNodeFactories.string(strValue);
			
		} else if (value instanceof Object[]) {
			
			ArrayList<JsonNode> childs = new ArrayList<JsonNode>();
			for (Object subValue: (Object[])value) {
				childs.add(this.toJsonValue (subValue));
			}
			node = JsonNodeFactories.lazyArray(childs);
		}
		
		return node;
	}

	/**
	 * Met à jour la config
	 */
	public void updateConfig() {
		
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
					
					String name = !prop.name().isEmpty() ? prop.name() : field.getName();
					
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
	}

}