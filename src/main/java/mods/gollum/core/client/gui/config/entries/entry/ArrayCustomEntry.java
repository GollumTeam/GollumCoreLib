package mods.gollum.core.client.gui.config.entries.entry;

import mods.gollum.core.client.gui.config.GuiEditCustomArray;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.ArrayEntry;
import cpw.mods.fml.client.config.IConfigElement;


public class ArrayCustomEntry extends ArrayEntry {

	public ArrayCustomEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
	}
	
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		mc.displayGuiScreen(new GuiEditCustomArray(this.owningScreen, configElement, slotIndex, currentValues, enabled()));
	}
	

}
