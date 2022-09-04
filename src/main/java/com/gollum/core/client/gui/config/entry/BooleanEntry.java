package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class BooleanEntry extends ButtonEntry {
	
	boolean value;
	
	public BooleanEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		this.value = (Boolean)configElement.getValue();
		this.updateValueButtonText();
	}
	
	public void updateValueButtonText() {
		String text = I18n.format(String.valueOf(this.value));
		this.updateValueButtonText(text, this.value ? this.COLOR_GREEN : this.COLOR_RED);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.setValue(!this.value);
	}
	
	@Override
	public Object getValue() {
		super.getValue();
		return this.value;
	}
	
	@Override
	public ConfigEntry setValue(Object value) {
		try {
			this.value = (Boolean)value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateValueButtonText();
		return super.setValue(value);
	}

}
