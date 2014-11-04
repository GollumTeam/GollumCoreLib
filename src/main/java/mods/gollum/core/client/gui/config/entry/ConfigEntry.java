package mods.gollum.core.client.gui.config.entry;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import cpw.mods.fml.client.config.GuiButtonExt;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;

public abstract class ConfigEntry {
	
	protected Minecraft mc;
	protected GuiConfigEntries parent;
	protected ConfigElement configElement;
	
	protected GuiButtonExt btUndo;
	protected GuiButtonExt btReset;

	public ConfigEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		this.mc            = mc;
		this.parent        = parent;
		this.configElement = configElement;
		
		this.btUndo  = new GuiButtonExt(0, 0, 0, 18, 18, UNDO_CHAR);
		this.btReset = new GuiButtonExt(0, 0, 0, 18, 18, RESET_CHAR);
	}
	
	public int getLabelWidth() {
		return 50; // TODO
	}
	
	public int getValueWidth() {
		return 200; // TODO
	}
	
	public boolean enabled() {
		return this.mc.theWorld != null ? !this.configElement.getConfigProp().worldRestart() : true;
	}
	
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		
//		String label = (!isValidValue ? EnumChatFormatting.RED.toString() :
//            (isChanged ? EnumChatFormatting.WHITE.toString() : EnumChatFormatting.GRAY.toString()))
//            + (isChanged ? EnumChatFormatting.ITALIC.toString() : "") + this.name;
		
		String label = "Label supe cool";
		this.mc.fontRenderer.drawString(label, this.parent.labelX, y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF);
		
		this.btUndo.xPosition = this.parent.scrollBarX - 44;
		this.btUndo.yPosition = y;
		this.btUndo.enabled = this.enabled() && this.isChanged();
		this.btUndo.drawButton(this.mc, mouseX, mouseY);
		
		this.btReset.xPosition = this.parent.scrollBarX - 22;
		this.btReset.yPosition = y;
		this.btReset.enabled = this.enabled() && !this.isDefault();
		this.btReset.drawButton(this.mc, mouseX, mouseY);
	}
	
	public boolean isChanged () {
		return true; //  TODO
	}
	
	public boolean isDefault () {
		return false; //  TODO
	}
	
	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {
		// TODO Auto-generated method stub
	}
}
