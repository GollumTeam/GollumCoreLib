package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.TypedValueElement;
import com.gollum.core.common.config.JsonConfigProp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class AddButtonEntry extends ConfigEntry {

	protected GuiTextField textFieldValue;
	private boolean first = true;
	
	public AddButtonEntry(int index, Minecraft mc, GuiConfigEntries parent) {
		super(index, mc, parent, new TypedValueElement(int.class, "", 0, 0, new JsonConfigProp()));
		this.labelDisplay = false;
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
		this.btnRemove.visible = false;
		this.btUndoIsVisible   = false;
		this.btResetIsVisible  = false;
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, partialTicks);
	}
	
	public Object getValue() {
		super.getValue();
		return this.configElement.getValue();
	}
	
	@Override
	public void drawToolTip(int mouseX, int mouseY) {
	}

}
