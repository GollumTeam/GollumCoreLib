package com.gollum.core.client.gui.config;

import java.util.TreeMap;

import com.gollum.core.client.gui.config.element.ListElement;
import com.gollum.core.client.gui.config.entry.ListEntry;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.world.biome.Biome;

public class GuiBiomeConfig extends GuiListConfig {

	public GuiBiomeConfig(ListEntry listEntry) {
		super(listEntry);
	}
	
	@Override
	protected void initConfigElement() {
		TreeMap<String, Biome> list = RegisteredObjects.instance().getBiomesList();
		for (String registerName: list.keySet()) {
			Biome biome = list.get(registerName);
			if (biome != null) {
				this.configElements.add(new ListElement(registerName, biome.getBiomeName() + " ("+registerName+")"));
			}
		}
		this.filter();
 	}

}
