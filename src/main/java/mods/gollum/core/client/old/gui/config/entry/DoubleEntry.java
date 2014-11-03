package mods.gollum.core.client.old.gui.config.entry;

import net.minecraft.client.renderer.Tessellator;
import mods.gollum.core.client.old.gui.config.entry.logic.GuiArrayButtonEntryLogic;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;

public class DoubleEntry extends cpw.mods.fml.client.config.GuiConfigEntries.DoubleEntry {
	
	protected GuiArrayButtonEntryLogic logic = new GuiArrayButtonEntryLogic(this);
	
	public DoubleEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
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
