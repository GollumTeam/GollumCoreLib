//package com.gollum.core.client.gui.config.entry;
//
//import com.gollum.core.client.gui.config.GuiConfigEntries;
//import com.gollum.core.client.gui.config.element.ConfigElement;
//
//import net.minecraft.client.Minecraft;
//
//public class LongEntry extends StringEntry {
//	
//	public LongEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
//		super(index, mc, parent, configElement);
//	}
//	
//	@Override
//	public Object getValue() {
//		super.getValue();
//		Long value = new Long(0L);
//		try {
//			value = Long.parseLong(this.textFieldValue.getText());
//		} catch (Exception e) {
//		}
//		
//		return value;
//	}
//	
//	protected long getLongValue () {
//		return new Long(this.getValue().toString());
//	}
//	
//	@Override
//	protected boolean validKeyTyped(char eventChar) {
//		if (eventChar <= 31 || (eventChar >= '0' && eventChar <='9') || eventChar == '-') {
//			return true;
//		}
//		return false;
//	}
//	
//	@Override
//	public boolean isValidValue() {
//		
//		Long min = 0L;
//		Long max = 0L;
//		try {
//			min = (Long)this.configElement.getMin();
//		} catch (Exception e) {
//			min = ((Double)this.configElement.getMin()).longValue();
//		}
//		try {
//			max = (Long)this.configElement.getMax();
//		} catch (Exception e) {
//			max = ((Double)this.configElement.getMax()).longValue();
//		}
//		
//		return 
//			this.textFieldValue.getText().equals(this.getValue().toString()) &&
//			this.getLongValue() >= min &&
//			this.getLongValue() <= max &&
//			this.respectPattern()
//		;
//	}
//
//}
