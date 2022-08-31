//package com.gollum.core.client.gui.config.entry;
//
//import com.gollum.core.client.gui.config.GuiConfigEntries;
//import com.gollum.core.client.gui.config.GuiItemConfig;
//import com.gollum.core.client.gui.config.element.ConfigElement;
//
//import net.minecraft.client.Minecraft;
//
//public class ItemEntry extends ListEntry {
//
//	public ItemEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
//		super(index, mc, parent, configElement);
//	}
//	
//	@Override
//	public void valueButtonPressed(int slotIndex) {
//		this.mc.displayGuiScreen(new GuiItemConfig(this));
//	}
//	
//	@Override
//	public boolean hasSearch () {
//		return true;
//	}
//	
//}
