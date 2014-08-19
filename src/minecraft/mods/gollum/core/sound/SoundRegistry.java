package mods.gollum.core.sound;

import java.util.ArrayList;

import mods.gollum.core.ModGollumCoreLib;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class SoundRegistry {
	
	private static ArrayList<String> sounds = new ArrayList<String>();
	
	public static void register (String sound, String modid) {
		sounds.add(modid.toLowerCase()+":"+sound+".ogg");
	}
	
	@ForgeSubscribe
	public void onSound(SoundLoadEvent event) {
			for (String sound : this.sounds) {
				try {
					event.manager.addSound(sound);
					ModGollumCoreLib.log.debug ("Load sound : "+sound);
				} catch (Exception e) {
					ModGollumCoreLib.log.severe ("Failed to registry sound : "+sound);
				}
			}
	}
}
