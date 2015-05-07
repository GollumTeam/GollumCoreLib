package com.gollum.core.client.gui.config.entry;

import net.minecraft.client.Minecraft;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;

public class ByteEntry extends ShortEntry {
	
	public ByteEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public Object getValue() {
		
		Byte value = new Byte((byte)0);
		try {
			value = Byte.parseByte(this.textFieldValue.getText());
		} catch (Exception e) {
		}
		
		return value;
	}

}
