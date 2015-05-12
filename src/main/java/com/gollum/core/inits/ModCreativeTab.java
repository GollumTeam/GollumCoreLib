package com.gollum.core.inits;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.creativetab.GollumCreativeTabs;

public class ModCreativeTab {
	
	public static GollumCreativeTabs tabBuildingStaff = new GollumCreativeTabs("BuildingStaff");
	public static GollumCreativeTabs tabDevTools      = new GollumCreativeTabs("GollumDevTools");
	
	public static void init() {
		if (ModGollumCoreLib.config.devTools) {
			ModCreativeTab.tabBuildingStaff.setIcon(ModItems.itemBuilding);
			ModCreativeTab.tabDevTools     .setIcon(ModItems.itemWrench);
		}
	}
}
