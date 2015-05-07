package com.gollum.core.inits;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.items.ItemBuilding;
import com.gollum.core.common.items.ItemInfos;
import com.gollum.core.common.items.ItemWrench;

public class ModItems {

	public static ItemBuilding itemBuilding;
	public static ItemWrench   itemWrench;
	public static ItemInfos    itemInfos;
	
	public static void init() {
		ModItems.itemBuilding = new ItemBuilding("Building");
		ModItems.itemWrench   = new ItemWrench  ("Wrench");
		ModItems.itemInfos    = new ItemInfos   ("Infos");
		
		if (ModGollumCoreLib.config.devTools) {
			ModItems.itemBuilding.setCreativeTab(ModCreativeTab.tabBuildingStaff);
			ModItems.itemWrench  .setCreativeTab(ModCreativeTab.tabDevTools);
			ModItems.itemInfos   .setCreativeTab(ModCreativeTab.tabDevTools);
		}
	}
}
