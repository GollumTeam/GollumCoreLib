package mods.gollum.core.client.gui.config.entry;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import static mods.gollum.core.ModGollumCoreLib.log;
import cpw.mods.fml.client.config.GuiButtonExt;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.common.config.JsonConfigProp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public abstract class ConfigEntry implements IGuiListEntry {
	
	protected Minecraft mc;
	public    GuiConfigEntries parent;
	public ConfigElement configElement;
	
	protected GuiButtonExt btUndo;
	protected GuiButtonExt btReset;
	
	public boolean labelDisplay = true;

	public ConfigEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		this.mc            = mc;
		this.parent        = parent;
		this.configElement = configElement;
		
		this.btUndo  = new GuiButtonExt(0, 0, 0, 18, 18, UNDO_CHAR);
		this.btReset = new GuiButtonExt(0, 0, 0, 18, 18, RESET_CHAR);
	}
	
	public int getLabelWidth() {
		if (!this.labelDisplay) {
			return 0;
		}
		return mc.fontRenderer.getStringWidth(this.getLabel());
	}
	
	public int getValueWidth() {
		return 200; // TODO
	}
	
	public String getLabel () {
		return this.parent.parent.mod.i18n().trans("config."+this.configElement.getName());
	}

	public String getName() {
		return this.configElement.getName();
	}
	
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		
		if (this.labelDisplay) {
			
//			String label = (!isValidValue ? EnumChatFormatting.RED.toString() :
//				(isChanged ? EnumChatFormatting.WHITE.toString() : EnumChatFormatting.GRAY.toString()))
//			+ (isChanged ? EnumChatFormatting.ITALIC.toString() : "") + this.name;
		
			String label = this.getLabel();
			this.mc.fontRenderer.drawString(label, this.parent.labelX, y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF);
		}
		
		this.btUndo.xPosition = this.parent.scrollBarX - 44;
		this.btUndo.yPosition = y;
		this.btUndo.enabled = this.enabled() && this.isChanged();
		this.btUndo.drawButton(this.mc, mouseX, mouseY);
		
		this.btReset.xPosition = this.parent.scrollBarX - 22;
		this.btReset.yPosition = y;
		this.btReset.enabled = this.enabled() && !this.isDefault();
		this.btReset.drawButton(this.mc, mouseX, mouseY);
	}

	public abstract Object getValue();
	
	public abstract ConfigEntry setValue(Object value);
	
	public boolean enabled() {
		return this.mc.theWorld != null ? !this.configElement.getConfigProp().worldRestart() : true;
	}
	
	public boolean isChanged () {
		return !(configElement != null && this.equals (configElement.getValue()));
	}
	
	public boolean isDefault () {
		return configElement != null && this.equals (configElement.getDefaultValue());
	}

	@Override
	public boolean equals (Object value) {
		if (value == null) {
			return value == this.getValue();
		}
		if (value instanceof ConfigEntry) {
			return ((ConfigEntry)value).getValue().equals(this.getValue());
		}
		
		return value.equals(this.getValue());
	}
	
	public ConfigEntry setToDefault() {
		return this.setValue(this.configElement.getDefaultValue());
	}
	
	public ConfigEntry undoChanges() {
		return this.setValue(this.configElement.getValue());
	}
	
	public boolean requiresMcRestart() {
		return configElement.getConfigProp().mcRestart();
	}
	
	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		
		if (this.btReset.mousePressed(this.mc, x, y)) {
			btReset.func_146113_a(mc.getSoundHandler());
			this.setToDefault();
			return true;
		} else if (this.btUndo.mousePressed(this.mc, x, y)) {
			btUndo.func_146113_a(mc.getSoundHandler());
			this.undoChanges();
			return true;
		}
		return false;
	}
	

	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
	}
	
	public void keyTyped(char eventChar, int eventKey){
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
	}

	public void updateCursorCounter() {
	}

	
}
