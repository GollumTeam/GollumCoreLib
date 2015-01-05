package mods.gollum.core.client.gui.config.entry;

import mods.gollum.core.client.gui.config.GuiBlockConfig;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import net.minecraft.client.Minecraft;

public class BlockEntry extends ListEntry {

	public BlockEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
	}
	
	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiBlockConfig(this));
	}
}
