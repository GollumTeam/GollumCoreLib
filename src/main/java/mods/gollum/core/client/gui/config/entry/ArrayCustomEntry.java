package mods.gollum.core.client.gui.config.entry;

import static mods.gollum.core.ModGollumCoreLib.log;

import mods.gollum.core.client.gui.config.GuiEditCustomArray;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.ArrayEntry;
import cpw.mods.fml.client.config.IConfigElement;


public class ArrayCustomEntry extends ArrayEntry implements IGollumConfigEntry {

	public ArrayCustomEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
	}
	
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		mc.displayGuiScreen(new GuiEditCustomArray(this.owningScreen, configElement, slotIndex, this, currentValues, this.configElement.getDefaults()));
	}
	
	@Override
	public void setValueFromChildScreen(Object value) {
		// TODO Auto-generated method stub
	}
	
	public Object createNewSubEntry() {
		log.debug (currentValues.getClass().getComponentType().getName());
		return null;
	}
	
	public Class getSubEntryClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
