package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import mods.gollum.core.client.gui.config.element.SubConfigElement;
import mods.gollum.core.client.gui.config.element.SubGuiElement;
import mods.gollum.core.client.gui.config.element.CategoryElement;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.config.GuiMessageDialog;

public class GuiModConfig extends GuiConfig {
	
	private ArrayList<ConfigLoad> subConfigLoaded;
	
	public GuiModConfig(GuiScreen parent) {
		super(parent);
	}
	
	@Override
	protected void initConfigElement() {
		
		configElements.add(new SubGuiElement("Configuration", GuiCategoryConfig.class));
		
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
	public void displayParent() {
		if (this.entryList.requiresMcRestart()) {
			this.saveValue ();
			this.mc.displayGuiScreen(new GuiMessageDialog(this.getParent(), "fml.configgui.gameRestartTitle", new ChatComponentText(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
		} else {
			super.displayParent();
		}
	}
	
	@Override
	public boolean mustntDisplay () {
		return this.subConfigLoaded.size() == 0;
	}
	
	@Override
	public void saveValue() {
//		log.info("Save configuration "+this.getMod());
//		
//		LinkedHashMap<String, Object> values = new LinkedHashMap<String, Object>();
//		for (Entry<String, Object> entry : this.entryList.getValues().entrySet()) {
//			values.putAll((Map<? extends String, ? extends Object>) entry.getValue());
//		}
//		
//		this.configLoad.saveValue(values);
//		new ConfigLoader(configLoad.config, false).writeConfig();
	}
	
}
