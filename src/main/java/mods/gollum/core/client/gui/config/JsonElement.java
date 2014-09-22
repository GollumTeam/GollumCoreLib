package mods.gollum.core.client.gui.config;

import mods.gollum.core.client.gui.config.entries.JsonEntry;
import mods.gollum.core.tools.simplejson.Json;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;

public class JsonElement<T> extends DummyCategoryElement<T> {
	
	public JsonElement(String name, String langKey, Json value, Json defaultValue) {
		super(name, langKey, JsonEntry.class);
		
		this.value        = value;
		this.defaultValue = defaultValue;
	}

}
