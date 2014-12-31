package mods.gollum.core.client.gui.config.entry;

import mods.gollum.core.client.gui.config.GuiArrayConfig;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.GuiValueConfig;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class ArrayEntry extends ButtonEntry {
	
	Object[] values;
	
	public ArrayEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(mc, parent, configElement);
		this.values = (Object[])configElement.getValue();
		this.updateValueButtonText();
	}
	
	public void updateValueButtonText() {
		
		String text = "";
		for (Object o : this.values) {
			text += ", [" + o + "]";
		}
		text = text.replaceFirst(", ", "");
		
		this.updateValueButtonText(text);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiArrayConfig(this));
	}
	
	@Override
	public Object getValue() {
		return this.values;
	}
	
	@Override
	public ConfigEntry setValue(Object value) {
		try {
			this.values = (Object[])value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

}
