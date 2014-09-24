package mods.gollum.core.client.gui.config.entries.entry;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.ButtonEntry;
import cpw.mods.fml.client.config.GuiSelectString;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class ItemEntry extends SelectValueEntry {

	public ItemEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<String> configElement) {
		super(owningScreen, owningEntryList, configElement);
	}

	@Override
	protected Map<Object, String> getOptions() {
		Map<Object, String> options = new HashMap<Object, String>();
		
		for (Entry<String, Item> entry : RegisteredObjects.instance().getItemsList().entrySet()) {
			options.put (entry.getKey(), entry.getValue().getItemStackDisplayName(new ItemStack(entry.getValue())));
		}
		
		return options;
	}
	
}