package mods.gollum.core.tools.registry;

import java.util.ArrayList;

import mods.gollum.core.tools.helper.IItemHelper;


public class ItemRegistry {

	private static ItemRegistry instance = new ItemRegistry();
	
	private ArrayList<IItemHelper> items = new ArrayList<IItemHelper>();
	
	public static ItemRegistry instance () {
		return instance;
	}
	
	public void add (IItemHelper item) {
		if (!this.items.contains(item)) {
			this.items.add(item);
		}
	}
	
	public void registerAll () {
		for (IItemHelper item : this.items) {
			item.register();
		}
		this.items.clear();
	}
	
}
