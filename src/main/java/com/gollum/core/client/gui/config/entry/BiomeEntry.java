package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiBiomeConfig;
import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeEntry extends ListEntry {

	public BiomeEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiBiomeConfig(this));
	}
	
	@Override
	public void updateValueButtonText(String text) {
		
		BiomeGenBase biome = RegisteredObjects.instance().getBiome(this.value.toString());
		if (biome == null) {
			text = this.value+" (ID not found)";
		} else {
			text = biome.biomeName+" ("+biome.biomeID+")";
		}
		super.updateValueButtonText(text);
	}
	
	@Override
	public boolean hasSearch () {
		return true;
	}
}
