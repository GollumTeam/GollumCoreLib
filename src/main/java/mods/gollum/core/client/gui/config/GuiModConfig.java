package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import mods.gollum.core.client.gui.config.element.CategoryElement;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.config.GuiMessageDialog;

public class GuiModConfig extends GuiConfig {
	
	private ArrayList<ConfigLoad> subConfigLoad;
	
	public GuiModConfig(GuiScreen parent) {
		super(parent);
	}
	
	@Override
	protected void initConfigElement() {
		
		this.subConfigLoad = ConfigLoader.getSubConfig(this.getMod());
		
		
		
//		
//		ArrayList<String> categories = configLoad.getCategories();
//		for (String category : categories) {
//			configElements.add(new CategoryElement (category, this.configLoad));
//		}
	}
	
	@Override
	public void saveValue() {
	}
	
}
