package mods.gollum.core.client.gui.config;

import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class GollumCategoryEntry extends CategoryEntry {
	
	public GollumCategoryEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
		super(owningScreen, owningEntryList, prop);
	}

	@Override
	protected GuiScreen buildChildScreen() {
		return new GuiGollumConfig(this.owningScreen, this.name);
	}
}