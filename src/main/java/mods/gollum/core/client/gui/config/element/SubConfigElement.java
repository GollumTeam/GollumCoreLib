package mods.gollum.core.client.gui.config.element;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import mods.gollum.core.client.gui.config.entry.CategoryEntry;
import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.client.gui.config.entry.SubConfigEntry;
import mods.gollum.core.common.config.Config;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.JsonConfigProp;

public class SubConfigElement extends ConfigElement {
	
	public SubConfigElement(String category, ArrayList<ConfigLoad> configLoadList) {
		super(category);

		ArrayList<Config> value = new ArrayList<Config>();
		ArrayList<Config> defaultValue = new ArrayList<Config>();
		
		for (ConfigLoad configLoad : configLoadList) {
			value.add(configLoad.config);
			defaultValue.add(configLoad.configDefault);
		}
		
		this.value = value;
		this.defaultValue = defaultValue;
		
	}
	
	@Override
	public ConfigProp getConfigProp() {
		return new JsonConfigProp();
	}
	
	@Override
	public Class< ? extends ConfigEntry> getEntryClass() {
		return SubConfigEntry.class;
	}

}
