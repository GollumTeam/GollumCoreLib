package com.gollum.core.client.gui.config.entry;

import net.minecraft.client.Minecraft;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.common.config.ConfigProp;
import com.gollum.core.common.config.type.ConfigJsonType;
import com.gollum.core.tools.simplejson.Json;

public class ConfigJsonTypeEntry extends JsonEntry {

	public ConfigJsonTypeEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		this.getValue();
	}
	
	protected void init(Object value, Object valueDefault, ConfigProp prop) {
		super.init(((ConfigJsonType)value).writeConfig(), ((ConfigJsonType)valueDefault).writeConfig(), prop);
	}
	
	public void updateValueButtonText(Object value) {
		if (this.proxy instanceof ButtonEntry) {
			((ButtonEntry) this.proxy).updateValueButtonText(value.toString());
		}
	}
	
	@Override
	protected Json getOldValue () {
		return ((ConfigJsonType)this.configElement.getValue()).writeConfig();
	}
	
	private Json rebuildJson (Json json) {
		try {
			ConfigJsonType value = ((ConfigJsonType)this.configElement.getValue()).getClass().newInstance();
			value.readConfig(json);
			json = value.writeConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@Override
	public Object getValue() {
		ConfigJsonType value = (ConfigJsonType) this.configElement.getValue();
		try {
			Json json = this.rebuildJson((Json) super.getValue());
			value.readConfig(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateValueButtonText(value);
		return value;
	}
	
	@Override
	public ConfigEntry setValue(Object value) {
		if (value instanceof ConfigJsonType) {
			return super.setValue(((ConfigJsonType) value).writeConfig());
		}
		
		if (value instanceof Json) {
			value = this.rebuildJson ((Json) value);
		}
		
		return super.setValue(value);
	}
}
