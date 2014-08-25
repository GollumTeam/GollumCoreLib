package mods.gollum.core.common.facory;

import mods.gollum.core.common.mod.GollumMod;
import cpw.mods.fml.common.registry.EntityRegistry;

public class Mobactory {
	
	/**
	 * Enregistre un mob
	 * @param entityClass
	 * @param name
	 * @param id
	 * @param spawn
	 */
	public void register (GollumMod mod, Class entityClass, String name, int colorBG, int colorPoid) {
		
		EntityRegistry.registerGlobalEntityID(entityClass, name, EntityRegistry.findGlobalUniqueEntityId(), 0xFFFFFF, colorPoid);
		
		// 30 est traking range : trackingRange The range at which MC will send tracking updates
		// 1 est la frequence : The frequency of tracking updates
		// true est l'envoie de la msie a jour de la velocité : sendsVelocityUpdates Whether to send velocity information packets as well
		EntityRegistry.registerModEntity(entityClass, name,  mod.nextMobID (), mod, 30, 1, true);
		
	}

}
