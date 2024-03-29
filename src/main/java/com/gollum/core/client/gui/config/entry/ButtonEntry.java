package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;

import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraft.client.Minecraft;

public abstract class ButtonEntry extends ConfigEntry {

	protected GuiButtonExt btnValue;

	public static Boolean COLOR_NONE  = null;
	public static Boolean COLOR_GREEN = true;
	public static Boolean COLOR_RED   = false;
	
	public ButtonEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		this.btnValue = new GuiButtonExt(0, parent.controlX, 0, parent.controlWidth, 18, "");
		
	}
	
	public void updateValueButtonText(String text) {
		this.updateValueButtonText(text, this.COLOR_NONE);
	}
	public void updateValueButtonText(String text, Boolean color) {
		this.btnValue.displayString = text;
		if (color != null) {
			this.btnValue.packedFGColour = color ? GuiUtils.getColorCode('2', true) : GuiUtils.getColorCode('4', true);
		}
	}
	
	/**
	 * Called when the value button has been clicked.
	 */
	public abstract void valueButtonPressed(int slotIndex);
	
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth, float partial) {
		
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, resetControlWidth, partial);
		
		this.btnValue.width = this.parent.controlWidth;
		this.btnValue.x = this.parent.controlX;
		this.btnValue.y = y;
		this.btnValue.enabled = this.enabled();
		this.btnValue.drawButton(this.mc, mouseX, mouseY, partial);
	}
	
	/////////////
	// ACTIONS //
	/////////////
	
	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		if (this.btnValue.mousePressed(this.mc, x, y)) {
			btnValue.playPressSound(mc.getSoundHandler());
			this.valueButtonPressed(index);
			return true;
		}
		return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
	}
	
	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
		this.btnValue.mouseReleased(x, y);
	}
}
