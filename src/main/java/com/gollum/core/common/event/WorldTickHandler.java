package com.gollum.core.common.event;

import static com.gollum.core.ModGollumCoreLib.log;

import java.util.Iterator;

import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Builder.BuilderRunnable;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
	}
}
