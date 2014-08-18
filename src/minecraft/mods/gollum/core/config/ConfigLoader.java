package mods.gollum.core.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import mods.gollum.core.ModGollumCoreLib;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigLoader {
	
	private boolean updateFile = false;
	private File dir;
	private String fileName;
	private Class configClass;
	private LinkedList<Field> configFields;
	private HashSet<String> groupList;
	
	/**
	 * Constructeur
	 * @param clss
	 * @param dir
	 * @param fileName
	 */
	public ConfigLoader (Class clss, FMLPreInitializationEvent event) {
		
		this.dir = event.getModConfigurationDirectory();
		
		if (!this.dir.exists()) {
			this.dir.mkdir();
		}
		
		this.configClass  = clss;
		this.configFields = new LinkedList<Field>();
		this.groupList    = new HashSet<String>();
		this.fileName     = event.getSuggestedConfigurationFile().getName();
		
		Field[] fields = this.configClass.getDeclaredFields();
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
					Field field = (Field) types.get(prop);
					Object obj = properties.get(prop);
					if (!obj.equals(field.get(null))) {
						field.set(null, obj);
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
			System.err.println(e.getMessage());
		}
		if (this.updateFile) {
			updateConfig();
		}
		this.updateFile = false;
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
						Object obj = null;
						Class classType = ((Field) types.get(name)).getType();
						
						if (classType.isAssignableFrom(String.class)) { obj = prop;                                        } else
						if (classType.isAssignableFrom(Integer.TYPE)) { obj = Integer.valueOf(Integer.parseInt(prop));     } else
						if (classType.isAssignableFrom(Short.TYPE))   { obj = Short.valueOf(Short.parseShort(prop));       } else
						if (classType.isAssignableFrom(Byte.TYPE))    { obj = Byte.valueOf(Byte.parseByte(prop));          } else
						if (classType.isAssignableFrom(Boolean.TYPE)) { obj = Boolean.valueOf(Boolean.parseBoolean(prop)); } else
						if (classType.isAssignableFrom(Float.TYPE))   { obj = Float.valueOf(Float.parseFloat(prop));       } else
						if (classType.isAssignableFrom(IConfigClass.class)) { 
							try {
								obj = classType.newInstance();
								((IConfigClass)obj).readConfig(prop);
							} catch (Exception e) {
								obj = null;
							}
							
						} else {
							
							if (classType.isAssignableFrom(IConfigClass[].class))  {
								ArrayList<IConfigClass> tmp = new ArrayList<IConfigClass>();
								String [] configs = prop.split(",");
								try {
									for (String value : configs) {
										obj = classType.newInstance();
										((IConfigClass)obj).readConfig(prop);
										tmp.add ((IConfigClass) obj);
									}
									
									ItemStackConfig[] table = new ItemStackConfig [tmp.size()];
									for (int i = 0; i < tmp.size(); i++) {
										table[i] = (ItemStackConfig) tmp.get(i);
									}
									obj = table;
								} catch (Exception e) {
									ModGollumCoreLib.log.severe ("Erreur read config : "+prop);
									obj = null;
								}
							}
						}
						
						if (obj != null) {
							config.put(name, obj);
						}
					}
				}
			}
		}
		
		reader.close();
		return config;
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
						
						Object object = field.get(null);
						String value = "";
						if (object instanceof Object[]) {
							ItemStackConfig[] itemStacks = (ItemStackConfig[]) object;
							for (ItemStackConfig itemStack: itemStacks) {
								value +=  itemStack.toString()+",";
							}
						} else {
							value = object.toString();
						}
						
						out.write(name + "=" + value + System.getProperty("line.separator"));
						
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
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