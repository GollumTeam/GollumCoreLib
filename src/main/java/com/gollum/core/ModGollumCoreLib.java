package com.gollum.core;

import net.minecraft.client.model.ModelWolf;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

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

@Mod(
	modid                     = ModGollumCoreLib.MODID,
	name                      = ModGollumCoreLib.MODNAME,
	version                   = ModGollumCoreLib.VERSION,
	acceptedMinecraftVersions = ModGollumCoreLib.MINECRAFT_VERSION
)
public class ModGollumCoreLib extends GollumMod {

	public final static String MODID = "gollumcorelib";
	public final static String MODNAME = "Gollum Core Lib";
	public final static String VERSION = "3.X.XDEV";
	public final static String MINECRAFT_VERSION = "1.12.2";

	@Instance(ModGollumCoreLib.MODID)
	public static ModGollumCoreLib instance;
	
	@SidedProxy(
			clientSide = "com.gollum.core.client.ClientProxyGolumCoreLib",
			serverSide = "com.gollum.core.common.CommonProxyGolumCoreLib"
	)
	public static CommonProxyGolumCoreLib proxy;
	
	/**
	 * Gestion des logs
	 */
	public static Logger logger;
	
	/**
	 * Gestion de l'i18n
	 */
	public static I18n i18n;
	
	/**
	 * La configuration
	 */
	public static ConfigGollumCoreLib config;

	@EventHandler @Override public void handler(FMLPreInitializationEvent event)  { super.handler (event); }
	@EventHandler @Override public void handler(FMLInitializationEvent event)     { super.handler (event); }
	@EventHandler @Override public void handler(FMLPostInitializationEvent event) { super.handler (event); }
	
//	@EventHandler
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
		ModContext.instance ().setCurrent(this);
		
		// Gestion de la nivaeu de log
		Logger.setLevelDisplay(config.level);
		
		// Affecte la config
		VersionChecker.setDisplay(config.versionChecker);
		
		// Creation du checker de version
		new VersionChecker();

		// Create tab creative
		ModCreativeTab.create();
		
		// Initialisation des blocks
		ModBlocks.init ();
		
		// Initialisation des items
		ModItems.init ();
		
	}
	
	/** 2 **/
	public void init(FMLInitializationEvent event) {
		
		// Initialisation les TileEntities
		ModTileEntities.init();
		
		// Set de l'icon du tab creative
		ModCreativeTab.init();
		
		// Enregistre les events
		proxy.registerEvents();
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
		GameRegistry.registerWorldGenerator (worldGeneratorByBuilding, 255);
	}
}
