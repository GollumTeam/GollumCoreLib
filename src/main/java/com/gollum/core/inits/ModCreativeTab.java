package com.gollum.core.inits;

import com.gollum.core.common.blocks.BlockProximitySpawn;
import com.gollum.core.common.creativetab.StaffCreativeTabs;

public class ModCreativeTab {
	
	public static StaffCreativeTabs tabBuildingStaff = new StaffCreativeTabs("BuildingStaff");
	public static StaffCreativeTabs tabDevTools      = new StaffCreativeTabs("GollumDevTools");
	
	public static void init() {
		ModCreativeTab.tabBuildingStaff.setIcon(ModItems.itemBuilding);
		ModCreativeTab.tabDevTools     .setIcon(ModItems.itemWrench);
	}
}
