package mods.gollum.core.inits;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.items.ItemBuilding;

public class ModItems {

	public static ItemBuilding itemBuilding;
	
	public static void init() {
		ModItems.itemBuilding = (ItemBuilding)new ItemBuilding("ItemBuilding").setCreativeTab(ModGollumCoreLib.tabBuildingStaff);
	}
}
