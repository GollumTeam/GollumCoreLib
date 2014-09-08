package mods.gollum.core.tools.registry;

import java.util.ArrayList;

import mods.gollum.core.common.building.handler.BuildingBlockHandler;
import mods.gollum.core.tools.helper.IBlockHelper;

public class BuildingBlockRegistry {
	
	private static BuildingBlockRegistry instance = new BuildingBlockRegistry();
	
	private ArrayList<BuildingBlockHandler> handlers = new ArrayList<BuildingBlockHandler>();
	
	public static BuildingBlockRegistry instance () {
		return instance;
	}
	
	public static void register (BuildingBlockHandler handler) {
		if (!instance.handlers.contains(handler)) {
			instance.handlers.add(handler);
		}
	}
	
	public ArrayList<BuildingBlockHandler> getHandlers () {
		return this.handlers;
	}
	
}
