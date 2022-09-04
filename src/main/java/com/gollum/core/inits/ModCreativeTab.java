package com.gollum.core.inits;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.creativetab.GollumCreativeTabs;

public class ModCreativeTab {
	
	public static GollumCreativeTabs tabBuildingStaff;
	public static GollumCreativeTabs tabDevTools     ;
	
	public static void create() {
		if (ModGollumCoreLib.config.devTools) {
			ModCreativeTab.tabBuildingStaff = new GollumCreativeTabs("BuildingStaff");
			ModCreativeTab.tabDevTools      = new GollumCreativeTabs("GollumDevTools");
		}
	}
	public static void init() {
		if (ModGollumCoreLib.config.devTools) {
			ModCreativeTab.tabBuildingStaff.setIcon(ModItems.BUILDING);
			ModCreativeTab.tabDevTools     .setIcon(ModItems.WRENCH);
		}
	}
}
