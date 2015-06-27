package com.gollum.core.client.gui.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;

import cpw.mods.fml.client.config.GuiButtonExt;


public class ListInlineEntry extends ConfigEntry {
	
	public String[] values;
	private int index = 0;
	
	private GuiButtonExt btnLabel;
	private GuiButtonExt btnPrev;
	private GuiButtonExt btnNext;
	
	public ListInlineEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		this.values = configElement.getConfigProp().validValues();
			
		this.btnLabel  = new GuiButtonExt(0, 0, 0, 18, 18, "");
		this.btnPrev   = new GuiButtonExt(0, 0, 0, 18, 18, "<");
		this.btnNext   = new GuiButtonExt(0, 0, 0, 18, 18, ">");
		this.btnLabel.enabled = false;
		
		this.setValue(configElement.getValue());
	}
	
	public void updateValueButtonText() {
		this.btnLabel.displayString = (String) this.getValue();
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth) {
		
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected, resetControlWidth);
		
		this.btnPrev.xPosition = this.parent.controlX;
		this.btnPrev.yPosition = y;
		this.btnPrev.drawButton(this.mc, mouseX, mouseY);
		
		this.btnLabel.xPosition = this.parent.controlX + 20;
		this.btnLabel.yPosition = y;
		this.btnLabel.width = this.parent.controlWidth - 40;
		this.btnLabel.drawButton(this.mc, mouseX, mouseY);
		
		this.btnNext.xPosition = this.parent.controlX + this.parent.controlWidth - 17;
		this.btnNext.yPosition = y;
		this.btnNext.drawButton(this.mc, mouseX, mouseY);
		
	}
	
	@Override
	public Object getValue() {
		super.getValue();
		return this.values[this.index];
	}

	@Override
	public ConfigEntry setValue(Object value) {
		
		int i = 0;
		for (String val: this.values) {
			if (val.equals(value)) {
				this.index = i;
				this.updateValueButtonText();
				return super.setValue(this.getValue());
			}
			i++;
		}
		
		this.updateValueButtonText();
		return super.setValue(value);
	}
	
	private void prev() {
		this.index--;
		if (this.index < 0) {
			this.index = this.values.length-1;
		}
		this.setValue(this.getValue());
	}
	
	private void next() {
		this.index++;
		if (this.index >= this.values.length) {
			this.index = 0;
		}
		this.setValue(this.getValue());
	}
	
	/////////////
	// ACTIONS //
	/////////////
	
	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		if (this.btnNext.mousePressed(this.mc, x, y)) {
			btnNext.func_146113_a(mc.getSoundHandler());
			this.next();
			return true;
		}
		if (this.btnPrev.mousePressed(this.mc, x, y)) {
			btnPrev.func_146113_a(mc.getSoundHandler());
			this.prev();
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
		this.btnNext.mouseReleased(x, y);
		this.btnPrev.mouseReleased(x, y);
	}
	
}
