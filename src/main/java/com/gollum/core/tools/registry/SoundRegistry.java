package com.gollum.core.tools.registry;

import java.util.ArrayList;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SoundRegistry {
	
	private static ArrayList<String> sounds = new ArrayList<String>();
	
	public static void register (String sound) {
		sounds.add(ModContext.instance().getCurrent().getModId().toLowerCase()+":"+sound+".ogg");
	}
	
	@SubscribeEvent
	public void onSound(SoundLoadEvent event) {
		
		if (this.sounds.isEmpty())ModGollumCoreLib.log.debug ("No sound registry");
		
		for (String sound : this.sounds) {
			try {
				// TODO a revoir pas fini
//				event.manager.addSound(sound);
				ModGollumCoreLib.log.debug ("Load sound : "+sound);
			} catch (Exception e) {
				e.printStackTrace();
				ModGollumCoreLib.log.severe ("Failed to registry sound : "+sound);
			}
		}
	}
}
