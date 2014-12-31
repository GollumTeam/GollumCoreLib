package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.Map.Entry;
import java.util.LinkedHashMap;

import mods.gollum.core.client.gui.config.element.CategoryElement;
import mods.gollum.core.client.gui.config.element.TypedValueElement;
import mods.gollum.core.client.gui.config.entry.ArrayEntry;
import mods.gollum.core.client.gui.config.entry.CategoryEntry;
import mods.gollum.core.common.config.ConfigProp;

public class GuiArrayConfig extends GuiConfig {

	private String category;
	private ArrayEntry parentEntry;
	
	public GuiArrayConfig(ArrayEntry arrayEntry) {
		super(arrayEntry.parent.parent);
		this.parentEntry = arrayEntry;
		this.titleLine2  = ((GuiConfig)this.parent).titleLine2 + " > " + arrayEntry.getLabel();
		this.category    = arrayEntry.configElement.getName();
		log.debug ("Category : "+this.category);
	}
	
	@Override
	public boolean isArray() {
		return true;
	}
	
	public boolean displayEntryeLabel() {
		return false;
	}
	
	@Override
	protected void initConfigElement() {
		
		try {
			
			Object[] values  = (Object[])this.parentEntry.getValue();
			Object[] dValues = (Object[])this.parentEntry.configElement.getDefaultValue();
			
			for (int i = 0; i < values.length; i++) {
				
				Object value = values[i];
				try {
					Object cloned = value.getClass().getMethod("clone").invoke(value);
					value = cloned;
				} catch (Exception e) {
				}
				
				Object defaultValue = null;
				if (i < dValues.length) {
					defaultValue = dValues[i];
				} else {
					defaultValue = this.parentEntry.configElement.newValue();
				}
				if (defaultValue == null) {
					log.severe("A value in array are skipped. Don't default new value are found for this field : \""+this.parentEntry.getName()+"["+i+"]\"");
					continue;
				}
				
				Class type      = values.getClass().getComponentType();
				ConfigProp prop = this.parentEntry.configElement.getConfigProp();
				
				this.configElements.add(new TypedValueElement(type, i+"", value, defaultValue, prop));
			}
		} catch (Exception e)  {
			e.printStackTrace();
		}
		
	}

	@Override
	public void saveValue() {
		
//		LinkedHashMap<String, Object> values    = (LinkedHashMap<String, Object>) this.parentEntry.getValue ();
//		LinkedHashMap<String, Object> newValues = (LinkedHashMap<String, Object>) this.entryList.getValues();
//		
//		for (Entry<String, Object> entry : values.entrySet()) {
//			if (newValues.containsKey(entry.getKey())) {
//				
//				Object value = newValues.get(entry.getKey());
//				
//				try {
//					Object cloned = value.getClass().getMethod("clone").invoke(value);
//					value = cloned;
//				} catch (Exception e) {
//				}
//				
//				values.put(entry.getKey(), value);
//			}
//		}
//		
//		this.parentEntry.setValue(values);
	}
	
}
