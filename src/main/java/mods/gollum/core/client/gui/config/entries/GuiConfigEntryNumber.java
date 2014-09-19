package mods.gollum.core.client.gui.config.entries;

import mods.gollum.core.client.gui.config.GuiConfigEntries;
import net.minecraft.client.Minecraft;

public abstract class GuiConfigEntryNumber extends GuiConfigEntryString {

	public GuiConfigEntryNumber(GuiConfigEntries parent, Minecraft mc, String label, String value, String defaultValue, Object[] params) {
		super(parent, mc, label, value, defaultValue, "([-]?\\d*)", params);
	}

}
