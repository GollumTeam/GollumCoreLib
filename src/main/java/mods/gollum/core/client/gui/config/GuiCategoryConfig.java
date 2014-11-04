package mods.gollum.core.client.gui.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import mods.gollum.core.client.gui.config.element.CategoryElement;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import net.minecraft.client.gui.GuiScreen;

public class GuiCategoryConfig extends GuiConfig {
	
	private ConfigLoad configLoad;
	
	public GuiCategoryConfig(GuiScreen parent) {
		super(parent);
	}
	
	@Override
	protected void initConfigElement() {
		
		this.configLoad = ConfigLoader.configLoaded.get(this.mod);
		
		ArrayList<String> categories = configLoad.getCategories();
		for (String category : categories) {
			configElements.add(new CategoryElement (category, this.configLoad));
		}
	}
	
}
