package com.gollum.core.client.gui.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.GuiModIdConfig;
import com.gollum.core.client.gui.config.GuiSoundCategoryConfig;
import com.gollum.core.client.gui.config.GuiSoundConfig;
import com.gollum.core.client.gui.config.element.ConfigElement;

public class SoundEntry extends ListButtonSlotEntry {

	public SoundEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiSoundConfig(this));
	}
	
	@Override
	public void updateValueButtonText(String text) {
		text = this.buttonValue != null ? ((SoundCategory)this.buttonValue).getCategoryName() : ((SoundCategory)this.value).getCategoryName();
		this.updateValueButtonText(text, this.COLOR_NONE);
	}
}
