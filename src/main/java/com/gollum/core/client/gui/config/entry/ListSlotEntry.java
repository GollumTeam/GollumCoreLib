package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.GuiListConfig;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.client.gui.config.element.ListElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

public class ListSlotEntry extends ConfigEntry {

	protected static RenderItem itemRender = new RenderItem();
	public ItemStack itemStackIcon = null;
	
	public ListSlotEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth) {
		
		int x1 = this.parent.controlX;
		int y1 = y + this.parent.getSlotHeight ();
		int x2 = this.parent.scrollBarX - 1;
		
		if (this.itemStackIcon != null) {
			try {

				this.drawRec(x1-19, y+1, 18, 18, 0xFFCCCCCC);
				this.drawRec(x1-18, y+2, 16, 16, 0xFF999999);
				RenderHelper.enableGUIStandardItemLighting();
				this.itemRender.renderItemIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), this.itemStackIcon, x1-18, y+2);
				
			} catch (Exception e) {
			}
		}
		
		if (this.isSelected()) {
			this.drawRec(x1  , y  , x2-x1  , this.parent.getSlotHeight ()  , 0xFF808080);
			this.drawRec(x1+1, y+1, x2-x1-2, this.parent.getSlotHeight ()-2, 0xFF000000);
		}
		
		String label = ((ListElement)this.configElement).label;
		
		while (!label.equals("") && this.mc.fontRenderer.getStringWidth(label) > x2 - x1 - 7) {
			label = label.substring(0, label.length() - 1);
		}
		
		this.mc.fontRenderer.drawString(label, this.parent.controlX + 7, y+6, this.isSelected() ? 0xFFFFFF : 0x888888, true);
	}
	
	public boolean isSelected() {
		return ((GuiListConfig)this.parent.parent).currentValue.equals(this.getValue());
	}

	@Override
	public Object getValue() {
		return this.configElement.getValue();
	}

	@Override
	public ConfigEntry setValue(Object value) {
		return this;
	}
	
	@Override
	public void setSlot (int slotIndex) {
		((GuiListConfig)this.parent.parent).currentValue = this.getValue();
	}
}
