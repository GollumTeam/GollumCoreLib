package com.gollum.core.client.gui.config;

import static com.gollum.core.ModGollumCoreLib.logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.gollum.core.client.gui.config.element.CategoryElement;
import com.gollum.core.client.gui.config.entry.SubConfigEntry;
import com.gollum.core.common.config.ConfigLoader;
import com.gollum.core.common.config.ConfigLoader.ConfigLoad;

import net.minecraftforge.fml.client.config.GuiMessageDialog;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraft.client.resources.I18n;

public class GuiSubConfigConfig extends GuiConfig {
	
	private ArrayList<ConfigLoad> subConfigLoaded;
	private SubConfigEntry subConfigEntry;
	
	public GuiSubConfigConfig(SubConfigEntry subConfigEntry) {
		super(subConfigEntry.parent.parent);

		this.titleLine2   = subConfigEntry.getLabel();
		
		this.subConfigEntry = subConfigEntry;
	}
	
	@Override
	protected void initConfigElement() {
		
		this.subConfigLoaded = ConfigLoader.getSubConfig(this.mod);
		
		for (ConfigLoad configLoad : this.subConfigLoaded) {
			configElements.add(new CategoryElement(this.subConfigEntry.getName(), configLoad, configLoad.config.getFileName()));
		}
	}
	
	@Override
	public void displayParent() {
		if (!this.displayRestart()) {
			super.displayParent();
		}
	}
	
	@Override
	public void saveValue() {
		
		logger.info("Save configuration "+this.getMod().getModId()+" > "+this.subConfigEntry.getLabel());
		
		for (Entry<String, Object> entry : this.entryList.getValues().entrySet()) {
			String fileName = entry.getKey();
			for (ConfigLoad configLoad : this.subConfigLoaded) {
				
				if (fileName.equals(configLoad.config.getFileName())) {
					logger.info("Save file: "+fileName);
					configLoad.saveValue((LinkedHashMap<String, Object>) entry.getValue());
					new ConfigLoader(configLoad.config, false).writeConfig();
				}
			}
		}
		
	}
	
}
