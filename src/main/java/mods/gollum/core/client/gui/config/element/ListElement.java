package mods.gollum.core.client.gui.config.element;

import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.client.gui.config.entry.ListSlotEntry;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.JsonConfigProp;

public class ListElement extends ConfigElement {
	
	public String label;
	
	public ListElement(String value, String label) {
		super(value);
		this.value = value;
		this.label = label;
	}
	
	public Class< ? extends ConfigEntry> getEntryClass() {
		return ListSlotEntry.class;
	}
	
	@Override
	public ConfigProp getConfigProp() {
		return new JsonConfigProp();
	}
}
