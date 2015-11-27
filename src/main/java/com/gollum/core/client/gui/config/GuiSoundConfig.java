package com.gollum.core.client.gui.config;

import java.util.Map.Entry;
import java.util.TreeSet;

import com.gollum.core.client.gui.config.element.ListElement;
import com.gollum.core.client.gui.config.entry.ListEntry;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.client.audio.SoundCategory;

public class GuiSoundConfig extends GuiListConfig {

	public GuiSoundConfig(ListEntry listEntry) {
		super(listEntry);
	}
	
	@Override
	protected void initConfigElement() {
		
		for (Entry<SoundCategory, TreeSet<String>> soundsByCategory: RegisteredObjects.instance().getAllSound().entrySet()) {
			
			SoundCategory category = soundsByCategory.getKey();
			TreeSet<String> sounds = soundsByCategory.getValue();
			
			if (sounds != null) {
				for (String sound : sounds) {
					this.configElements.add(new ListElement(sound, "["+category.getCategoryName()+"] " + sound, category.getCategoryName()));
				}
			}
		}
		this.filter();
 	}
}
