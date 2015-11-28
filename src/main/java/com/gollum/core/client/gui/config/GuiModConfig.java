package com.gollum.core.client.gui.config;

import java.util.ArrayList;

import com.gollum.core.client.gui.config.element.SubConfigElement;
import com.gollum.core.client.gui.config.element.SubGuiElement;
import com.gollum.core.common.config.ConfigLoader;
import com.gollum.core.common.config.ConfigLoader.ConfigLoad;

import net.minecraft.client.gui.GuiScreen;

public class GuiModConfig extends GuiConfig {
	
	private ArrayList<ConfigLoad> subConfigLoaded;
	
	public GuiModConfig(GuiScreen parent) {
		super(parent);
	}
	
	@Override
	protected void initConfigElement() {
		
		this.configElements.add(new SubGuiElement("Configuration", GuiCategoryConfig.class));
		
		this.subConfigLoaded = ConfigLoader.getSubConfig(this.mod);
		
		if (this.mustntDisplay ()) {
			this.mc.displayGuiScreen(new GuiCategoryConfig(this));
			return;
		}
		
		ArrayList<String> categories = new ArrayList<String>();
		
		for (ConfigLoad configLoad : this.subConfigLoaded) {
			ArrayList<String> cats = configLoad.getCategories();
			for (String cat: cats) {
				if (!categories.contains(cat)) {
					categories.add(cat);
				}
			}
		}
		
		for (String category : categories) {
			configElements.add(new SubConfigElement(category, this.subConfigLoaded));
		}
	}
	
	@Override
	public boolean resetIsVisible() {
		return false;
	}
	
	@Override
	public boolean undoIsVisible() {
		return false;
	}
	
	@Override
	public boolean mustntDisplay () {
		return this.subConfigLoaded.size() == 0;
	}
	
	@Override
	public void saveValue() {
	}
	
}
