package com.gollum.core.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.client.keys.BlockInfosKeyHandler;
import com.gollum.core.common.CommonProxyGolumCoreLib;
import com.gollum.core.tools.registry.SoundRegistry;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

public class ClientProxyGolumCoreLib extends CommonProxyGolumCoreLib {

	public void registerEvents () {
		
		super.registerEvents ();
		
		MinecraftForge.EVENT_BUS.register(new SoundRegistry());
		
		if (ModGollumCoreLib.config.devTools) {
			
			KeyBinding keyBinding = new KeyBinding (ModGollumCoreLib.MODID.toLowerCase()+".key.blockinfos", Keyboard.KEY_F4, "key.categories.misc");
			ClientRegistry.registerKeyBinding(keyBinding);
			FMLCommonHandler.instance().bus().register(new BlockInfosKeyHandler(keyBinding));
		}
	}

	public boolean isRemote() {
		return true;
	}
	
}
