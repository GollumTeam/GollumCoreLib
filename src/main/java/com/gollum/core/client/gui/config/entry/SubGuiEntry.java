package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class SubGuiEntry extends ButtonEntry {

	public SubGuiEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		this.labelDisplay = false;
		this.updateValueButtonText(this.getLabel());
	}

	@Override
	public void valueButtonPressed(int slotIndex) {
		Class<? extends GuiScreen> c = ((Class<? extends GuiScreen>)(this.configElement.getValue()));
		try {
			this.mc.displayGuiScreen(c.getConstructor(GuiScreen.class).newInstance(this.parent.parent));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getValue() {
		super.getValue();
		return this.configElement.getValue();
	}
}
