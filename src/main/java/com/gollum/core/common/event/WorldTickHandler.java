package com.gollum.core.common.event;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;

import net.minecraft.entity.EntityTracker;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ChunkDataEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;

import com.gollum.core.common.building.Builder;
import com.gollum.core.common.reflection.EntityTrackerProxy;
import com.gollum.core.utils.reflection.Reflection;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;


public class WorldTickHandler {
	
	@SubscribeEvent
	public void onWorldTickEvent (WorldTickEvent event) {
		
		if (event.phase == Phase.START) {
			Builder.lockWorld();
		} else {
			Builder.unlockWorld();
		}
		
	}
}
