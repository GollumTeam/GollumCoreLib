package mods.gollum.core.client.gui.config;

import mods.gollum.core.client.gui.config.entries.JsonEntry;
import mods.gollum.core.tools.simplejson.Json;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;

public class CustomElement extends DummyCategoryElement {
	
	public CustomElement(Class< ? extends IConfigEntry> classEntry, String name, String langKey, Object value, Object defaultValue) {
		super(name, langKey, classEntry);
		
		this.value        = value;
		this.defaultValue = defaultValue;
	}

}
