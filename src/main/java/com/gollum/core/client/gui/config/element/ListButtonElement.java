package com.gollum.core.client.gui.config.element;

import com.gollum.core.client.gui.config.entry.ConfigEntry;
import com.gollum.core.client.gui.config.entry.ListButtonSlotEntry;
import com.gollum.core.common.config.ConfigProp;
import com.gollum.core.common.config.JsonConfigProp;

public class ListButtonElement extends ConfigElement {
	
	public String label;
	private ConfigProp prop;
	private Class<? extends ListButtonSlotEntry> entry;

	public ListButtonElement(Object value, String label, Class<? extends ListButtonSlotEntry> entry) {
		this(value, label, entry, new JsonConfigProp());
	}
	public ListButtonElement(Object value, String label, Class<? extends ListButtonSlotEntry> entry, ConfigProp prop) {
		super(value.toString());
		this.value = value;
		this.label = label;
		this.prop  = prop;
		this.entry = entry;
	}
	
	public Class< ? extends ConfigEntry> getEntryClass() {
		return this.entry;
	}
	
	@Override
	public ConfigProp getConfigProp() {
		return this.prop;
	}
}
