//package com.gollum.core.client.gui.config.entry;
//
//import com.gollum.core.client.gui.config.GuiConfigEntries;
//import com.gollum.core.client.gui.config.element.ConfigElement;
//
//import net.minecraft.client.Minecraft;
//
//public class DoubleEntry extends StringEntry {
//	
//	public DoubleEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
//		super(index, mc, parent, configElement);
//	}
//	
//	@Override
//	public Object getValue() {
//		
//		super.getValue();
//		
//		Double value = new Double(0D);
//		try {
//			value = Double.parseDouble(this.textFieldValue.getText());
//		} catch (Exception e) {
//		}
//		
//		return value;
//	}
//	
//	private double getDoubleValue () {
//		return new Double(this.getValue().toString());
//	}
//	
//	@Override
//	public void keyTyped(char eventChar, int eventKey) {
//		if (eventChar == ',') {
//			eventChar = '.';
//		}
//		super.keyTyped(eventChar, eventKey);
//	}
//	
//	@Override
//	protected boolean validKeyTyped(char eventChar) {
//		if (eventChar <= 31 || (eventChar >= '0' && eventChar <='9') || eventChar == '-' || eventChar == '.') {
//			return true;
//		}
//		return false;
//	}
//	
//	@Override
//	public boolean isValidValue() {
//		
//		Double min = (Double)this.configElement.getMin();
//		Double max = (Double)this.configElement.getMax();
//		
//		Double val = null;
//		try {
//			val = Double.parseDouble(this.textFieldValue.getText());
//		} catch (Exception e) {
//		}
//		
//		return 
//			val != null &&
//			this.getDoubleValue() >= min &&
//			this.getDoubleValue() <= max &&
//			this.respectPattern()
//		;
//	}
//
//}
