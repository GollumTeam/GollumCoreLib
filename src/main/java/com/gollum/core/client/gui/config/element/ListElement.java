package com.gollum.core.client.gui.config.element;

import com.gollum.core.client.gui.config.entry.ConfigEntry;
import com.gollum.core.client.gui.config.entry.ListSlotEntry;
import com.gollum.core.common.config.ConfigProp;
import com.gollum.core.common.config.JsonConfigProp;

public class ListElement extends ConfigElement {
	
	public String label;
	public String group;
	
	public ListElement(Object value, String label) {
		this(value, label, "General");
	}
	
	public ListElement(Object value, String label, String group) {
		super(value.toString());
		this.value = value;
		this.label = label;
		this.group = group;
	}
	
	public Class< ? extends ConfigEntry> getEntryClass() {
		return ListSlotEntry.class;
	}
	
	@Override
	public ConfigProp getConfigProp() {
		return new JsonConfigProp();
	}
}
