package mods.gollum.core.client.gui.config.entries;

import mods.gollum.core.client.gui.config.GuiEditCustomArray;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiEditArray;
import cpw.mods.fml.client.config.IConfigElement;

public class GuiEditArrayCustomEntries extends GuiConfigEntries {
	
	GuiEditCustomArray parent;
	Minecraft mc;
	IConfigElement configElement;
	Object[] beforeValues;
	Object[] currentValues;
	
	public GuiEditArrayCustomEntries(GuiEditCustomArray parent, Minecraft mc, IConfigElement configElement, Object[] beforeValues, Object[] currentValues) {
		super(parent.parent, mc);
		
		this.configElement = configElement;
		this.beforeValues  = beforeValues;
		this.currentValues = currentValues;
		this.mc            = mc;
		this.parent        = parent;
	}

}
