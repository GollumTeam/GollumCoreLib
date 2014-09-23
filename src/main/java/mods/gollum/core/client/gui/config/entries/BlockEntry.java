package mods.gollum.core.client.gui.config.entries;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import mods.gollum.core.client.gui.config.GuiFieldConfig;
import mods.gollum.core.client.gui.config.GuiJsonConfig;
import mods.gollum.core.tools.registered.RegisteredObjects;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.ButtonEntry;
import cpw.mods.fml.client.config.GuiSelectString;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class BlockEntry extends SelectEntry {

	public BlockEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<String> configElement) {
		super(owningScreen, owningEntryList, configElement);
	}

	@Override
	protected Map<Object, String> getOptions() {
		Map<Object, String> options = new HashMap<Object, String>();
		
		for (Entry<String, Block> entry : RegisteredObjects.instance().getBlocksList().entrySet()) {
			options.put (entry.getKey(), entry.getValue().getLocalizedName());
		}
		
		return options;
	}
	
}