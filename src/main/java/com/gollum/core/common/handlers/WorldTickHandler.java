package com.gollum.core.common.handlers;

import static com.gollum.core.ModGollumCoreLib.log;

import java.util.Iterator;

import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Builder.BuilderRunnable;
import com.gollum.core.common.events.BuildingGenerateEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;


public class WorldTickHandler {
	
	private long time = 0;
	
	@SubscribeEvent
	public void onWorldTickEvent (WorldTickEvent event) {
		
		if (!event.world.isRemote) {
			
			if (System.currentTimeMillis() - this.time > 200) {
				this.time = System.currentTimeMillis();
					
				Iterator<BuilderRunnable> i = Builder.currentBuilds.iterator();
				while (i.hasNext()) {
					BuilderRunnable thread = i.next(); 
					if (thread.getWorld() == event.world && !thread.isAlive()) {
						i.remove();
						
						BuildingGenerateEvent newEvent = new BuildingGenerateEvent.Post(event.world, thread.getBuilding(), thread.getRotate(), thread.getInitPos());
						MinecraftForge.EVENT_BUS.post(newEvent);
						
						log.debug ("Thread "+thread.getId()+" is finish remove of pile.");
						continue;
					}
					
					try {
	//					log.debug ("WorldServer notify Builder");
						synchronized  (thread.waiter) {
							thread.waiter.notify();
						}
						Thread.sleep(10);
	//					log.debug ("WorldServer lock for builder");
						thread.lockWorld.lock();
	//					log.debug ("WorldServer is unlock");
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
}
