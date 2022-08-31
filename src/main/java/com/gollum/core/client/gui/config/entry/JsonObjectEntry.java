//package com.gollum.core.client.gui.config.entry;
//
//import com.gollum.core.client.gui.config.GuiConfigEntries;
//import com.gollum.core.client.gui.config.GuiJsonObjectConfig;
//import com.gollum.core.client.gui.config.element.ConfigElement;
//import com.gollum.core.tools.simplejson.IJsonObjectDisplay;
//import com.gollum.core.tools.simplejson.JsonObject;
//
//import net.minecraft.client.Minecraft;
//
//public class JsonObjectEntry extends ButtonEntry {
//	
//	private JsonObject value;
//	
//	public JsonObjectEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
//		super(index, mc, parent, configElement);
//		
//		this.value = (JsonObject)configElement.getValue();
//		this.updateValueButtonText();
//	}
//
//	public void updateValueButtonText() {
//		
//		IJsonObjectDisplay complement = (IJsonObjectDisplay) this.value.getComplement(IJsonObjectDisplay.class);
//		if (complement != null) {
//			this.updateValueButtonText(complement.display(this.value));
//			return;
//		}
//		this.updateValueButtonText(this.getValue().toString());
//	}
//	
//	@Override
//	public void valueButtonPressed(int slotIndex) {
//		this.mc.displayGuiScreen(new GuiJsonObjectConfig(this));
//	}
//
//	@Override
//	public Object getValue() {
//		super.getValue();
//		return this.value;
//	}
//
//	@Override
//	public ConfigEntry setValue(Object value) {
//		try {
//			this.value = (JsonObject)value;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		this.updateValueButtonText();
//		return super.setValue(value);
//	}
//
//}
