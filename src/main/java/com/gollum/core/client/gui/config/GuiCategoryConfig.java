package com.gollum.core.client.gui.config;

import static com.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.gollum.core.client.gui.config.element.CategoryElement;
import com.gollum.core.client.gui.config.entry.CategoryEntry;
import com.gollum.core.common.config.ConfigLoader;
import com.gollum.core.common.config.ConfigLoader.ConfigLoad;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;

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
		
		boolean mcRestart = this.entryList.requiresMcRestart();
		boolean wRestart  = this.entryList.requiresWorldRestart();
		
		ConfigChangedEvent event = new OnConfigChangedEvent(this.getMod().getModId(), "", wRestart, mcRestart);
		FMLCommonHandler.instance().bus().post(event);
		if (!event.getResult().equals(Result.DENY)) {
			this.saveValue ();
			FMLCommonHandler.instance().bus().post(new PostConfigChangedEvent(this.getMod().getModId(), "", wRestart, mcRestart));
			
			if (mcRestart) {
				this.mc.displayGuiScreen(new GuiMessageDialog(this.getParent(), "fml.configgui.gameRestartTitle", new ChatComponentText(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
				return;
			}
		}
		super.displayParent();
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
