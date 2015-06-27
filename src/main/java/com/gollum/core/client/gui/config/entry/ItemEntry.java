package com.gollum.core.client.gui.config.entry;

import net.minecraft.client.Minecraft;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.GuiItemConfig;
import com.gollum.core.client.gui.config.element.ConfigElement;

public class ItemEntry extends ListEntry {

	public ItemEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiItemConfig(this));
	}
	
	@Override
	public boolean hasSearch () {
		return true;
	}
	
}
