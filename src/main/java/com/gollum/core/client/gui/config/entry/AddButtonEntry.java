package com.gollum.core.client.gui.config.entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.TypedValueElement;
import com.gollum.core.common.config.JsonConfigProp;

public class AddButtonEntry extends ConfigEntry {

	protected GuiTextField textFieldValue;
	private boolean first = true;
	
	public AddButtonEntry(int index, Minecraft mc, GuiConfigEntries parent) {
		super(index, mc, parent, new TypedValueElement(int.class, "", 0, 0, new JsonConfigProp()));
		this.labelDisplay = false;
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth) {
		this.btnRemove.visible = false;
		this.btUndoIsVisible   = false;
		this.btResetIsVisible  = false;
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected, resetControlWidth);
	}
	
	public Object getValue() {
		super.getValue();
		return this.configElement.getValue();
	}
	
	@Override
	public void drawToolTip(int mouseX, int mouseY) {
	}
}
