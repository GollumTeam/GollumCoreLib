package com.gollum.core.client.gui.config;

import java.util.Map.Entry;

import com.gollum.core.client.gui.config.element.ListElement;
import com.gollum.core.client.gui.config.entry.ConfigEntry;
import com.gollum.core.client.gui.config.entry.ListEntry;
import com.gollum.core.client.gui.config.entry.ListSlotEntry;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;

public class GuiBiomeIdConfig extends GuiListConfig {

	public GuiBiomeIdConfig(ListEntry listEntry) {
		super(listEntry);
	}
	
	@Override
	protected void initConfigElement() {
		for (BiomeGenBase biome : RegisteredObjects.instance().getAllBiomes()) {
			if (biome != null) {
				this.configElements.add(new ListElement(biome.biomeID, biome.biomeName + " ("+biome.biomeID+")"));
			}
		}
		this.filter();
 	}

}
