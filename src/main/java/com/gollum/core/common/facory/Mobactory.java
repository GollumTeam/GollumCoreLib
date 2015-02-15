package com.gollum.core.common.facory;

import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.mod.GollumMod;

import cpw.mods.fml.common.registry.EntityRegistry;

public class Mobactory {
	
	/**
	 * Enregistre un mob
	 */
	public void register (Class entityClass, String name, int colorBG, int colorPoid) {
		this.register(entityClass, name, colorBG, colorPoid, 1);
	}
	
	/**
	 * Enregistre un mob
	 */
	public void register (Class entityClass, String name, int colorBG, int colorPoid, int frequence) {
		this.register(entityClass, name, colorBG, colorPoid, frequence, 30);
	}

	
	/**
	 * Enregistre un mob
	 */
	public void register (Class entityClass, String name, int colorBG, int colorPoid, int frequence, int traking) {
		
		GollumMod mod = ModContext.instance().getCurrent();
		
		EntityRegistry.registerGlobalEntityID(entityClass, name, EntityRegistry.findGlobalUniqueEntityId(), colorBG, colorPoid);
		
		// 30 est traking range : trackingRange The range at which MC will send tracking updates
		// 1 est la frequence : The frequency of tracking updates
		// true est l'envoie de la mise a jour de la velocité : sendsVelocityUpdates Whether to send velocity information packets as well
		EntityRegistry.registerModEntity(entityClass, name,  mod.nextMobID (), mod, traking, frequence, true);
		
	}

}
