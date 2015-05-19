package com.gollum.core.common.event;

import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import com.gollum.core.utils.reflection.collections.ConcurrentTreeMap;
import com.gollum.core.utils.reflection.collections.ConcurrentTreeSet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;


public class WorldHandler {
	
	boolean mustBeSave = false;
	
	@SubscribeEvent
	public void onLoad (Load event) {
		
		if (!event.world.isRemote) {
			
			WorldAccesssSheduler.instance().observe(event.world);
			
			WorldServer worldServer = (WorldServer) event.world;
			
			EntityTracker tracker = worldServer.getEntityTracker();
			if (!(tracker instanceof EntityTrackerProxy)) {
				for (Field f : WorldServer.class.getDeclaredFields()) {
					
					if (f.getType() == EntityTracker.class) {
						try {
							Reflection.setFinalField(f, worldServer, new EntityTrackerProxy(worldServer, tracker));
						} catch (Exception e) {
							e.printStackTrace();
						}
					} 
					else 
					if (f.getType() == TreeSet.class) {
						try {
							f.setAccessible(true);
							TreeSet o = (TreeSet)f.get(worldServer);
								
							Object dd = new ConcurrentTreeSet(o);
							
							f.set(worldServer, dd);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
//					else 
//					if (f.getType() == Set.class) {
//						try {
//							f.setAccessible(true);
//							Set o = (Set)f.get(worldServer);
//							Object dd = Collections.synchronizedCollection(o);
//							
//							f.set(worldServer, dd);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
					
					
				}
			}
			
			log.debug("Load");
			
		}
	}
	
	@SubscribeEvent
	public void onSave (Save event) {
		
		if (!event.world.isRemote) {
			for (Thread thread : Builder.currentBuilds) {
				if (thread.isAlive()) {
					mustBeSave = true;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onUnload (Unload event) {
		
		if (!event.world.isRemote) {
			
			WorldAccesssSheduler.instance().forget(event.world);
			WorldAccesssSheduler.instance().unlockWorld(event.world);
			
			for (BuilderRunnable thread : Builder.currentBuilds) {
				if (thread.isAlive()) {
					try {
						log.message("Wait finish building");
						thread.dontWaitWorld();
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				mustBeSave = true;
			}
			if (mustBeSave) {
				log.message("Resave after building...");
				try {
					((WorldServer) event.world).saveAllChunks(true, (IProgressUpdate)null);
					log.message("Resave after building : DONE");
				} catch (Exception e) {
				}
			}
			Builder.currentBuilds.clear ();
			WorldAccesssSheduler.instance().clean();

			mustBeSave = false;
			
			log.debug("=========== UnloadEvent ===========");
		}
	}
	
}
