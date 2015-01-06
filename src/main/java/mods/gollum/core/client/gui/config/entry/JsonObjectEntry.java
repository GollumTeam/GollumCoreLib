package mods.gollum.core.client.gui.config.entry;

import java.lang.reflect.Array;
import java.util.Set;
import java.util.Map.Entry;

import mods.gollum.core.client.gui.config.GuiArrayConfig;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.GuiJsonObjectConfig;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.tools.simplejson.Json;
import mods.gollum.core.tools.simplejson.JsonObject;
import net.minecraft.client.Minecraft;

public class JsonObjectEntry extends ButtonEntry {
	
	private JsonObject value;

	public JsonObjectEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		this.value = (JsonObject)configElement.getValue();
		this.updateValueButtonText();
	}

	public void updateValueButtonText() {
		this.updateValueButtonText(this.getValue().toString());
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiJsonObjectConfig(this));
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public ConfigEntry setValue(Object value) {
		try {
			this.value = (JsonObject)value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateValueButtonText();
		return this;
	}

}
