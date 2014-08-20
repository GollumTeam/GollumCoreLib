package mods.gollum.core.facory;

import mods.gollum.core.mod.GollumMod;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemFactory {

	public Item create(Item item, String name, GollumMod mod) {
		
		item.setUnlocalizedName(name);
		GameRegistry.registerItem (item, name, mod.getModid());
		
		return item;
	}

}
