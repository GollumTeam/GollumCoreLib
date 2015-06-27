package com.gollum.core.client.gui.config;

import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.gollum.core.client.gui.config.element.ListElement;
import com.gollum.core.client.gui.config.entry.ConfigEntry;
import com.gollum.core.client.gui.config.entry.ListEntry;
import com.gollum.core.client.gui.config.entry.ListSlotEntry;
import com.gollum.core.tools.registered.RegisteredObjects;

public class GuiBlockConfig extends GuiListConfig {

	public GuiBlockConfig(ListEntry listEntry) {
		super(listEntry);
	}
	
	@Override
	protected void initConfigElement() {
		
		for (Entry<String, Block> entry : RegisteredObjects.instance().getBlocksList().entrySet()) {
			this.configElements.add(new ListElement(entry.getKey(), entry.getValue().getLocalizedName() + " ("+entry.getKey()+")"));
		}
		this.filter();
 	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		for (int i = 0; i < this.entryList.getSize(); i++) {
			ConfigEntry entry = this.entryList.getEntry(i);
			if (entry instanceof ListSlotEntry) {
				Block b = RegisteredObjects.instance().getBlock((String) entry.getValue());
				if (b != null) {
					((ListSlotEntry) entry).itemStackIcon = new ItemStack(b);
				}
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
