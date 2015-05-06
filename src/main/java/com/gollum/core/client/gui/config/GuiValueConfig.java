package com.gollum.core.client.gui.config;

import static com.gollum.core.ModGollumCoreLib.log;

import java.util.Map.Entry;
import java.util.LinkedHashMap;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;

import com.gollum.core.client.gui.config.element.CategoryElement;
import com.gollum.core.client.gui.config.element.TypedValueElement;
import com.gollum.core.client.gui.config.entry.CategoryEntry;
import com.gollum.core.common.config.ConfigProp;

import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;

public class GuiValueConfig extends GuiConfig {

	private String category;
	private CategoryEntry parentEntry;
	
	public GuiValueConfig(CategoryEntry parentEntry) {
		super(parentEntry.parent.parent);
		this.parentEntry = parentEntry;
		this.titleLine2  = ((GuiConfig)this.parent).titleLine2 + " > " + parentEntry.getLabel();	
		this.category    = parentEntry.configElement.getName();
		log.debug ("Category : "+this.category);
	}
	
	@Override
	protected void initConfigElement() {
		
		try {
			
			LinkedHashMap<String, Object> values  = (LinkedHashMap<String, Object>)this.parentEntry.getValue();
			LinkedHashMap<String, Object> dValues = (LinkedHashMap<String, Object>)this.parentEntry.configElement.getDefaultValue();
			
			for (Entry<String, Object> entry : values.entrySet()) {
				String name  = entry.getKey();
				Object value = entry.getValue();
				
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
		
		LinkedHashMap<String, Object> values    = (LinkedHashMap<String, Object>) this.parentEntry.getValue ();
		LinkedHashMap<String, Object> newValues = (LinkedHashMap<String, Object>) this.entryList.getValues();
		
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
	

	@Override
	public void displayParent() {
		if (this.entryList.requiresMcRestart()) {
			this.parentEntry.mustBeRestart = true;
		}
		super.displayParent();
	}
	
}
