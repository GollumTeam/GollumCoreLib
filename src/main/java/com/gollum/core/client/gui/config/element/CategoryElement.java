package com.gollum.core.client.gui.config.element;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.gollum.core.client.gui.config.entry.CategoryEntry;
import com.gollum.core.client.gui.config.entry.ConfigEntry;
import com.gollum.core.common.config.ConfigLoader.ConfigLoad;
import com.gollum.core.common.config.ConfigProp;
import com.gollum.core.common.config.JsonConfigProp;

public class CategoryElement extends ConfigElement {

	private ConfigLoad configLoad;
	private HashMap<String, Class> types = new HashMap<String, Class>();
	private HashMap<String, ConfigProp> props = new HashMap<String, ConfigProp>();
	public String category ;
	
	public CategoryElement(String category, ConfigLoad configLoad) {
		this(category, configLoad, category);
	}
		
	
	public CategoryElement(String category, ConfigLoad configLoad, String label) {
		super(label);
		
		this.category = category; 
		
		this.configLoad = configLoad;
		
		LinkedHashMap<String, Object> value        = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> defaultValue = new LinkedHashMap<String, Object>();
		
		for (Field f : this.configLoad.config.getClass().getDeclaredFields()) {
			
			ConfigProp prop = f.getAnnotation(ConfigProp.class);
			if (prop != null && prop.show()) {
				
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
	
	@Override
	public ConfigProp getConfigProp() {
		return new JsonConfigProp();
	}
	
	@Override
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
	
	public HashMap<String, ConfigProp> getProps() {
		return this.props;
	}

}
