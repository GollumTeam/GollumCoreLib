package com.gollum.core.client.gui.config.entry;

import java.lang.reflect.Array;
import java.util.LinkedHashMap;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.GuiValueConfig;
import com.gollum.core.client.gui.config.element.ConfigElement;

import net.minecraft.client.Minecraft;

public class CategoryEntry extends ButtonEntry {
	
	private Object value;
	public boolean mustBeRestart = false;
	
	public CategoryEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		this.labelDisplay = false;
		this.mustBeRestart = false;
		this.updateValueButtonText(this.getLabel());
		this.value = this.configElement.getValue();
	}

	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiValueConfig(this));
	}

	@Override
	public Object getValue() {
		super.getValue();
		return this.value;
	}

	@Override
	public ConfigEntry setValue(Object value) {
		this.value = value;
		return super.setValue(value);
	}
	
	@Override
	public boolean requiresMcRestart() {
		return this.mustBeRestart ;
	}
	
	@Override
	public boolean equals (Object values) {
		
		if (values instanceof LinkedHashMap) {
			
			for (Object key : ((LinkedHashMap)values).keySet()) {
				if (((LinkedHashMap) this.value).containsKey(key)) {
					
					Object value    = ((LinkedHashMap)values).get(key);
					Object oldValue = ((LinkedHashMap) this.value).get(key);
					
					if (oldValue == null && oldValue != value) {
						return false;
					} else {
						
						if (value == null) {
							return false;
						}
						if (oldValue.getClass().isArray()) {
							
							int length = Array.getLength(oldValue);
							if (length != Array.getLength(value)) {
								return false;
							}
							for (int i = 0; i < length; i++) {
								Object nv = Array.get(value, i);
								Object ov = Array.get(oldValue, i);
								if (!nv.equals(ov)) {
									return false;
								}
							}
							
						} else if (!oldValue.equals(value)) {
							return false;
						}
					}
					
				} else {
					return false;
				}
			}
			
			return true;
		}
		
		return super.equals(values);
	}
}
