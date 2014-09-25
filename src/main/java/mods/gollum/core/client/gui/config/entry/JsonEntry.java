package mods.gollum.core.client.gui.config.entry;

import java.awt.Color;

import mods.gollum.core.client.gui.config.GuiEditCustomArray;
import mods.gollum.core.client.gui.config.GuiJsonConfig;
import mods.gollum.core.client.gui.config.entry.logic.EditCustomArrayEntryLogic;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.client.config.GuiConfigEntries.ButtonEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class JsonEntry extends ButtonEntry implements IGollumConfigEntry {

	protected Json defaultValue;
	protected Json beforeValue;
	protected Json currentValue;
	
	protected EditCustomArrayEntryLogic logic = new EditCustomArrayEntryLogic(this);
	
	public JsonEntry(GuiConfig parent, GuiConfigEntries entryList, IConfigElement<String> configElement) {
		super(parent, entryList, configElement);
		
		this.init();
		
		updateValueButtonText();
	}
	
	public void init() {
		defaultValue  = (Json)this.configElement.getDefault();
		beforeValue   = (Json)configElement.get();
		currentValue  = (Json)configElement.get();
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		logic.drawEntryBefore();
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
		logic.drawEntryAfter(x, y, mouseX, mouseY);
	}
	
	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY) ? true : this.logic.mousePressed(index, x, y);
	}

	/**
	 * Fired when the mouse button is released. Arguments: index, x, y,
	 * mouseEvent, relativeX, relativeY
	 */
	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
		this.logic.mouseReleased(x, y);
	}
	
	@Override
	public void updateValueButtonText() {
		this.btnValue.displayString = currentValue.toString(); 
	}

	@Override
	public void valueButtonPressed(int slotIndex) {
		mc.displayGuiScreen(new GuiJsonConfig(this.owningScreen, this, this.currentValue, this.defaultValue));
	}

	public void setValueFromChildScreen(Object newValue) {
		if (enabled() && currentValue != null ? !currentValue.equals((Json)newValue) : newValue != null) {
			currentValue = (Json) newValue;
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
			this.currentValue = (Json) defaultValue.clone();
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
			currentValue = (Json) beforeValue.clone();
			updateValueButtonText();
		}
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

	@Override
	public boolean saveConfigElement() {
		if (enabled() && isChanged()) {
			this.configElement.set(currentValue);
			return configElement.requiresMcRestart();
		}
		return false;
	}
}