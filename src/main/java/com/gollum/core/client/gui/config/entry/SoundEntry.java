package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.GuiSoundConfig;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;

public class SoundEntry extends ListEntry {

	public SoundEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiSoundConfig(this));
	}
	
	@Override
	public void updateValueButtonText(String text) {
		SoundCategory category = RegisteredObjects.instance().getSoundCategoryBySound(this.value.toString());
		if (category == null) {
			text = "[not found] " + this.value;
		} else {
			text = "["+category.getCategoryName()+"] " + this.value;
		}
		super.updateValueButtonText(text);
	}
	
	@Override
	public boolean hasSearch () {
		return true;
	}
}
