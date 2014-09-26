package mods.gollum.core.client.gui.config.entry;

import static mods.gollum.core.ModGollumCoreLib.log;
import mods.gollum.core.client.gui.config.GuiEditCustomArray;
import mods.gollum.core.common.config.type.ItemStackConfigType;
import mods.gollum.core.tools.simplejson.Json;
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
	public void setValueFromChildScreen(Object newValue) {
		if (enabled() && newValue != null && newValue.getClass().isArray()) {
			this.currentValues = (Object[]) newValue;
			updateValueButtonText();
		}
	}
	
	public Object createNewSubEntry() {
		
		Class subType = currentValues.getClass().getComponentType();
		
		log.debug ("Add : "+subType.getName());
		g// TODO other type for add
		if (ItemStackConfigType.class.isAssignableFrom(subType)) {
			try {
				return subType.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
