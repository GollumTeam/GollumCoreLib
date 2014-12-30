package mods.gollum.core.client.gui.config.entry;

import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import net.minecraft.client.Minecraft;

public class IntegerEntry extends LongEntry {
	
	public IntegerEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(mc, parent, configElement);
	}
	
	public Object getValue() {
		
		Integer value = new Integer(0);
		try {
			value = Integer.parseInt(this.textFieldValue.getText());
		} catch (Exception e) {
		}
		
		return value;
	}

}
