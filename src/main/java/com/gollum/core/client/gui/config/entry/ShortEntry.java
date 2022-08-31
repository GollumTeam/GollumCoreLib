//package com.gollum.core.client.gui.config.entry;
//
//import com.gollum.core.client.gui.config.GuiConfigEntries;
//import com.gollum.core.client.gui.config.element.ConfigElement;
//
//import net.minecraft.client.Minecraft;
//
//public class ShortEntry extends IntegerEntry {
//	
//	public ShortEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
//		super(index, mc, parent, configElement);
//	}
//	
//	@Override
//	public Object getValue() {
//		super.getValue();
//		Short value = new Short((short) 0);
//		try {
//			value = Short.parseShort(this.textFieldValue.getText());
//		} catch (Exception e) {
//		}
//		
//		return value;
//	}
//
//}
