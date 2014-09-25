package mods.gollum.core.client.gui.config.entries.entry;

import java.awt.Color;

import mods.gollum.core.client.gui.config.GuiEditCustomArray;
import mods.gollum.core.client.gui.config.GuiJsonConfig;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.ButtonEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class JsonEntry extends ButtonEntry implements IGollumConfigEntry {

	protected final Json defaultValue;
	protected final Json beforeValue;
	protected Json currentValue;
	
	protected final GuiButtonExt btnPlus;
	protected final GuiButtonExt btnMinus;
	
	public JsonEntry(GuiConfig parent, GuiConfigEntries entryList, IConfigElement<String> configElement) {
		super(parent, entryList, configElement);
		
		defaultValue  = (Json)this.configElement.getDefault();
		beforeValue   = (Json)configElement.get();
		currentValue  = (Json)configElement.get();
		
		updateValueButtonText();
		
		this.btnPlus  = new GuiButtonExt(0, 0, 0, 18, 18, Color.GREEN + "+");
		this.btnMinus = new GuiButtonExt(0, 0, 0, 18, 18, Color.RED   + "-");
		
	}
	
	public boolean parentIsArray () {
		return this.owningScreen instanceof GuiEditCustomArray;
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		
		if (this.parentIsArray()) {
			this.owningScreen.entryList.controlX -= 44;
			this.owningEntryList.scrollBarX -= 44;
		}
		
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
		
		if (this.parentIsArray()) {
			
			this.owningEntryList.scrollBarX += 44;
			this.owningScreen.entryList.controlX += 44;
			
			this.btnPlus.xPosition = this.owningEntryList.scrollBarX - 44;
			this.btnPlus.yPosition = y;
			this.btnPlus.drawButton(this.mc, mouseX, mouseY);
			
			this.btnMinus.xPosition = this.owningEntryList.scrollBarX - 22;
			this.btnMinus.yPosition = y;
			this.btnMinus.drawButton(this.mc, mouseX, mouseY);
		}
	}
	
	@Override
	public void updateValueButtonText() {
		this.btnValue.displayString = currentValue.toString(); 
	}

	@Override
	public void valueButtonPressed(int slotIndex) {
		Json d  = (Json)this.configElement.get();
		Json dv = (Json)this.configElement.getDefault();
		
		mc.displayGuiScreen(new GuiJsonConfig(this.owningScreen, this, currentValue, defaultValue));
	}

	public void setValueFromChildScreen(Object newValue) {
		if (enabled() && currentValue != null ? !currentValue.equals((Json)newValue) : newValue != null) {
			currentValue = (Json) newValue;
			updateValueButtonText();
		}
	}

	@Override
	public boolean isDefault() {
		return defaultValue.equals(currentValue);
	}

	@Override
	public void setToDefault() {
		if (enabled()) {
			this.currentValue = defaultValue;
			updateValueButtonText();
		}
	}

	@Override
	public boolean isChanged() {
		if (beforeValue != null)
			return !beforeValue.equals(currentValue);
		else
			return currentValue == null;
	}

	@Override
	public void undoChanges() {
		if (enabled()) {
			currentValue = beforeValue;
			updateValueButtonText();
		}
	}

	@Override
	public boolean saveConfigElement() {
		if (enabled() && isChanged()) {
			this.configElement.set(currentValue);
			return configElement.requiresMcRestart();
		}
		return false;
	}

	@Override
	public String getCurrentValue() {
		return this.currentValue.toString();
	}
	
	public Json getValue() {
		return this.currentValue;
	}

	@Override
	public String[] getCurrentValues() {
		return new String[] { getCurrentValue() };
	}
}