package mods.gollum.core.client.gui.config.entry;

import java.lang.reflect.Array;

import scala.actors.threadpool.Arrays;
import mods.gollum.core.client.gui.config.GuiArrayConfig;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.GuiValueConfig;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class ArrayEntry extends ButtonEntry {
	
	Object values;
	
	public ArrayEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(mc, parent, configElement);
		
		Object values = configElement.getValue();
		
		this.values =  Array.newInstance(values.getClass().getComponentType(), Array.getLength(values));
		for (int i = 0; i < Array.getLength(values);i++) {
			Array.set(this.values, i, Array.get(values, i));
		}
		this.updateValueButtonText();
	}
	
	public void updateValueButtonText() {
		
		String text = "";
		for (int i = 0; i < Array.getLength(this.values); i++) {
			text += ", [" + Array.get(this.values, i).toString() + "]";
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
			this.values = value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.updateValueButtonText();
		return this;
	}

}
