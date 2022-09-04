package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.GuiListConfig;
import com.gollum.core.client.gui.config.element.ConfigElement;

import net.minecraft.client.Minecraft;


public class ListEntry extends ButtonEntry {
	
	Object value;
	
	public ListEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		this.value = configElement.getValue();
		this.updateValueButtonText(this.value.toString());
	}
	
	public boolean hasSearch () {
		return false;
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiListConfig(this));
	}

	@Override
	public Object getValue() {
		super.getValue();
		return this.value;
	}
	
	@Override
	public ConfigEntry setValue(Object value) {
		this.value = value;
		this.updateValueButtonText(this.value.toString());
		return super.setValue(value);
	}
	
}
