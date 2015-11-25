package com.gollum.core.client.gui.config.entry;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.config.GuiSlider;

public class SliderEntry extends ConfigEntry {
	
	private GuiSlider slider;
	
	public SliderEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		boolean floating = 
			Double.class.isAssignableFrom(configElement.getValue().getClass()) || Double.TYPE.isAssignableFrom(configElement.getValue().getClass()) ||
			Float.class.isAssignableFrom(configElement.getValue().getClass())  || Float.TYPE.isAssignableFrom(configElement.getValue().getClass())
		;

		Double min = 0.0D;
		Double max = 0.0D;
		try {
			min = (Double)configElement.getMin();
		} catch (Exception e) {
			min = ((Long)configElement.getMin()).doubleValue();
		}
		try {
			max = (Double)configElement.getMax();
		} catch (Exception e) {
			max = ((Long)configElement.getMax()).doubleValue();
		}
		
		this.slider = new GuiSlider(
			0, this.parent.controlX, 
			0, this.parent.controlWidth,
			18, 
			"", "",
			(Double)min, 
			(Double)max, 
			0.0D, 
			floating, 
			true
		);
		this.setValue(this.configElement.getValue());
	}
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight , int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth) {
		
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, resetControlWidth);
		
		this.slider.width = this.parent.controlWidth;
		this.slider.xPosition = this.parent.controlX;
		this.slider.yPosition = y;
		this.slider.enabled = this.enabled();
		this.slider.drawButton(this.mc, mouseX, mouseY);
	}
	
	@Override
	public Object getValue() {
		super.getValue();
		try {
			if (Double.class.isAssignableFrom(this.configElement.getValue().getClass()) || Double.TYPE.isAssignableFrom(this.configElement.getValue().getClass())) {
				return new Double (this.slider.getValue());
			} else
			if (Float.class.isAssignableFrom(this.configElement.getValue().getClass()) || Float.TYPE.isAssignableFrom(this.configElement.getValue().getClass())) {
				return (float)this.slider.getValue();
			} else
			if (Long.class.isAssignableFrom(this.configElement.getValue().getClass()) || Long.TYPE.isAssignableFrom(this.configElement.getValue().getClass())) {
				return new Long ((long)Math.round(this.slider.getValue()));
			} else
			if (Integer.class.isAssignableFrom(this.configElement.getValue().getClass()) || Integer.TYPE.isAssignableFrom(this.configElement.getValue().getClass())) {
				return new Integer ((int)Math.round(this.slider.getValue()));
			} else
			if (Short.class.isAssignableFrom(this.configElement.getValue().getClass()) || Short.TYPE.isAssignableFrom(this.configElement.getValue().getClass())) {
				return new Short ((short)Math.round(this.slider.getValue()));
			}else
			if (Byte.class.isAssignableFrom(this.configElement.getValue().getClass()) || Byte.TYPE.isAssignableFrom(this.configElement.getValue().getClass())) {
				return new Byte ((byte)Math.round(this.slider.getValue()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.configElement.getValue();
	}
	
	@Override
	public ConfigEntry setValue(Object value) {
		
		Double valueD = 0.0;
		if (Double.class.isAssignableFrom(configElement.getValue().getClass()) || Double.TYPE.isAssignableFrom(configElement.getValue().getClass())) {
			valueD = (Double)value;
		} else
		if (Float.class.isAssignableFrom(configElement.getValue().getClass()) || Float.TYPE.isAssignableFrom(configElement.getValue().getClass())) {
			valueD = new Double ((Float)value);
		} else
		if (Long.class.isAssignableFrom(configElement.getValue().getClass()) || Long.TYPE.isAssignableFrom(configElement.getValue().getClass())) {
			valueD = new Double ((Long)value);
		} else
		if (Integer.class.isAssignableFrom(configElement.getValue().getClass()) || Integer.TYPE.isAssignableFrom(configElement.getValue().getClass())) {
			valueD = new Double ((Integer)value);
		} else
		if (Short.class.isAssignableFrom(configElement.getValue().getClass()) || Short.TYPE.isAssignableFrom(configElement.getValue().getClass())) {
			valueD = new Double ((Short)value);
		} else
		if (Byte.class.isAssignableFrom(configElement.getValue().getClass()) || Byte.TYPE.isAssignableFrom(configElement.getValue().getClass())) {
			valueD = new Double ((Byte)value);
		}
		
		this.slider.setValue((Double) valueD);
		this.slider.updateSlider();
		
		return super.setValue(value);
	}
	
	
	/////////////
	// ACTIONS //
	/////////////
	
	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		if (this.slider.mousePressed(this.mc, x, y)) {
			slider.playPressSound(mc.getSoundHandler());
			return true;
		}
		return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
	}
	
	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		super.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
		this.slider.mouseReleased(x, y);
	}

}
