package com.gollum.core.client.gui.config;

import java.util.Map.Entry;
import java.util.TreeSet;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.client.gui.config.element.ListElement;
import com.gollum.core.client.gui.config.entry.ConfigEntry;
import com.gollum.core.client.gui.config.entry.ListEntry;
import com.gollum.core.client.gui.config.entry.ListSlotEntry;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiSoundConfig extends GuiListConfig {

	public GuiSoundConfig(ListEntry listEntry) {
		super(listEntry);
	}
	
	@Override
	protected void initConfigElement() {
		
		for (Entry<String, SoundEvent> entry: RegisteredObjects.instance().getSoundEventsList().entrySet()) {
			String name = entry.getKey();
			SoundEvent sound = entry.getValue();
			String domain = sound.getRegistryName().getResourceDomain();
			this.configElements.add(new ListElement(name, name, sound.getRegistryName().getResourceDomain()));
		}
		this.filter();
 	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for (int i = 0; i < this.entryList.getSize(); i++) {
			ConfigEntry entry = this.entryList.getEntry(i);
			if (entry instanceof ListSlotEntry) {
				((ListSlotEntry) entry).action = ModGollumCoreLib.i18n.trans("config.sound_play");
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void runSlotAction(int slotIndex, GuiButtonExt btAction, boolean doubleClick, int mouseX, int mouseY) {
		String sound = (String) this.entryList.getEntry(slotIndex).getValue();
		this.mc.getSoundHandler().stopSounds();
		this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(RegisteredObjects.instance().getSoundEvent(sound), 1.0F));;
	}
}
