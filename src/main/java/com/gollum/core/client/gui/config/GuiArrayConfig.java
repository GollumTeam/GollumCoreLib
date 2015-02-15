package com.gollum.core.client.gui.config;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.gollum.core.client.gui.config.element.TypedValueElement;
import com.gollum.core.client.gui.config.entry.ArrayEntry;
import com.gollum.core.common.config.ConfigProp;
import com.gollum.core.common.config.JsonConfigProp;

public class GuiArrayConfig extends GuiConfig {

	private ArrayEntry parentEntry;
	
	public GuiArrayConfig(ArrayEntry arrayEntry) {
		super(arrayEntry.parent.parent);
		this.parentEntry = arrayEntry;
		this.titleLine2  = ((GuiConfig)this.parent).titleLine2 + " > " + arrayEntry.getLabel();
	}
	
	@Override
	public boolean isArray() {
		return true;
	}
	
	public boolean displayEntriesLabel() {
		return false;
	}
	
	@Override
	protected void initConfigElement() {
		
		try {
			
			Object values  = this.parentEntry.getValue();
			Object dValues = this.parentEntry.configElement.getDefaultValue();
			
			for (int i = 0; i < Array.getLength(values); i++) {
				
				Object value = Array.get(values, i);
				try {
					Object cloned = value.getClass().getMethod("clone").invoke(value);
					value = cloned;
				} catch (Exception e) {
				}
				
				Object defaultValue = null;
				if (i < Array.getLength(dValues)) {
					defaultValue = Array.get(dValues, i);
				} else {
					defaultValue = this.parentEntry.configElement.newValue();
				}
				if (defaultValue == null) {
					log.severe("A value in array are skipped. Don't default new value are found for this field : \""+this.parentEntry.getName()+"["+i+"]\"");
					continue;
				}
				
				Class type      = values.getClass().getComponentType();
				ConfigProp prop = this.parentEntry.configElement.getConfigProp();
				
				this.configElements.add(new TypedValueElement(type, "", value, defaultValue, prop));
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
		
		ArrayList<Object> values = this.entryList.getValuesByIndex();
		
		Class type = this.parentEntry.getValue().getClass().getComponentType();
		Object rtn = Array.newInstance(type,values.size());
		
		int i = 0;
		for (Object value : values) {
			Array.set (rtn, i++, value);
		}
		
		this.parentEntry.setValue(rtn);
	}
	
}
