package mods.gollum.core.client.gui.config.entries.entry;

import java.util.HashMap;
import java.util.Map;

import mods.gollum.core.client.gui.config.GuiFieldConfig;
import mods.gollum.core.client.gui.config.GuiJsonConfig;
import mods.gollum.core.client.gui.config.entries.GuiEditArrayCustomEntries;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.ButtonEntry;
import cpw.mods.fml.client.config.GuiEditArrayEntries;
import cpw.mods.fml.client.config.GuiSelectString;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class JsonEntry extends ButtonEntry {

	protected final Json defaultValue;
	protected final Json beforeValue;
	protected Json currentValue;
	
	/**
	 * Contructor for ArryList
	 * @param parent
	 * @param entryList
	 * @param configElement
	 * @param value
	 */
	public JsonEntry(GuiConfig parent, GuiEditArrayCustomEntries entryList, IConfigElement<String> configElement) {
		super(parent, entryList, configElement);
		
		defaultValue  = (Json)this.configElement.getDefault();
		beforeValue   = (Json)configElement.get();
		currentValue  = (Json)configElement.get();
		
		updateValueButtonText();
	}
	
	public JsonEntry(GuiConfig parent, GuiConfigEntries entryList, IConfigElement<String> configElement) {
		super(parent, entryList, configElement);
		
		defaultValue  = (Json)this.configElement.getDefault();
		beforeValue   = (Json)configElement.get();
		currentValue  = (Json)configElement.get();
		
		updateValueButtonText();
	}

	@Override
	public void updateValueButtonText() {
		this.btnValue.displayString = currentValue.toString(); 
	}

	@Override
	public void valueButtonPressed(int slotIndex) {
		Json d  = (Json)this.configElement.get();
		Json dv = (Json)this.configElement.getDefault();
		
		mc.displayGuiScreen(new GuiJsonConfig(this.owningScreen, this, currentValue, defaultValue));
	}

	public void setValueFromChildScreen(Json newValue) {
		if (enabled() && currentValue != null ? !currentValue.equals(newValue) : newValue != null) {
			currentValue = newValue;
			updateValueButtonText();
		}
	}

	@Override
	public boolean isDefault() {
		return defaultValue.equals(currentValue);
	}

	@Override
	public void setToDefault() {
		if (enabled()) {
			this.currentValue = defaultValue;
			updateValueButtonText();
		}
	}

	@Override
	public boolean isChanged() {
		if (beforeValue != null)
			return !beforeValue.equals(currentValue);
		else
			return currentValue == null;
	}

	@Override
	public void undoChanges() {
		if (enabled()) {
			currentValue = beforeValue;
			updateValueButtonText();
		}
	}

	@Override
	public boolean saveConfigElement() {
		if (enabled() && isChanged()) {
			this.configElement.set(currentValue);
			return configElement.requiresMcRestart();
		}
		return false;
	}

	@Override
	public String getCurrentValue() {
		return this.currentValue.toString();
	}
	
	public Json getValue() {
		return this.currentValue;
	}

	@Override
	public String[] getCurrentValues() {
		return new String[] { getCurrentValue() };
	}
}