package com.gollum.core.client.gui.config.entry;

import net.minecraft.client.Minecraft;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;

public class IntegerEntry extends LongEntry {
	
	public IntegerEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public Object getValue() {
		super.getValue();
		Integer value = new Integer(0);
		try {
			value = Integer.parseInt(this.textFieldValue.getText());
		} catch (Exception e) {
		}
		
		return value;
	}

}
