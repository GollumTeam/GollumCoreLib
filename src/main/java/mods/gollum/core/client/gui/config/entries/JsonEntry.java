package mods.gollum.core.client.gui.config.entries;

import mods.gollum.core.client.gui.config.GuiFieldConfig;
import mods.gollum.core.client.gui.config.GuiJsonConfig;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class JsonEntry extends CategoryEntry {
	
	public JsonEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement jsonElement) {
		super(owningScreen, owningEntryList, jsonElement);
	}

	@Override
	protected GuiScreen buildChildScreen() {

		Json d  = (Json)this.configElement.get();
		Json dv = (Json)this.configElement.getDefault();
		
		return new GuiJsonConfig(this.owningScreen, this.name, d, dv);
	}
}