//package com.gollum.core.client.gui.config.entry;
//
//import com.gollum.core.client.gui.config.GuiConfigEntries;
//import com.gollum.core.client.gui.config.GuiModIdConfig;
//import com.gollum.core.client.gui.config.element.ConfigElement;
//
//import net.minecraft.client.Minecraft;
//
//public class ModIdEntry extends ListEntry {
//
//	public ModIdEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
//		super(index, mc, parent, configElement);
//	}
//	
//	@Override
//	public void valueButtonPressed(int slotIndex) {
//		this.mc.displayGuiScreen(new GuiModIdConfig(this));
//	}
//	
//	@Override
//	public boolean hasSearch () {
//		return true;
//	}
//}
