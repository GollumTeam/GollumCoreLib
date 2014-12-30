package mods.gollum.core.client.gui.config.entry;

import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import net.minecraft.client.Minecraft;

public class ByteEntry extends ShortEntry {
	
	public ByteEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(mc, parent, configElement);
	}
	
	public Object getValue() {
		
		Byte value = new Byte((byte)0);
		try {
			value = Byte.parseByte(this.textFieldValue.getText());
		} catch (Exception e) {
		}
		
		return value;
	}

}
