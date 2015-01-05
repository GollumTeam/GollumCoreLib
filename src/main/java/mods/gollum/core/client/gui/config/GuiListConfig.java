package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import mods.gollum.core.client.gui.config.element.ListElement;
import mods.gollum.core.client.gui.config.element.TypedValueElement;
import mods.gollum.core.client.gui.config.entry.ArrayEntry;
import mods.gollum.core.client.gui.config.entry.ListEntry;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.JsonConfigProp;

public class GuiListConfig extends GuiConfig {
	
	protected ListEntry parentEntry;
	public String currentValue;
	
	public GuiListConfig(ListEntry listEntry) {
		super(listEntry.parent.parent);
		this.parentEntry  = listEntry;
		this.titleLine2   = ((GuiConfig)this.parent).titleLine2 + " > " + listEntry.getLabel();
		this.currentValue = this.parentEntry.getValue().toString();
	}
	
	public boolean displayEntriesLabel() {
		return false;
	}
	
	@Override
	protected void initConfigElement() {
		
		String[] values = this.parentEntry.configElement.getConfigProp().validValues();
		
		for (String value : values) {
			this.configElements.add(new ListElement(value, value));
		}
 	}
	
	@Override
	public void saveValue() {
		this.parentEntry.setValue(this.currentValue);
	}
	
	@Override
	protected boolean isChanged() {
		return !this.currentValue.equals(this.parentEntry.configElement.getValue());
	}
	
	@Override
	protected boolean isDefault() {
		return this.currentValue.equals(this.parentEntry.configElement.getDefaultValue());
	}
	
	protected void setToDefault() {
		
		Object defaultValue = this.parentEntry.configElement.getDefaultValue();
		
		for (int i = 0; i < this.entryList.getSize(); i++) {
			if (this.entryList.getEntry(i).getValue().equals(defaultValue)) {
				this.entryList.setSlot(i);
				break;
			}
		}
	}
	
	protected void undoChanges() {

		Object value = this.parentEntry.configElement.getValue();
		
		for (int i = 0; i < this.entryList.getSize(); i++) {
			if (this.entryList.getEntry(i).getValue().equals(value)) {
				this.entryList.setSlot(i);
				break;
			}
		}
	}
	
}
