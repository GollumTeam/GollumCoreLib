package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.Map.Entry;
import java.util.TreeMap;

import mods.gollum.core.client.gui.config.element.CategoryElement;
import mods.gollum.core.client.gui.config.element.TypedValueElement;
import mods.gollum.core.client.gui.config.entry.CategoryEntry;
import mods.gollum.core.common.config.ConfigProp;

public class GuiValueConfig extends GuiConfig {

	private String category;
	private CategoryEntry parentEntry;
	
	public GuiValueConfig(CategoryEntry parentEntry) {
		super(parentEntry.parent.parent);
		this.parentEntry = parentEntry;
		this.titleLine2  = parentEntry.getLabel();	
		this.category    = parentEntry.configElement.getName();
		log.debug ("Category : "+this.category);
	}
	
	@Override
	protected void initConfigElement() {
		
		try {
			
			TreeMap<String, Object> values  = (TreeMap<String, Object>)this.parentEntry.getValue();
			TreeMap<String, Object> dValues = (TreeMap<String, Object>)this.parentEntry.configElement.getDefaultValue();
			
			for (Entry<String, Object> entry : values.entrySet()) {
				String name         = entry.getKey();
				Object value        = entry.getValue();
				
				try {
					Object cloned = value.getClass().getMethod("clone").invoke(value);
					value = cloned;
				} catch (Exception e) {
				}
				
				Object defaultValue = dValues.get(name);
				Class type          = ((CategoryElement)this.parentEntry.configElement).getType(name);
				ConfigProp prop     = ((CategoryElement)this.parentEntry.configElement).getProp(name);
				
				this.configElements.add(new TypedValueElement(type, name, value, defaultValue, prop));
			}
		} catch (Exception e)  {
			e.printStackTrace();
		}
		
	}

	@Override
	public void saveValue() {
		
		TreeMap<String, Object> values    = (TreeMap<String, Object>) this.parentEntry.getValue ();
		TreeMap<String, Object> newValues = (TreeMap<String, Object>) this.entryList.getValues();
		
		for (Entry<String, Object> entry : values.entrySet()) {
			if (newValues.containsKey(entry.getKey())) {
				
				Object value = newValues.get(entry.getKey());
				
				try {
					Object cloned = value.getClass().getMethod("clone").invoke(value);
					value = cloned;
				} catch (Exception e) {
				}
				
				values.put(entry.getKey(), value);
			}
		}
		
		this.parentEntry.setValue(values);
	}
	
}
