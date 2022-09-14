package com.gollum.core.common.handlers;

import static com.gollum.core.ModGollumCoreLib.logger;

import com.gollum.core.common.building.Builder;
import com.gollum.core.common.building.Builder.BuilderRunnable;
import com.gollum.core.common.events.BuildingGenerateEvent;

import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent.Save;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class WorldHandler {
	
	boolean mustBeSave = false;
	
	@SubscribeEvent
	public void onSave (Save event) {
		
		if (!event.getWorld().isRemote) {
			for (Thread thread : Builder.currentBuilds) {
				if (thread.isAlive()) {
					this.mustBeSave = true;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onUnload (Unload event) {
		
		if (!event.getWorld().isRemote) {
			
			for (BuilderRunnable thread : Builder.currentBuilds) {
				if (thread.isAlive()) {
					logger.message("Wait finish building");
					
					while (thread.isAlive()) {
						thread.pauseMainThread();
					}
				}
				BuildingGenerateEvent newEvent = new BuildingGenerateEvent.Post(event.getWorld(), thread.getBuilding(), thread.getRotate(), thread.getInitPos());
				MinecraftForge.EVENT_BUS.post(newEvent);
				this.mustBeSave = true;
			}
			if (this.mustBeSave) {
				logger.message("Resave after building...");
				try {
					((WorldServer) event.getWorld()).saveAllChunks(true, (IProgressUpdate)null);
					logger.message("Resave after building : DONE");
				} catch (Exception e) {
				}
			}
			Builder.currentBuilds.clear ();

			this.mustBeSave = false;
			
			logger.debug("=========== UnloadEvent ===========");
		}
	}
	
}
