
package com.gollum.core;

import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import com.gollum.core.common.CommonProxyGolumCoreLib;
import com.gollum.core.common.command.CommandBuilding;
import com.gollum.core.common.config.ConfigGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.event.WorldHandler;
import com.gollum.core.common.i18n.I18n;
import com.gollum.core.common.log.Logger;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.common.reflection.WorldStub;
import com.gollum.core.common.version.VersionChecker;
import com.gollum.core.common.worldgenerator.WorldGeneratorByBuilding;
import com.gollum.core.common.worldgenerator.WorldGeneratorByBuildingLoader;
import com.gollum.core.inits.ModBlocks;
import com.gollum.core.inits.ModCreativeTab;
import com.gollum.core.inits.ModItems;
import com.gollum.core.inits.ModTileEntities;
import com.gollum.core.tools.registry.BlockRegistry;
import com.gollum.core.tools.registry.ItemRegistry;
import com.gollum.core.utils.reflection.Reflection;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
	modid                     = ModGollumCoreLib.MODID,
	name                      = ModGollumCoreLib.MODNAME,
	version                   = ModGollumCoreLib.VERSION,
	acceptedMinecraftVersions = ModGollumCoreLib.MINECRAFT_VERSION
)
public class ModGollumCoreLib extends GollumMod {

	public final static String MODID = "GollumCoreLib";
	public final static String MODNAME = "Gollum Core Lib";
	public final static String VERSION = "2.0.0";
	public final static String MINECRAFT_VERSION = "1.6.4";

	@Instance(ModGollumCoreLib.MODID)
	public static ModGollumCoreLib instance;
	
	@SidedProxy(clientSide = "com.gollum.core.client.ClientProxyGolumCoreLib", serverSide = "com.gollum.core.common.CommonProxyGolumCoreLib")
	public static CommonProxyGolumCoreLib proxy;
	
	/**
	 * Gestion des logs
	 */
	public static Logger log;
	
	/**
	 * Gestion de l'i18n
	 */
	public static I18n i18n;
	
	/**
	 * La configuration
	 */
	public static ConfigGollumCoreLib config;
	
	@EventHandler public void handler(FMLInitializationEvent event)     { super.handler (event); }
	@EventHandler public void handler(FMLPostInitializationEvent event) { super.handler (event); }
	
	@EventHandler
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
		ModContext.instance ().setCurrent(this);
		
		// On charge la config avant le logger mais après 
		// le push du context
		// Le logger à besoin de la config
		// Tandis que le loader de config est indépendant
		
		// Charge la configuration
		this.config = (ConfigGollumCoreLib) new ConfigGollumCoreLib().loadConfig();
		
		// Creation du logger
		this.log = new Logger();
		
		// Creation du logger
		this.i18n = new I18n();
		
		// Affecte la config
		VersionChecker.setDisplay(this.config.versionChecker);
		
		// Gestion de la nivaeu de log
		Logger.setLevelDisplay(this.config.level);
		
		// Creation du checker de version
		new VersionChecker();

		// Initialisation des blocks
		ModBlocks.init ();
		
		// Initialisation des items
		ModItems.init ();

		// Initialisation des reflection sur vanilla
		this.initReflection();
		
		BlockRegistry.instance().registerAll();
		ItemRegistry.instance().registerAll();
		
	}
	
	/** 2 **/
	public void init(FMLInitializationEvent event) {
		
		// Enregistre les events
		this.proxy.registerEvents();
		
		// Initialisation les TileEntities
		ModTileEntities.init();
		
		// Set de l'icon du tab creative
		ModCreativeTab.init();
		
		MinecraftForge.EVENT_BUS.register(new WorldHandler());
	}

	/** 3 **/
	public void postInit(FMLPostInitializationEvent event) {
		// Initialisation des générateur de terrain
		this.initWorldGenerators();
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandBuilding());
	}
	
	/**
	 * Initialisation des reflection sur vanilla
	 */
	private void initReflection() {
		try {
			
			Reflection.enableSynchronized (Reflection.getObfuscateMethod(WorldServer.class, WorldStub.class, "scheduleBlockUpdateWithPriority"));
			Reflection.enableSynchronized (Reflection.getObfuscateMethod(WorldServer.class, WorldStub.class, "tick"                           ));
			Reflection.enableSynchronized (Reflection.getObfuscateMethod(WorldServer.class, WorldStub.class, "tickUpdates"                    ));
			Reflection.enableSynchronized (Reflection.getObfuscateMethod(WorldServer.class, WorldStub.class, "func_147446_b"                  ));
			Reflection.enableSynchronized (Reflection.getObfuscateMethod(WorldServer.class, WorldStub.class, "getPendingBlockUpdates"         ));
			Reflection.enableSynchronized (Reflection.getObfuscateMethod(WorldServer.class, WorldStub.class, "initialize"                     ));
			Reflection.enableSynchronized (Reflection.getObfuscateMethod(WorldServer.class, WorldStub.class, "addBlockEvent"                  ));
			Reflection.enableSynchronized (Reflection.getObfuscateMethod(WorldServer.class, WorldStub.class, "updateEntities"                 ));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Enregistre les générateur de terrain
	 */
	private void initWorldGenerators () {
		
		// Céation du world generator
		WorldGeneratorByBuilding worldGeneratorByBuilding = new WorldGeneratorByBuildingLoader().load ();
		
		// Enregistrement du worldgenerator mercenary
		GameRegistry.registerWorldGenerator (worldGeneratorByBuilding);
	}
}
