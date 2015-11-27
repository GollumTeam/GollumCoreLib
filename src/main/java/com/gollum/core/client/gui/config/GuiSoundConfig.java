package com.gollum.core.client.gui.config;

import java.util.TreeSet;

import com.gollum.core.client.gui.config.element.ListElement;
import com.gollum.core.client.gui.config.entry.ListButtonSlotEntry;
import com.gollum.core.client.gui.config.entry.ListEntry;
import com.gollum.core.client.gui.config.entry.SoundCategoryEntry;
import com.gollum.core.tools.registered.RegisteredObjects;

public class GuiSoundConfig extends GuiListConfig {

	public GuiSoundConfig(ListEntry listEntry) {
		super(listEntry);
	}
	
	@Override
	protected void initConfigElement() {
		
		GuiSoundCategoryConfig parent = (GuiSoundCategoryConfig)this.parent;
		SoundCategoryEntry soundCategoryEntry = (SoundCategoryEntry)parent.parentEntry;
		
		Object category = ((ListButtonSlotEntry)this.parentEntry).buttonValue;
		TreeSet<String> sounds = RegisteredObjects.instance().getAllSound().get(category);
		if (sounds != null) {
			for (String sound : sounds) {
				this.configElements.add(new ListElement(sound, sound));
			}
		}
 	}
	
	@Override
	public boolean undoIsVisible() {
		return false;
	}
	
	@Override
	public boolean resetIsVisible() {
		return false;
	}

}
