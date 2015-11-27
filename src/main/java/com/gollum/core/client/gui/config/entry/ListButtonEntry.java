package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.world.biome.BiomeGenBase;

public class ListButtonEntry extends ListEntry {
	
	public ListButtonEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
}