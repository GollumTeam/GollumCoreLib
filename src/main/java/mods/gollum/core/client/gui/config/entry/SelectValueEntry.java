package mods.gollum.core.client.gui.config.entry;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiSelectString;
import cpw.mods.fml.client.config.IConfigElement;

public abstract class SelectValueEntry extends cpw.mods.fml.client.config.GuiConfigEntries.SelectValueEntry {
	
	protected final String        beforeValue;
	protected Object              currentValue;
	protected Map<Object, String> selectableValues;

	public SelectValueEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement<String> configElement) {
		super(owningScreen, owningEntryList, configElement, new HashMap<Object, String>());
		
		beforeValue = configElement.get().toString();
		currentValue = configElement.get().toString();
		
		this.selectableValues = getOptions();
		
		updateValueButtonText();
	}

	protected abstract Map<Object, String> getOptions();

	@Override
	public void updateValueButtonText() {
		if (this.currentValue != null) {
			this.btnValue.displayString = this.currentValue.toString();
			if (this.selectableValues.containsKey(currentValue)) {
				this.btnValue.displayString = this.selectableValues.get(this.currentValue);
			}
		}
	}

	@Override
	public void valueButtonPressed(int slotIndex) {
		mc.displayGuiScreen(new GuiSelectString(this.owningScreen, configElement, slotIndex, selectableValues, currentValue, enabled()));
	}

	public void setValueFromChildScreen(Object newValue) {
		if (enabled() && currentValue != null ? !currentValue.equals(newValue) : newValue != null) {
			currentValue = newValue;
			updateValueButtonText();
		}
	}

	@Override
	public boolean isDefault() {
		if (configElement.getDefault() != null)
			return configElement.getDefault().equals(currentValue);
		else
			return currentValue == null;
	}

	@Override
	public void setToDefault() {
		if (enabled()) {
			this.currentValue = configElement.getDefault().toString();
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

	@Override
	public String[] getCurrentValues() {
		return new String[] { getCurrentValue() };
	}
}