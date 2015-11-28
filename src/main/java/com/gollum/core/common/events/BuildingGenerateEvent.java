package com.gollum.core.common.events;

import com.gollum.core.common.building.Building;
import com.gollum.core.utils.math.Integer3d;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class BuildingGenerateEvent extends Event {
	
	public World     world;
	public Building  building;
	public int       rotate;
	public Integer3d position;

	public BuildingGenerateEvent(World world, Building building, int rotate, Integer3d position) {
		this.world    = world;
		this.building = building;
		this.rotate   = rotate;
		this.position = position;
	}
	
	public static class Pre extends BuildingGenerateEvent {
		public Pre(World world, Building building, int rotate, Integer3d position) {
			super(world, building, rotate, position);
		}
	}
	
	public static class Post extends BuildingGenerateEvent {
		public Post(World world, Building building, int rotate, Integer3d position) {
			super(world, building, rotate, position);
		}
	}
	
}
