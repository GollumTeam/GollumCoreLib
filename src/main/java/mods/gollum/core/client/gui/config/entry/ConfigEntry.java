package mods.gollum.core.client.gui.config.entry;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.client.config.HoverChecker;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.common.config.ConfigProp;
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
	
	public GuiButtonExt btnAdd;
	protected GuiButtonExt btnRemove;
	protected GuiButtonExt btUndo;
	protected GuiButtonExt btReset;
	public int index;
	
	protected List<String> toolTip        = new ArrayList<String>();
	protected List<String> undoToolTip    = new ArrayList<String>();
	protected List<String> defaultToolTip = new ArrayList<String>();

	protected HoverChecker tooltipHoverChecker;
	protected HoverChecker undoHoverChecker;
	protected HoverChecker defaultHoverChecker;
	
	protected boolean labelDisplay = true;
	private int posY = 0;
	
	public ConfigEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		this.index         = index;
		this.mc            = mc;
		this.parent        = parent;
		this.configElement = configElement;
		
		this.btnAdd     = new GuiButtonExt(0, 0, 0, 18, 18, "+");
		this.btnRemove  = new GuiButtonExt(0, 0, 0, 18, 18, "x");
		this.btUndo  = new GuiButtonExt(0, 0, 0, 18, 18, UNDO_CHAR);
		this.btReset = new GuiButtonExt(0, 0, 0, 18, 18, RESET_CHAR);
		
		this.btnAdd    .packedFGColour = GuiUtils.getColorCode('2', true);
		this.btnRemove .packedFGColour = GuiUtils.getColorCode('c', true);
		
		this.undoToolTip   .add(I18n.format("fml.configgui.tooltip.undoChanges"));
		this.defaultToolTip.add(I18n.format("fml.configgui.tooltip.resetToDefault"));
		
		ConfigProp prop = configElement.getConfigProp();
		
		String defaultValueStr = "";
		Object defaultValue = configElement.getDefaultValue();
		if (defaultValue != null) {
			if (defaultValue.getClass().isArray()) {
				for (int i = 0; i < Array.getLength(defaultValue); i++) {
					defaultValueStr += ", [" + Array.get(defaultValue, i).toString() + "]";
				}
				defaultValueStr = defaultValueStr.replaceFirst(", ", "");
			} else {
				defaultValueStr = configElement.getDefaultValue().toString();
			}
		}
		if (defaultValueStr.length() > 27) {
			defaultValueStr = defaultValueStr.substring(0, 25) + "...";
		}
		
		this.toolTip.add (EnumChatFormatting.GREEN + configElement.getName());
		if (!prop.info().equals("")) {
			this.toolTip.add (EnumChatFormatting.YELLOW + prop.info());
		}
		if (!prop.minValue().equals("") || !prop.minValue().equals("")) {
			this.toolTip.add (EnumChatFormatting.AQUA + I18n.format("fml.configgui.tooltip.defaultNumeric", configElement.getMin(), configElement.getMax(), defaultValueStr));
		} else {
			this.toolTip.add (EnumChatFormatting.AQUA + I18n.format("fml.configgui.tooltip.default", defaultValueStr));
		}
		
		this.undoHoverChecker = new HoverChecker(this.btUndo, 800);
		this.defaultHoverChecker = new HoverChecker(this.btReset, 800);
	}
	
	public int getLabelWidth() {
		if (!this.getLabelDisplay()) {
			return 0;
		}
		return mc.fontRenderer.getStringWidth(this.getLabel());
	}
	
	public String getLabel () {
		
		String key = "config."+this.configElement.getName();
		mods.gollum.core.common.i18n.I18n i18n = this.parent.parent.mod.i18n();
		
		if (i18n.transExist(key)) {
			return i18n.trans(key);
		}
		return this.configElement.getName();
	}

	public String getName() {
		return this.configElement.getName();
	}
	
	public boolean getLabelDisplay () {
		return this.labelDisplay && this.parent.parent.displayEntriesLabel();
	}
	
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		
		this.posY = y;
		
		if (this.getLabelDisplay()) {
			
			String label = (!this.isValidValue() ? EnumChatFormatting.RED.toString() : (this.isChanged() ? EnumChatFormatting.WHITE.toString() : EnumChatFormatting.GRAY.toString())) + (this.isChanged() ? EnumChatFormatting.ITALIC.toString() : "") + this.getLabel();
			
			this.mc.fontRenderer.drawString(label, this.parent.labelX, y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2, 0xFFFFFF);
		}
		
		int posArBt = 66;
		if (this.parent.parent.canRemove ()) {
			this.btnRemove.xPosition = this.parent.scrollBarX - posArBt;
			this.btnRemove.yPosition = y;
			this.btnRemove.drawButton(this.mc, mouseX, mouseY);
			posArBt += 22;
		}
		
		if (this.parent.parent.canAdd ()) {
			this.btnAdd.xPosition = this.parent.scrollBarX - posArBt;
			this.btnAdd.yPosition = y;
			this.btnAdd.drawButton(this.mc, mouseX, mouseY);
		}
		
		this.btUndo.xPosition = this.parent.scrollBarX - 44;
		this.btUndo.yPosition = y;
		this.btUndo.enabled = this.enabled() && this.isChanged();
		this.btUndo.drawButton(this.mc, mouseX, mouseY);
		
		this.btReset.xPosition = this.parent.scrollBarX - 22;
		this.btReset.yPosition = y;
		this.btReset.enabled = this.enabled() && !this.isDefault();
		this.btReset.drawButton(this.mc, mouseX, mouseY);
		
		if (this.tooltipHoverChecker == null) {
			this.tooltipHoverChecker = new HoverChecker(y, y + slotHeight, x, this.parent.controlX + this.parent.controlWidth - 8, 800);
		} else {
			this.tooltipHoverChecker.updateBounds(y, y + slotHeight, x, this.parent.controlX + this.parent.controlWidth - 8 - 8);
		}
	}
	
	public abstract Object getValue();
	
	public abstract ConfigEntry setValue(Object value);
	
	public boolean enabled() {
		return this.mc.theWorld != null ? !this.configElement.getConfigProp().worldRestart() : true;
	}
	
	public boolean isChanged () {
		return !(this.configElement != null && this.equals (this.configElement.getValue()));
	}
	
	public boolean isDefault () {
		return this.configElement != null && this.equals (this.configElement.getDefaultValue());
	}
	
	public boolean isValidValue() {
		return true;
	}

	@Override
	public boolean equals (Object value) {
		Object cValue = this.getValue();
		
		if (value == null || cValue == null) {
			return value == this.getValue();
		}
		
		if (value instanceof ConfigEntry) {
			return ((ConfigEntry)value).getValue().equals(cValue);
		}
		if (value.getClass().isArray() && cValue.getClass().isArray()) {
			
			int size = Array.getLength(value);
			if (size != Array.getLength(cValue)) {
				return false;
			}
			for (int i = 0; i < size;i++) {
				Object val = Array.get(value, i);
				if (!val.equals(Array.get(cValue, i))) {
					return false;
				}
			}
			return true;
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
		
		if (this.btnAdd.mousePressed(this.mc, x, y)) {
			btReset.func_146113_a(mc.getSoundHandler());
			this.parent.add(index);
			return true;
		} else
		if (this.btnRemove.mousePressed(this.mc, x, y)) {
			btReset.func_146113_a(mc.getSoundHandler());
			this.parent.remove(index);
			return true;
		} else
		if (this.btReset.mousePressed(this.mc, x, y)) {
			btReset.func_146113_a(mc.getSoundHandler());
			this.setToDefault();
			return true;
		} else
		if (this.btUndo.mousePressed(this.mc, x, y)) {
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
	
	public void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY) {
	}

	public void setSlot(int slotIndex) {
	}
	
	public void drawToolTip(int mouseX, int mouseY) {
		boolean canHover = mouseY < this.parent.bottom && mouseY > this.parent.top;
		log.debug(this.posY, canHover);
		if (toolTip.size() > 0 && this.tooltipHoverChecker != null) {
			if (this.tooltipHoverChecker.checkHover(mouseX, mouseY, canHover)) {
				this.parent.parent.drawToolTip(toolTip, mouseX, mouseY);
			}
		}

		if (this.undoHoverChecker.checkHover(mouseX, mouseY, canHover)) {
			this.parent.parent.drawToolTip(undoToolTip, mouseX, mouseY);
		}
		if (this.defaultHoverChecker.checkHover(mouseX, mouseY, canHover)) {
			this.parent.parent.drawToolTip(defaultToolTip, mouseX, mouseY);
		}
	}
	
}
