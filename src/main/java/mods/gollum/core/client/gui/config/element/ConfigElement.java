package mods.gollum.core.client.gui.config.element;

import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.common.config.ConfigProp;

public class ConfigElement {
	
	private String name;
	
	public ConfigElement (String name) {
		this.name = name;
	}
	
	public ConfigProp getConfigProp() {
		// TODO Auto-generated method stub
		return null;
	}

	public Class< ? extends ConfigEntry> getEntryClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return this.name;
	}

}
