package mods.gollum.core.client.gui.config.entry;

import static mods.gollum.core.ModGollumCoreLib.log;
import net.minecraft.client.renderer.Tessellator;
import mods.gollum.core.client.gui.config.GuiEditCustomArray;
import mods.gollum.core.client.gui.config.entry.logic.GuiArrayButtonEntryLogic;
import mods.gollum.core.common.config.type.ItemStackConfigType;
import mods.gollum.core.tools.simplejson.Json;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;


public class ArrayEntry extends cpw.mods.fml.client.config.GuiConfigEntries.ArrayEntry implements IGollumConfigEntry {
	
	protected GuiArrayButtonEntryLogic logic = new GuiArrayButtonEntryLogic(this);
	
	public ArrayEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
		super(owningScreen, owningEntryList, configElement);
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		logic.drawEntryBefore();
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
		logic.drawEntryAfter(x, y, mouseX, mouseY);
	}
	
	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY) ? true : this.logic.mousePressed(index, x, y);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		mc.displayGuiScreen(new GuiEditCustomArray(this.owningScreen, slotIndex, this, currentValues, this.configElement.getDefaults()));
	}
	
	/**
	 * Fired when the mouse button is released. Arguments: index, x, y,
	 * mouseEvent, relativeX, relativeY
	 */
	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
		this.logic.mouseReleased(x, y);
	}
	
	@Override
	public void setValueFromChildScreen(Object newValue) {
		
		if (enabled() && newValue != null && newValue.getClass().isArray()) {
			this.currentValues = (Object[]) newValue;
			updateValueButtonText();
		}
		
	}
	
	public Object createNewLine() {
		
		Class subType    = this.configElement.getDefaults().getClass().getComponentType();
		Class entryClass = this.configElement.getArrayEntryClass();
		
		log.debug ("Add : "+subType.getName());
		// TODO other type for add
		if (ItemStackConfigType.class.isAssignableFrom(subType)) {
			try {
				return subType.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else 
		
		if (StringEntry .class.isAssignableFrom(entryClass)) { return "";   } else 
		if (IntegerEntry.class.isAssignableFrom(entryClass)) { return 0;    } else 
		if (DoubleEntry .class.isAssignableFrom(entryClass)) { return 0.D;  } else 
		if (BooleanEntry.class.isAssignableFrom(entryClass)) { return true; }
		
		return null;
	}
	
	public Object[] getBeforeValues () {
		return this.beforeValues;
	}
}
