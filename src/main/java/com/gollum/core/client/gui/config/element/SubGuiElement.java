//package com.gollum.core.client.gui.config.element;
//
//import java.util.HashMap;
//
//import com.gollum.core.client.gui.config.entry.ConfigEntry;
//import com.gollum.core.client.gui.config.entry.SubGuiEntry;
//import com.gollum.core.common.config.ConfigLoader.ConfigLoad;
//import com.gollum.core.common.config.ConfigProp;
//import com.gollum.core.common.config.JsonConfigProp;
//
//import net.minecraft.client.gui.GuiScreen;
//
//public class SubGuiElement extends ConfigElement {
//
//	private ConfigLoad configLoad;
//	private HashMap<String, Class> types = new HashMap<String, Class>();
//	private HashMap<String, ConfigProp> props = new HashMap<String, ConfigProp>();
//	
//	public SubGuiElement(String name, Class<? extends GuiScreen> gui) {
//		super(name);
//		this.value = gui;
//	}
//	
//	@Override
//	public ConfigProp getConfigProp() {
//		return new JsonConfigProp();
//	}
//
//	@Override
//	public Class< ? extends ConfigEntry> getEntryClass() {
//		return SubGuiEntry.class;
//	}
//	
//}
