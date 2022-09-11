package com.gollum.core.inits;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.creativetab.GollumCreativeTabs;

public class ModCreativeTab {
	
	public static GollumCreativeTabs BUILDING_STAFF;
	public static GollumCreativeTabs DEV_TOOLS;
	
	public static void create() {
		if (ModGollumCoreLib.config.devTools) {
			ModCreativeTab.BUILDING_STAFF = new GollumCreativeTabs("BuildingStaff");
			ModCreativeTab.DEV_TOOLS      = new GollumCreativeTabs("GollumDevTools");
		}
	}
	public static void init() {
		if (ModGollumCoreLib.config.devTools) {
			ModCreativeTab.BUILDING_STAFF.setIcon(ModItems.BUILDING);
			ModCreativeTab.DEV_TOOLS     .setIcon(ModItems.WRENCH);
		}
	}
}
