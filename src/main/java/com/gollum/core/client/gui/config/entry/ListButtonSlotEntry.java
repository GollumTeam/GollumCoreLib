package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiBiomeConfig;
import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.GuiListConfig;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;

public abstract class ListButtonSlotEntry extends ListEntry {
	
	public Object buttonValue = null;

	public ListButtonSlotEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		this.buttonValue = this.value;
		this.value = ((GuiListConfig)this.parent.parent).currentValue;
		this.updateValueButtonText("");
	}
	
	@Override
	public void updateValueButtonText(String text) {
		super.updateValueButtonText(this.buttonValue != null ? this.buttonValue.toString() : text);
	}
	
	@Override
	public ConfigEntry setValue(Object value) {
		this.value = value;
		((GuiListConfig)this.parent.parent).currentValue = value;
		return super.setValue(value);
	}
}

