package mods.gollum.core.client.gui.config.element;

import java.lang.reflect.Field;
import java.util.HashMap;

import mods.gollum.core.client.gui.config.entry.CategoryEntry;
import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.JsonConfigProp;

public class CategoryElement extends ConfigElement {

	private ConfigLoad configLoad;
	private HashMap<String, Class> types = new HashMap<String, Class>();
	private HashMap<String, ConfigProp> props = new HashMap<String, ConfigProp>();
	
	public CategoryElement(String category, ConfigLoad configLoad) {
		super(category);
		
		this.configLoad = configLoad;
		
		HashMap<String, Object> value        = new HashMap<String, Object>();
		HashMap<String, Object> defaultValue = new HashMap<String, Object>();
		
		for (Field f : this.configLoad.config.getClass().getDeclaredFields()) {
			
			ConfigProp prop = f.getAnnotation(ConfigProp.class);
			if (prop != null) {
				
				String c = prop.group();
				if (c == null || c.equals("")) {
					c = "General";
				}
				if (category.equals(c)) {
					try {
						String name =  f.getName();
						value       .put(name, f.get(this.configLoad.config));
						defaultValue.put(name, f.get(this.configLoad.configDefault));
						this.types  .put(name, f.getType());
						this.props  .put(name, prop);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		this.value        = value;
		this.defaultValue = defaultValue;
		
	}
	
	public ConfigProp getConfigProp() {
		return new JsonConfigProp();
	}
	
	@Override
	public Class getType() {
		return null;
	}

	public Class< ? extends ConfigEntry> getEntryClass() {
		return CategoryEntry.class;
	}

	public Class getType(String name) {
		if (this.types.containsKey(name)) {
			return this.types.get(name);
		}
		return null;
	}
	
	public ConfigProp getProp(String name) {
		if (this.props.containsKey(name)) {
			return this.props.get(name);
		}
		return null;
	}

}
