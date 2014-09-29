package mods.gollum.core.client.gui.config.entry;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import mods.gollum.core.client.gui.config.entry.logic.GuiArrayButtonEntryLogic;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.client.config.GuiConfigEntries.ButtonEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class BooleanEntry extends ButtonEntry {
	
	protected final boolean beforeValue;
	protected boolean currentValue;
	
	protected GuiArrayButtonEntryLogic logic = new GuiArrayButtonEntryLogic(this);
	
	public BooleanEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
		this.beforeValue = Boolean.valueOf(configElement.get().toString());
		this.currentValue = beforeValue;
		this.btnValue.enabled = enabled();
		updateValueButtonText();
	}
	
	@Override
	public void updateValueButtonText() {
		this.btnValue.displayString = I18n.format(String.valueOf(currentValue));
		btnValue.packedFGColour = currentValue ? GuiUtils.getColorCode('2', true) : GuiUtils.getColorCode('4', true);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		if (enabled()) currentValue = !currentValue;
	}
	
	@Override
	public boolean isDefault() {
		return currentValue == Boolean.valueOf(configElement.getDefault().toString());
	}
	
	@Override
	public void setToDefault() {
		if (enabled()) {
			currentValue = Boolean.valueOf(configElement.getDefault().toString());
			updateValueButtonText();
		}
	}
	
	@Override
	public boolean isChanged() {
		return currentValue != beforeValue;
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
			configElement.set(currentValue);
			return configElement.requiresMcRestart();
		}
		return false;
	}
	
	@Override
	public Boolean getCurrentValue() {
		return currentValue;
	}

	@Override
	public Boolean[] getCurrentValues() {
		return new Boolean[] { getCurrentValue() };
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
}
