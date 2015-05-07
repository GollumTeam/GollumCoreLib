package com.gollum.core.client.gui.config;

import java.util.Map.Entry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.gollum.core.client.gui.config.element.ListElement;
import com.gollum.core.client.gui.config.entry.ConfigEntry;
import com.gollum.core.client.gui.config.entry.ListEntry;
import com.gollum.core.client.gui.config.entry.ListSlotEntry;
import com.gollum.core.tools.registered.RegisteredObjects;

public class GuiItemConfig extends GuiListConfig {

	public GuiItemConfig(ListEntry listEntry) {
		super(listEntry);
	}
	
	@Override
	protected void initConfigElement() {
		
		for (Entry<String, Item> entry : RegisteredObjects.instance().getItemsList().entrySet()) {
			this.configElements.add(new ListElement (entry.getKey(), entry.getValue().getItemStackDisplayName(new ItemStack(entry.getValue())) + " ("+entry.getKey()+")" ));
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
				Item it = RegisteredObjects.instance().getItem((String) entry.getValue());
				if (it != null) {
					((ListSlotEntry) entry).itemStackIcon = new ItemStack(it);
				}
			}
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
