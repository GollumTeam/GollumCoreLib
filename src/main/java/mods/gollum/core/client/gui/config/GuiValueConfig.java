package mods.gollum.core.client.gui.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import static mods.gollum.core.ModGollumCoreLib.log;
import mods.gollum.core.client.gui.config.element.CategoryElement;
import mods.gollum.core.client.gui.config.element.TypedValueElement;
import mods.gollum.core.client.gui.config.entry.CategoryEntry;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.JsonConfigProp;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
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
			
			HashMap<String, Object> values  = (HashMap<String, Object>)this.parentEntry.getValue();
			HashMap<String, Object> dValues = (HashMap<String, Object>)this.parentEntry.configElement.getDefaultValue();
			
			for (Entry<String, Object> entry : values.entrySet()) {
				String name         = entry.getKey();
				Object value        = entry.getValue();
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
//		Object value = this.entryList.getValues();
//		this.parentEntry.setValue(value);
	}
	
}
