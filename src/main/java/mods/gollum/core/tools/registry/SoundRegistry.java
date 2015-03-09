package mods.gollum.core.tools.registry;

import java.util.ArrayList;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.context.ModContext;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class SoundRegistry {
	
	private static ArrayList<String> sounds = new ArrayList<String>();
	
	public static void register (String sound) {
		sounds.add(ModContext.instance().getCurrent().getModId().toLowerCase()+":"+sound+".ogg");
	}
	
	@ForgeSubscribe
	public void onSound(SoundLoadEvent event) {
		
		if (this.sounds.isEmpty())ModGollumCoreLib.log.debug ("No sound registry");
		
		for (String sound : this.sounds) {
			try {
				event.manager.addSound(sound);
				ModGollumCoreLib.log.debug ("Load sound : "+sound);
			} catch (Exception e) {
				e.printStackTrace();
				ModGollumCoreLib.log.severe ("Failed to registry sound : "+sound);
			}
		}
	}
}
