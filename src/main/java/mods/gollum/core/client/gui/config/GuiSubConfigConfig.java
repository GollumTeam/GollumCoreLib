package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.client.gui.config.element.SubConfigElement;
import mods.gollum.core.client.gui.config.element.SubGuiElement;
import mods.gollum.core.client.gui.config.element.CategoryElement;
import mods.gollum.core.client.gui.config.entry.SubConfigEntry;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.config.GuiMessageDialog;

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
			configElements.add(new CategoryElement(this.subConfigEntry.getLabel(), configLoad, configLoad.config.getFileName()));
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
	public void saveValue() {
		log.info("Save configuration "+this.getMod().getModId()+" > "+this.subConfigEntry.getLabel());
		
		for (Entry<String, Object> entry : this.entryList.getValues().entrySet()) {
			String fileName = entry.getKey();
			for (ConfigLoad configLoad : this.subConfigLoaded) {
				
				if (fileName.equals(configLoad.config.getFileName())) {
					log.info("Save file: "+fileName);
					configLoad.saveValue((LinkedHashMap<String, Object>) entry.getValue());
					new ConfigLoader(configLoad.config, false).writeConfig();
				}
			}
		}
		
	}
	
}