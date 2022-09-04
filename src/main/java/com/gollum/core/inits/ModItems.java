package com.gollum.core.inits;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.items.ItemBuilding;
import com.gollum.core.common.items.ItemInfos;
import com.gollum.core.common.items.ItemWrench;

public class ModItems {

	public static ItemBuilding BUILDING;
	public static ItemWrench   WRENCH;
	public static ItemInfos    INFOS;
	
	public static void init() {
		if (ModGollumCoreLib.config.devTools) {
			ModItems.BUILDING = new ItemBuilding("building");
			ModItems.WRENCH   = new ItemWrench  ("wrench");
			ModItems.INFOS    = new ItemInfos   ("infos");
		}
	}
}
