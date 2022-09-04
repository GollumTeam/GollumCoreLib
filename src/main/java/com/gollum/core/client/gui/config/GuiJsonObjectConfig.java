package com.gollum.core.client.gui.config;

import static com.gollum.core.ModGollumCoreLib.logger;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.gollum.core.client.gui.config.element.TypedValueElement;
import com.gollum.core.client.gui.config.entry.JsonObjectEntry;
import com.gollum.core.common.config.ConfigProp;
import com.gollum.core.tools.simplejson.Json;
import com.gollum.core.tools.simplejson.JsonObject;

public class GuiJsonObjectConfig extends GuiConfig {

	private JsonObjectEntry parentEntry;
	
	public GuiJsonObjectConfig(JsonObjectEntry jsonObjectEntry) {
		super(jsonObjectEntry.parent.parent);
		this.parentEntry = jsonObjectEntry;
		this.titleLine2  = ((GuiConfig)this.parent).titleLine2 + " > " + jsonObjectEntry.getLabel();
	}
	
	@Override
	public boolean isArray() {
		return true;
	}
	
	public boolean displayEntriesLabel() {
		return true;
	}
	
	@Override
	protected void initConfigElement() {
		
		try {
			
			JsonObject values  = (JsonObject)this.parentEntry.getValue();
			JsonObject dValues = (JsonObject)this.parentEntry.configElement.getDefaultValue();
			
			for (Entry<String, Json> entry : values.allChildWithKey()) {
				
				
				Object defaultValue = null;
				if (dValues.containsKey(entry.getKey())) {
					defaultValue = dValues.child(entry.getKey());
				} else {
					defaultValue = this.parentEntry.configElement.newValue();
				}
				if (defaultValue == null) {
					logger.severe("A value in array are skipped. Don't default new value are found for this field : \""+this.parentEntry.getName()+"["+entry.getKey()+"]\"");
					continue;
				}
				
				ConfigProp prop = this.parentEntry.configElement.getConfigProp();
				
				this.configElements.add(new TypedValueElement(Json.class, entry.getKey(), entry.getValue(), defaultValue, prop));
			}
		} catch (Exception e)  {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean canAdd() {
		
		if (!this.isArray()) {
			return false;
		}
		
		Object defaultValue = this.newValue();
		ConfigProp prop = this.parentEntry.configElement.getConfigProp();
		Integer max = null;
		try {
			max = Integer.parseInt(prop.maxListLength());
		} catch (Exception e) {
		}
		
		return defaultValue != null && !prop.isListLengthFixed() && (max == null || this.entryList.getValuesByIndex().size() < max);
	}
	
	@Override
	public boolean canRemove() {
		
		if (!this.isArray()) {
			return false;
		}
		
		Object defaultValue = this.newValue();
		ConfigProp prop = this.parentEntry.configElement.getConfigProp();
		Integer min = null;
		try {
			min = Integer.parseInt(prop.minListLength());
		} catch (Exception e) {
		}
		
		return defaultValue != null && this.entryList.getSize() > 0 && !prop.isListLengthFixed() && (min == null || this.entryList.getValuesByIndex().size() > min);
	}
	
	@Override
	public Object newValue() {
		return this.parentEntry.configElement.newValue();
	}
	
	@Override
	public ConfigProp newConfigProp() {
		return this.parentEntry.configElement.getConfigProp();
	}
	
	@Override
	public void saveValue() {
		
		LinkedHashMap<String, Object> values = this.entryList.getValues();
		JsonObject rtn = new JsonObject();
		for (Entry<String, Object> entry : values.entrySet()) {
			rtn.add(entry.getKey(), (Json)entry.getValue());
		}
		
		
		
		this.parentEntry.setValue(rtn);
	}
	
}
