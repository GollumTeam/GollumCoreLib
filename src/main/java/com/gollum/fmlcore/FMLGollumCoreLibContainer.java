package com.gollum.fmlcore;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.Arrays;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.GameRules;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Guilherme Chaguri
 */
public class FMLGollumCoreLibContainer extends DummyModContainer {

	public FMLGollumCoreLibContainer() {
		super(createMetadata());
	}
	
	private static ModMetadata createMetadata() {
		ModMetadata meta = new ModMetadata();
		meta.modId = FMLGollumCoreLib.MODID;
		meta.name = FMLGollumCoreLib.MODNAME;
		meta.version = FMLGollumCoreLib.VERSION;
		meta.authorList = Arrays.asList("SmeagolWorms4");
		meta.description = "It's a core mod for Gollum Core Lib";
		meta.url = "http://www.smeagol-mods.com";
		return meta;
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {
	}

	@Subscribe
	public void init(FMLInitializationEvent event) {
	}
	
	@Subscribe
	public void start(FMLServerStartingEvent event) {
	}
	
}
