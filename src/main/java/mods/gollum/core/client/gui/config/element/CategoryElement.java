package mods.gollum.core.client.gui.config.element;

import mods.gollum.core.client.gui.config.entry.CategoryEntry;
import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.JsonConfigProp;

public class CategoryElement extends ConfigElement {
	
	private ConfigLoad configLoad;
	
	public CategoryElement(String category, ConfigLoad configLoad) {
		super(category);
		this.configLoad = configLoad;
	}
	
	public ConfigProp getConfigProp() {
		return new JsonConfigProp();
	}

	@Override
	public Class getType() {
		return null;
	}

	public Class< ? extends ConfigEntry> getEntryClass() {
		return CategoryEntry.class;
	}
}
