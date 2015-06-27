package com.gollum.core.common.handlers;

import static com.gollum.core.ModGollumCoreLib.log;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;

import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Builder.BuilderRunnable;
import com.gollum.core.common.events.BuildingGenerateEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;


public class WorldHandler {
	
	boolean mustBeSave = false;
	
	@SubscribeEvent
	public void onSave (Save event) {
		
		if (!event.world.isRemote) {
			for (Thread thread : Builder.currentBuilds) {
				if (thread.isAlive()) {
					this.mustBeSave = true;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onUnload (Unload event) {
		
		if (!event.world.isRemote) {
			
			for (BuilderRunnable thread : Builder.currentBuilds) {
				if (thread.isAlive()) {
					try {
						log.message("Wait finish building");
						
						while (thread.isAlive()) {
							thread.dontWaitWorld();
							synchronized (thread.waiter) {
								thread.waiter.notify();
							}
							Thread.sleep(500);
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				BuildingGenerateEvent newEvent = new BuildingGenerateEvent.Post(event.world, thread.getBuilding(), thread.getRotate(), thread.getPosition());
				MinecraftForge.EVENT_BUS.post(newEvent);
				this.mustBeSave = true;
			}
			if (this.mustBeSave) {
				log.message("Resave after building...");
				try {
					((WorldServer) event.world).saveAllChunks(true, (IProgressUpdate)null);
					log.message("Resave after building : DONE");
				} catch (Exception e) {
				}
			}
			Builder.currentBuilds.clear ();

			this.mustBeSave = false;
			
			log.debug("=========== UnloadEvent ===========");
		}
	}
	
}
