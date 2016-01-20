package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.GuiListConfig;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.client.gui.config.element.ListElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class ListSlotEntry extends ConfigEntry {

	protected GuiButtonExt btAction = new GuiButtonExt(0, 0, 0, "");
	public ItemStack itemStackIcon = null;
	public String action = null;
	
	public ListSlotEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight , int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth) {
		
		int x1 = this.parent.controlX;
		int y1 = y + this.parent.getSlotHeight ();
		int x2 = this.parent.scrollBarX - 1;
		
		if (this.itemStackIcon != null) {
			try {

				this.drawRec(x1-19, y+1, 18, 18, 0xFFCCCCCC);
				this.drawRec(x1-18, y+2, 16, 16, 0xFF999999);
				RenderHelper.enableGUIStandardItemLighting();
				this.mc.getRenderItem().renderItemIntoGUI(this.itemStackIcon, x1-18, y+2);
				
			} catch (Exception e) {
			}
		}
		
		if (this.isSelected()) {
			this.drawRec(x1  , y  , x2-x1  , this.parent.getSlotHeight ()  , 0xFF808080);
			this.drawRec(x1+1, y+1, x2-x1-2, this.parent.getSlotHeight ()-2, 0xFF000000);
		}
		
		String label = ((ListElement)this.configElement).label;
		
		while (!label.equals("") && this.mc.fontRendererObj.getStringWidth(label) > x2 - x1 - 7) {
			label = label.substring(0, label.length() - 1);
		}
		
		this.mc.fontRendererObj.drawString(label, this.parent.controlX + 7, y+6, this.isSelected() ? 0xFFFFFF : 0x888888, true);
		
		if (this.action != null) {
			this.btAction.displayString = this.action;
			this.btAction.width = 55;
			this.btAction.height = 13;
			this.btAction.xPosition = x2-60;
			this.btAction.yPosition = y+3;
			this.btAction.drawButton(this.mc, mouseX, mouseY);
		}
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
	public void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY) {
		if (this.action != null && this.btAction.mousePressed(this.mc, mouseX, mouseY)) {
			if (this.parent.parent instanceof GuiListConfig) {
				((GuiListConfig)this.parent.parent).runSlotAction(slotIndex, this.btAction, doubleClick, mouseX, mouseY);
			}
		} else {
			super.elementClicked(slotIndex, doubleClick, mouseX, mouseY);
		}
	}
	
	@Override
	public void setSlot (int slotIndex) {
		((GuiListConfig)this.parent.parent).currentValue = this.getValue();
	}
}
