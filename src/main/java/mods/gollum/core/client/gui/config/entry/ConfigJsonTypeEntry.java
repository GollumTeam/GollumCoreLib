package mods.gollum.core.client.gui.config.entry;

import java.awt.Color;

import mods.gollum.core.client.gui.config.GuiEditCustomArray;
import mods.gollum.core.client.gui.config.GuiJsonConfig;
import mods.gollum.core.client.gui.config.entry.logic.EditCustomArrayEntryLogic;
import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.client.config.GuiConfigEntries.ButtonEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class ConfigJsonTypeEntry extends JsonEntry {
	
	protected IConfigJsonType defaultConfigType;
	protected IConfigJsonType beforeConfigType;
	
	public ConfigJsonTypeEntry(GuiConfig parent, GuiConfigEntries entryList, IConfigElement<String> configElement) {
		super(parent, entryList, configElement);
	}

	
	public void init() {
		this.defaultConfigType = (IConfigJsonType)this.configElement.getDefault();
		this.beforeConfigType  = (IConfigJsonType)configElement.get();
		
		this.defaultValue = this.defaultConfigType.writeConfig();
		this.beforeValue  = this.beforeConfigType .writeConfig();
		this.currentValue = this.beforeConfigType .writeConfig();
	}
	

	@Override
	public void updateValueButtonText() {
		try {
			IConfigJsonType newValue = (IConfigJsonType)this.beforeConfigType.getClass().newInstance();
			newValue.readConfig(this.currentValue);
			
			this.btnValue.displayString = newValue.toString(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean saveConfigElement() {
		if (enabled() && isChanged()) {
			try {
				
				IConfigJsonType newValue = (IConfigJsonType)this.beforeConfigType.getClass().newInstance();
				newValue.readConfig(this.currentValue);
				
				this.configElement.set(newValue);
				return configElement.requiresMcRestart();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}