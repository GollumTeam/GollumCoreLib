package mods.gollum.core.client.gui.config.entry;

import cpw.mods.fml.client.config.GuiButtonExt;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

public abstract class ButtonEntry extends ConfigEntry {

	protected GuiButtonExt btnValue;
	
	public ButtonEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(mc, parent, configElement);
		
		this.btnValue = new GuiButtonExt(0, parent.controlX, 0, parent.controlWidth, 18, "");
		
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
		
		this.btnValue.width = this.parent.controlWidth;
		this.btnValue.xPosition = this.parent.controlX;
		this.btnValue.yPosition = y;
		this.btnValue.enabled = this.enabled();
		this.btnValue.drawButton(this.mc, mouseX, mouseY);
	}
}
