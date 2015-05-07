package com.gollum.core.client.gui.config.entry;

import net.minecraft.client.Minecraft;

import com.gollum.core.client.gui.config.GuiBlockConfig;
import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;

public class BlockEntry extends ListEntry {

	public BlockEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiBlockConfig(this));
	}
	
	@Override
	public boolean hasSearch () {
		return true;
	}
}
