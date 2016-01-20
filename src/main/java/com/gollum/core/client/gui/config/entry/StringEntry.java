package com.gollum.core.client.gui.config.entry;

import static com.gollum.core.ModGollumCoreLib.log;

import org.lwjgl.input.Keyboard;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.common.config.ConfigProp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class StringEntry extends ConfigEntry {

	protected GuiTextField textFieldValue;
	private boolean first = true;
	
	public StringEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		this.textFieldValue = new GuiTextField(0, this.mc.fontRendererObj, this.parent.controlX + 1, 0, this.parent.controlWidth - 3, 16);
		this.textFieldValue.setMaxStringLength(10000);
		this.setValue(this.configElement.getValue().toString());
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight , int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth) {
		
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, resetControlWidth);
		
		if (this.first) {
			this.first = false;
			this.textFieldValue.setCursorPositionZero();
		}
		
		this.textFieldValue.xPosition = this.parent.controlX + 2;
		this.textFieldValue.yPosition = y + 1;
		this.textFieldValue.width = this.parent.controlWidth - 4;
		this.textFieldValue.setEnabled(this.enabled());
		this.textFieldValue.drawTextBox();
		
	}
	
	 @Override
	public void keyTyped(char eventChar, int eventKey) {
		if ((this.enabled() && this.validKeyTyped(eventChar)) || (eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)) {
			this.textFieldValue.textboxKeyTyped((enabled() ? eventChar : Keyboard.CHAR_NONE), eventKey);
		}
	}
	
	protected boolean validKeyTyped(char eventChar) {
		if (eventChar > 31) {
			Long max = (Long)this.configElement.getMax();
			if (max != null) {
				return ((String)this.getValue()).length() < max;
			}
		} else {
			if (eventChar == 8) {
				Long min = (Long)this.configElement.getMin();
				log.debug ( min);
				if (min != null) {
					return ((String)this.getValue()).length() > min;
				}
			}
		}
		
		return true;
	}
	
	@Override
	public Object getValue() {
		super.getValue();
		return textFieldValue.getText();
	}
	
	@Override
	public ConfigEntry setValue(Object value) {
		this.textFieldValue.setText(value.toString());
		return super.setValue(value);
	}
	
	@Override
	public void updateCursorCounter() {
		this.textFieldValue.updateCursorCounter();
	}
	
	@Override
	public void mouseClicked(int x, int y, int mouseEvent) {
		this.textFieldValue.mouseClicked(x, y, mouseEvent);
		super.mouseClicked(x, y, mouseEvent);
	}
	
	protected boolean respectPattern () {
		
		ConfigProp prop = this.configElement.getConfigProp();
		if (prop.pattern().equals("")) {
			return true;
		}
		
		return this.getValue().toString().matches(prop.pattern());
	}
	
	@Override
	public boolean isValidValue() {
		
		Long max = (Long)this.configElement.getMax();
		if (max != null) {
			if (((String)this.getValue()).length() > max) {
				return false;
			}
		}
		Long min = (Long)this.configElement.getMin();
		if (min != null) {
			if (((String)this.getValue()).length() < min) {
				return false;
			}
		}
		return this.respectPattern();
	}
}
