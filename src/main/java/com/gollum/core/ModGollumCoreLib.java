
package com.gollum.core;

import com.gollum.core.common.CommonProxyGolumCoreLib;
import com.gollum.core.common.command.CommandBuilding;
import com.gollum.core.common.config.ConfigGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.i18n.I18n;
import com.gollum.core.common.log.Logger;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.common.version.VersionChecker;
import com.gollum.core.common.worldgenerator.WorldGeneratorByBuilding;
import com.gollum.core.common.worldgenerator.WorldGeneratorByBuildingLoader;
import com.gollum.core.inits.ModBlocks;
import com.gollum.core.inits.ModCreativeTab;
import com.gollum.core.inits.ModItems;
import com.gollum.core.inits.ModTileEntities;
import com.gollum.core.tools.registry.BlockRegistry;
import com.gollum.core.tools.registry.ItemRegistry;

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
	public final static String VERSION = "2.1.0DEV";
	public final static String MINECRAFT_VERSION = "1.7.10";

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

		// Create tab creative
		ModCreativeTab.create();
		
		// Set gollum gui config
		initGuiConfig();
		
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
		
		BlockRegistry.instance().registerAll();
		ItemRegistry.instance().registerAll();
		
	}
	
	/** 2 **/
	public void init(FMLInitializationEvent event) {
		
		// Initialisation les TileEntities
		ModTileEntities.init();
		
		// Set de l'icon du tab creative
		ModCreativeTab.init();
		
		// Enregistre les events
		this.proxy.registerEvents();
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
	 * Enregistre les générateur de terrain
	 */
	private void initWorldGenerators () {
		
		// Céation du world generator
		WorldGeneratorByBuilding worldGeneratorByBuilding = new WorldGeneratorByBuildingLoader().load ();
		
		// Enregistrement du worldgenerator mercenary
		GameRegistry.registerWorldGenerator (worldGeneratorByBuilding, 0);
	}
}
