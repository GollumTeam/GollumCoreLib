package mods.gollum.core.client.gui.config.entry;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.client.config.HoverChecker;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.client.gui.config.element.TypedValueElement;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.JsonConfigProp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public abstract class ConfigEntry implements IGuiListEntry {
	
	public abstract static class Event {
		
		public ConfigEntry target;
		
		enum Type {
			CHANGE,
			SET_VALUE,
			GET_VALUE
		}
		
		public abstract void call (Type type, Object ... params);
	}
	
	private ArrayList<Event> events = new ArrayList<Event>();
	
	protected Minecraft mc;
	public GuiConfigEntries parent;
	public ConfigElement configElement;
	
	public    GuiButtonExt btnAdd;
	protected GuiButtonExt btnRemove;
	protected GuiButtonExt btUndo;
	protected GuiButtonExt btReset;
	public int index;
	
	public boolean btUndoIsVisible  = true;
	public boolean btResetIsVisible = true;
	
	public boolean formatedLabel = true;
	
	protected List<String> toolTip        = new ArrayList<String>();
	protected List<String> undoToolTip    = new ArrayList<String>();
	protected List<String> defaultToolTip = new ArrayList<String>();
	
	protected HoverChecker tooltipHoverChecker;
	protected HoverChecker undoHoverChecker;
	protected HoverChecker defaultHoverChecker;
	
	protected boolean labelDisplay = true;
	
	protected boolean eventGetCall   = false;
	protected boolean eventSetCall   = false;
	protected boolean eventChangeCall= false;
	
	private Object oldValue = null;
	
	private ArrayList<ConfigEntry> subEntries = new ArrayList<ConfigEntry>();
	
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
	
	public String tradIfExist (String name) {
		
		String key = "config."+name;
		mods.gollum.core.common.i18n.I18n i18n = this.parent.parent.mod.i18n();
		
		if (i18n.transExist(key)) {
			return i18n.trans(key);
		}
		return name;
	}
	
	public String getLabel () {
		return this.tradIfExist(this.configElement.getName());
	}

	public String getName() {
		return this.configElement.getName();
	}
	
	public boolean getLabelDisplay () {
		return this.labelDisplay && this.parent.parent.displayEntriesLabel();
	}

	
	public final void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		this.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected, true);
	}
	
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth) {
		
		if (this.getLabelDisplay()) {
			String label = this.getLabel();
			
			if (this.formatedLabel) {
				label = (!this.isValidValue() ? EnumChatFormatting.RED.toString() : (this.isChanged() ? EnumChatFormatting.WHITE.toString() : EnumChatFormatting.GRAY.toString())) + (this.isChanged() ? EnumChatFormatting.ITALIC.toString() : "") + label;
			}
			
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
		
		this.btUndo.visible  = this.btUndoIsVisible  && this.parent.parent.undoIsVisible();
		this.btReset.visible = this.btResetIsVisible && this.parent.parent.resetIsVisible();
		
		if (resetControlWidth) {
			this.resetControlWidth();
		}
		
		this.btUndo.xPosition = this.parent.scrollBarX - 44;
		this.btUndo.yPosition = y;
		this.btUndo.enabled = this.enabled() && this.isChanged() && this.parent.parent.undoIsVisible();
		this.btUndo.drawButton(this.mc, mouseX, mouseY);
		
		this.btReset.xPosition = this.parent.scrollBarX - 22;
		this.btReset.yPosition = y;
		this.btReset.enabled = this.enabled() && !this.isDefault() && this.parent.parent.resetIsVisible();
		this.btReset.drawButton(this.mc, mouseX, mouseY);
		
		if (this.tooltipHoverChecker == null) {
			this.tooltipHoverChecker = new HoverChecker(y, y + slotHeight, x, this.parent.controlX + this.parent.controlWidth - 8, 800);
		} else {
			this.tooltipHoverChecker.updateBounds(y, y + slotHeight, x, this.parent.controlX + this.parent.controlWidth - 8 - 8);
		}
		
	}
	
	protected void resetControlWidth() {
		if (!this.btUndo.visible && !this.btReset.visible) {
			this.parent.controlWidth = this.parent.scrollBarX - 5- this.parent.controlX;
		} else {
			this.parent.initGui();
		}
	}
	
	
	protected void drawRec(int x, int y, int width, int height, int color1) {
		this.drawRec(x, y, width, height, color1, color1);
	}
	
	protected void drawRec(int x, int y, int width, int height, int color1, int color2) {
		
		this.parent.parent.drawGradientRect(
			x, 
			y, 
			x + width, 
			y + height, 
			color1, 
			color2
		);
		
	}
	
	public Object getValue() {
		if (!this.eventGetCall) {
			this.eventGetCall = true;
			this.fireEvent(Event.Type.SET_VALUE);
			this.eventGetCall = false;
		}
		
		return null;
	}
	
	public ConfigEntry setValue(Object value) {
		if (!this.eventSetCall) {
			this.eventSetCall = true;
			this.fireEvent(Event.Type.GET_VALUE);
			this.eventSetCall = false;
		}
		
		if (!this.eventChangeCall && (this.oldValue == null || !this.equals(this.oldValue))) {
			this.eventChangeCall = true;
			this.fireEvent(Event.Type.CHANGE);
			this.eventChangeCall = false;
		}
		this.oldValue = this.getValue();
		
		return this;
	}
	
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
		
		for (ConfigEntry subEntry : this.subEntries) {
			if (subEntry.mousePressed(index, x, y, mouseEvent, relativeX, relativeY)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		for (ConfigEntry subEntry : this.subEntries) {
			subEntry.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
		}
	}
	
	public void keyTyped(char eventChar, int eventKey){
		for (ConfigEntry subEntry : this.subEntries) {
			subEntry.keyTyped(eventChar, eventKey);
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		for (ConfigEntry subEntry : this.subEntries) {
			subEntry.mouseClicked(mouseX, mouseY, mouseEvent);
		}
	}
	
	public void updateCursorCounter() {
		for (ConfigEntry subEntry : this.subEntries) {
			subEntry.updateCursorCounter();
		}
	}
	
	public void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY) {
		for (ConfigEntry subEntry : this.subEntries) {
			subEntry.elementClicked(slotIndex, doubleClick, mouseX, mouseY);
		}
	}

	public void setSlot(int slotIndex) {
	}
	
	public void drawToolTip(int mouseX, int mouseY) {
		int posYReal = (this.index * this.parent.getSlotHeight()) - this.parent.getAmountScrolled();
		boolean canHover = mouseY < this.parent.bottom && mouseY > this.parent.top && posYReal >= 0 && posYReal <= this.parent.height ;
		
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
		
		for (ConfigEntry subEntry : this.subEntries) {
			subEntry.drawToolTip(mouseX, mouseY);
		}
	}
	
	protected ConfigEntry createSubEntry(String label, Object value, Object defaultValue, int index) {
		return this.createSubEntry(label, value, defaultValue, index, new JsonConfigProp());
	}
	
	protected ConfigEntry createSubEntry(String label, Object value, Object defaultValue, int index, ConfigProp prop) {
		
		TypedValueElement configElement = new TypedValueElement(value.getClass(), label, value, defaultValue, prop);
		ConfigEntry configEntry  = this.parent.newInstanceOfEntryConfig(index, configElement, prop);
		
		if (configEntry != null) {
			this.subEntries.add (configEntry);
		}
		return configEntry;
	}
	
	public void addEvent (Event e) {
		e.target = this;
		this.events.add(e);
	}
	
	public void fireEvent (Event.Type type, Object... params) {
		for (Event e : this.events) {
			e.call(type, params);
		}
	}
}
