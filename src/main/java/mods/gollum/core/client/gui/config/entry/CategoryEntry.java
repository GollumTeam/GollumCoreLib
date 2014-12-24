package mods.gollum.core.client.gui.config.entry;

import static mods.gollum.core.ModGollumCoreLib.log;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.GuiFieldConfig;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import net.minecraft.client.Minecraft;

public class CategoryEntry extends ButtonEntry {
	
	private Object value;
	
	public CategoryEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(mc, parent, configElement);
		
		this.labelDisplay = false;
		this.updateValueButtonText(this.getLabel());
		this.value = this.configElement.getValue();;
	}

	@Override
	public void valueButtonPressed(int slotIndex) {
		this.mc.displayGuiScreen(new GuiFieldConfig(this));
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public ConfigEntry setValue(Object value) {
		this.value = value;
		log.debug("set value ", value);
		return this;
	}
	
}
