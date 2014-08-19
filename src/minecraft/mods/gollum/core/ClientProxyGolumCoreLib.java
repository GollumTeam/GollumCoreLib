package mods.gollum.core;

import mods.gollum.core.sound.SoundRegistry;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxyGolumCoreLib extends CommonProxyGolumCoreLib {

	public void registerEvents () {
		super.registerEvents ();
		MinecraftForge.EVENT_BUS.register(new SoundRegistry());
	}
	
}
