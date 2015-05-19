package com.gollum.core.common.event;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.entity.EntityTracker;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.ChunkDataEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;

import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Builder.BuilderRunnable;
import com.gollum.core.common.concurrent.WorldAccesssSheduler;
import com.gollum.core.common.reflection.EntityTrackerProxy;
import com.gollum.core.utils.reflection.Reflection;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;


public class WorldTickHandler {
	
	@SubscribeEvent
	public void onWorldTickEvent (WorldTickEvent event) {
		
		if (!event.world.isRemote) {
			
			Iterator<BuilderRunnable> i = Builder.currentBuilds.iterator();
			while (i.hasNext()) {
				BuilderRunnable thread = i.next(); 
				if (!thread.isAlive()) {
					i.remove();
					log.debug ("Thread "+thread.getId()+" is finish remove of pile.");
					continue;
				}
				
				try {
					log.debug ("WorldServer notify Builder");
					synchronized  (thread.waiter) {
						thread.waiter.notify();
					}
					Thread.sleep(50);
					log.debug ("WorldServer lock for builder");
					thread.lockWorld.lock();
					log.debug ("WorldServer is unlock");
					thread.unlockWorld();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				break;
			}
			
		}
		
//		
//		if (event.phase == Phase.START) {
//			WorldAccesssSheduler.instance().lockWorld(event.world);
//		} else {
//			WorldAccesssSheduler.instance().unlockWorld(event.world);
//		}
//		
	}
}
