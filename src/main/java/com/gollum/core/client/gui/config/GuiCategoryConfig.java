//package com.gollum.core.client.gui.config;
//
//import static com.gollum.core.ModGollumCoreLib.log;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import com.gollum.core.client.gui.config.element.CategoryElement;
//import com.gollum.core.client.gui.config.entry.CategoryEntry;
//import com.gollum.core.common.config.ConfigLoader;
//import com.gollum.core.common.config.ConfigLoader.ConfigLoad;
//
//import net.minecraftforge.fml.client.config.GuiMessageDialog;
//import net.minecraftforge.fml.client.event.ConfigChangedEvent;
//import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
//import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
//import net.minecraftforge.fml.common.FMLCommonHandler;
//import net.minecraftforge.fml.common.eventhandler.Event.Result;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.util.ChatComponentText;
//
//public class GuiCategoryConfig extends GuiConfig {
//	
//	private ConfigLoad configLoad;
//	
//	public GuiCategoryConfig(GuiScreen parent) {
//		super(parent);
//		this.titleLine2 = "Configuration";
//	}
//	
//	@Override
//	protected void initConfigElement() {
//		
//		this.configLoad = ConfigLoader.configLoaded.get(this.mod);
//		
//		ArrayList<String> categories = this.configLoad.getCategories();
//		
//		for (String category : categories) {
//			configElements.add(new CategoryElement (category, this.configLoad));
//		}
//	}
//	
//	@Override
//	public void initGui() {
//		super.initGui();
//		
//		if (this.mustntDisplay()) {
//			this.mc.displayGuiScreen(new GuiValueConfig((CategoryEntry)this.entryList.getEntry(0)));
//			return;
//		}
//	}
//	
//	@Override
//	public boolean mustntDisplay () {
//		return this.configLoad.getCategories().size() == 1;
//	}
//	
//	@Override
//	public void displayParent() {
//		if (!this.displayRestart()) {
//			super.displayParent();
//		}
//	}
//	
//	@Override
//	public void saveValue() {
//		log.info("Save configuration "+this.getMod().getModId());
//		
//		LinkedHashMap<String, Object> values = new LinkedHashMap<String, Object>();
//		for (Entry<String, Object> entry : this.entryList.getValues().entrySet()) {
//			values.putAll((Map<? extends String, ? extends Object>) entry.getValue());
//		}
//		
//		this.configLoad.saveValue(values);
//		new ConfigLoader(configLoad.config, false).writeConfig();
//		
//	}
//	
//}
