package mods.gollum.core.common.event;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.building.Builder;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.world.WorldEvent.Unload;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;


public class WorldHandler {
	
	@SubscribeEvent
	public void onUnload (Unload event) {
		
		if (!event.world.isRemote) {
			
			boolean mustBeSave = false;
			
			for (Thread thread : Builder.currentBuilds) {
				if (thread.isAlive()) {
					try {
						ModGollumCoreLib.log.message("Wait finish building");
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				mustBeSave = true;
			}
			if (mustBeSave) {
				ModGollumCoreLib.log.message("Resave after building...");
				try {
					((WorldServer) event.world).saveAllChunks(true, (IProgressUpdate)null);
					ModGollumCoreLib.log.message("Resave after building : DONE");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Builder.currentBuilds.clear ();
			ModGollumCoreLib.log.debug("=========== UnloadEvent ===========");
		}
	}
	
}
