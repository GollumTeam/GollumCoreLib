package mods.gollum.core.facory;

import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemFactory {

	public Item create(Item item, String name, String modId) {
		
		item.setUnlocalizedName(name);
		GameRegistry.registerItem (item, name, modId);
		
		return item;
	}

	public Item create(Item item, String name, String modId, String trans) {
		
		create (item, name, modId);
		LanguageRegistry.addName(item, trans);
		
		return item;
	}

}
