package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiBiomeConfig;
import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.GuiSoundCategoryConfig;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;

public class SoundCategoryEntry extends ListButtonEntry {
	
	public SoundCategoryEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiSoundCategoryConfig(this));
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
	
}
