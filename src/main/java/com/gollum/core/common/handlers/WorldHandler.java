package com.gollum.core.common.handlers;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.minecraft.entity.EntityTracker;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkDataEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;

import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Builder.BuilderRunnable;
import com.gollum.core.common.events.BuildingGenerateEvent;
import com.gollum.core.utils.reflection.Reflection;

public class WorldHandler {
	
	boolean mustBeSave = false;
	
	@ForgeSubscribe
	public void onSave (Save event) {
		
		if (!event.world.isRemote) {
			for (Thread thread : Builder.currentBuilds) {
				if (thread.isAlive()) {
					this.mustBeSave = true;
				}
			}
		}
	}
	
	@ForgeSubscribe
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
