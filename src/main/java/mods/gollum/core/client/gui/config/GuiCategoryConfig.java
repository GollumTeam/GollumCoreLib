package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import mods.gollum.core.client.gui.config.element.CategoryElement;
import mods.gollum.core.client.gui.config.entry.CategoryEntry;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.config.GuiMessageDialog;

public class GuiCategoryConfig extends GuiConfig {
	
	private ConfigLoad configLoad;
	
	public GuiCategoryConfig(GuiScreen parent) {
		super(parent);
		this.titleLine2 = "Configuration";
	}
	
	@Override
	protected void initConfigElement() {
		
		this.configLoad = ConfigLoader.configLoaded.get(this.mod);
		
		ArrayList<String> categories = this.configLoad.getCategories();
		
		for (String category : categories) {
			configElements.add(new CategoryElement (category, this.configLoad));
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		if (this.mustntDisplay()) {
			this.mc.displayGuiScreen(new GuiValueConfig((CategoryEntry)this.entryList.getEntry(0)));
			return;
		}
	}
	
	@Override
	public boolean mustntDisplay () {
		return this.configLoad.getCategories().size() == 1;
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
	public void saveValue() {
		log.info("Save configuration "+this.getMod().getModId());
		
		LinkedHashMap<String, Object> values = new LinkedHashMap<String, Object>();
		for (Entry<String, Object> entry : this.entryList.getValues().entrySet()) {
			values.putAll((Map<? extends String, ? extends Object>) entry.getValue());
		}
		
		this.configLoad.saveValue(values);
		new ConfigLoader(configLoad.config, false).writeConfig();
		
	}
	
}
