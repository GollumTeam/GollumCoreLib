package mods.gollum.core.client.gui.config.entry;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.HashMap;
import java.util.Map.Entry;

import cpw.mods.fml.client.config.GuiMessageDialog;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.GuiValueConfig;
import mods.gollum.core.client.gui.config.element.CategoryElement;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.common.config.ConfigProp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;

public class CategoryEntry extends ButtonEntry {
	
	private Object value;
	
	public CategoryEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(mc, parent, configElement);
		
		this.labelDisplay = false;
		this.updateValueButtonText(this.getLabel());
		this.value = this.configElement.getValue();
	}

	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiValueConfig(this));
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public ConfigEntry setValue(Object value) {
		this.value = value;
		return this;
	}
	
	@Override
	public boolean requiresMcRestart() {
		
		if (!this.isChanged()) {
			return false;
		}
		
		for (Entry<String, ConfigProp> entry : ((CategoryElement)this.configElement).getProps().entrySet()) {
			if (entry.getValue().mcRestart()) {
				return true;
			}
		}
		return false;
		
	}
	
	@Override
	public boolean equals (Object values) {
		
		if (value instanceof HashMap) {
			boolean equal = true;
			
			for (Object key : ((HashMap)values).keySet()) {
				if (((HashMap) this.value).containsKey(key)) {
					
					Object value    = ((HashMap)values).get(key);
					Object oldValue = ((HashMap) this.value).get(key);
					
					if (
						(oldValue == null && oldValue != value ) ||
						(oldValue != null && ! oldValue.equals(value))
					) {
						equal = false;
						break;
					}
					
				} else {
					equal = false;
					break;
				}
			}
			
			return equal;
		}
		
		return super.equals(values);
	}
	
	
}
