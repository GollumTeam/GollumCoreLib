package mods.gollum.core.client;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.client.keys.GollumKeybinds;
import mods.gollum.core.common.CommonProxyGolumCoreLib;
import mods.gollum.core.tools.registry.SoundRegistry;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.KeyBindingRegistry;

public class ClientProxyGolumCoreLib extends CommonProxyGolumCoreLib {

	public void registerEvents () {
		
		super.registerEvents ();
		
		MinecraftForge.EVENT_BUS.register(new SoundRegistry());
		
		if (ModGollumCoreLib.config.devTools) KeyBindingRegistry.registerKeyBinding(new GollumKeybinds());
	}
	
}
