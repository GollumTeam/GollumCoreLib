package mods.gollum.core.client.gui.config.entry;

import java.lang.reflect.Array;

import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.Minecraft;

public class ConfigJsonTypeEntry extends JsonEntry {

	public ConfigJsonTypeEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		this.getValue();
	}
	
	protected void init(Object value, Object valueDefault, ConfigProp prop) {
		super.init(((IConfigJsonType)value).writeConfig(), ((IConfigJsonType)valueDefault).writeConfig(), prop);
	}
	
	public void updateValueButtonText(Object value) {
		if (this.proxy instanceof ButtonEntry) {
			((ButtonEntry) this.proxy).updateValueButtonText(value.toString());
		}
	}
	
	@Override
	protected Json getOldValue () {
		return ((IConfigJsonType)this.configElement.getValue()).writeConfig();
	}
	
	@Override
	public Object getValue() {
		IConfigJsonType value = (IConfigJsonType) this.configElement.getValue();
		try {
			Json json = (Json) super.getValue();
			value = ((IConfigJsonType)this.configElement.getValue()).getClass().newInstance();
			value.readConfig(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateValueButtonText(value);
		return value;
	}
	
	@Override
	public ConfigEntry setValue(Object value) {
		if (value instanceof IConfigJsonType) {
			super.setValue(((IConfigJsonType) value).writeConfig());
			this.getValue();
			return this;
		}
		
		super.setValue(value);
		
		this.getValue();
		return this;
	}
}
