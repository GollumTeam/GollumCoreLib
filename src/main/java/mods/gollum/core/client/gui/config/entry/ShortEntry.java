package mods.gollum.core.client.gui.config.entry;

import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import net.minecraft.client.Minecraft;

public class ShortEntry extends IntegerEntry {
	
	public ShortEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(mc, parent, configElement);
	}
	
	public Object getValue() {
		
		Short value = new Short((short) 0);
		try {
			value = Short.parseShort(this.textFieldValue.getText());
		} catch (Exception e) {
		}
		
		return value;
	}

}
