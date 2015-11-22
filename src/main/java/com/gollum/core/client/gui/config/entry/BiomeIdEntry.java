package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiBiomeIdConfig;
import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeIdEntry extends ListEntry {

	public BiomeIdEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiBiomeIdConfig(this));
	}
	
	@Override
	public void updateValueButtonText(String text) {
		
		BiomeGenBase[] biomes = RegisteredObjects.instance().getAllBiomes();
		if (biomes[(Integer) this.value] == null) {
			text = "Not Found ("+this.value+")";
		} else {
			text = biomes[(Integer) this.value].biomeName+" ("+this.value+")";
		}
		super.updateValueButtonText(text);
	}
	
	@Override
	public boolean hasSearch () {
		return true;
	}
}
