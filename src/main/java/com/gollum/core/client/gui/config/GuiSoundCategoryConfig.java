package com.gollum.core.client.gui.config;

import static com.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.gollum.core.client.gui.config.element.CategoryElement;
import com.gollum.core.client.gui.config.element.ListButtonElement;
import com.gollum.core.client.gui.config.element.ListElement;
import com.gollum.core.client.gui.config.entry.CategoryEntry;
import com.gollum.core.client.gui.config.entry.ConfigEntry;
import com.gollum.core.client.gui.config.entry.ListEntry;
import com.gollum.core.client.gui.config.entry.SoundCategoryEntry;
import com.gollum.core.client.gui.config.entry.SoundEntry;
import com.gollum.core.common.config.ConfigLoader;
import com.gollum.core.tools.registered.RegisteredObjects;

import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;

public class GuiSoundCategoryConfig extends GuiListConfig {
	
	public GuiSoundCategoryConfig(SoundCategoryEntry soundEntry) {
		super(soundEntry);
	}
	
	@Override
	protected void initConfigElement() {
		for (SoundCategory category : RegisteredObjects.instance().getAllSound().keySet()) {
			configElements.add(new ListButtonElement(category, category.getCategoryName(), SoundEntry.class, this.parentEntry.configElement.getConfigProp()));
		}
	}
	
	@Override
	public void saveValue() {
		this.parentEntry.setValue(this.currentValue);
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
