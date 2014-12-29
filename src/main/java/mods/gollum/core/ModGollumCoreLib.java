
package mods.gollum.core;

import java.lang.reflect.Method;
import java.util.List;

import mods.gollum.core.common.CommonProxyGolumCoreLib;
import mods.gollum.core.common.command.CommandBuilding;
import mods.gollum.core.common.config.ConfigGollumCoreLib;
import mods.gollum.core.common.context.ModContext;
import mods.gollum.core.common.creativetab.GollumCreativeTabs;
import mods.gollum.core.common.event.WorldHandler;
import mods.gollum.core.common.i18n.I18n;
import mods.gollum.core.common.log.Logger;
import mods.gollum.core.common.mod.GollumMod;
import mods.gollum.core.common.reflection.WorldStub;
import mods.gollum.core.common.tileentities.TileEntityBlockProximitySpawn;
import mods.gollum.core.common.version.VersionChecker;
import mods.gollum.core.common.worldgenerator.WorldGeneratorByBuilding;
import mods.gollum.core.common.worldgenerator.WorldGeneratorByBuildingLoader;
import mods.gollum.core.inits.ModBlocks;
import mods.gollum.core.inits.ModItems;
import mods.gollum.core.inits.ModTileEntities;
import mods.gollum.core.tools.registry.BlockRegistry;
import mods.gollum.core.tools.registry.ItemRegistry;
import mods.gollum.core.utils.reflection.DeobfuscateName;
import mods.gollum.core.utils.reflection.Reflection;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
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
	public final static String MINECRAFT_VERSION = "1.7.10";

	@Instance(ModGollumCoreLib.MODID)
	public static ModGollumCoreLib instance;
	
	@SidedProxy(clientSide = "mods.gollum.core.client.ClientProxyGolumCoreLib", serverSide = "mods.gollum.core.common.CommonProxyGolumCoreLib")
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
	
	/**
	 * Tab du mode creative
	 */
	public static GollumCreativeTabs tabBuildingStaff = new GollumCreativeTabs("BuildingStaff");;
	
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
		this.tabBuildingStaff.setIcon(ModItems.itemBuilding);
		
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
			
			Reflection.enableSynchronized (WorldServer.class.getDeclaredMethod(Reflection.getObfuscateName(WorldStub.class, "scheduleBlockUpdateWithPriority"), int.class, int.class, int.class, Block.class, int.class, int.class));
			Reflection.enableSynchronized (WorldServer.class.getDeclaredMethod(Reflection.getObfuscateName(WorldStub.class, "tickUpdates"                    ), boolean.class));
			Reflection.enableSynchronized (WorldServer.class.getDeclaredMethod(Reflection.getObfuscateName(WorldStub.class, "func_147446_b"                  ), int.class, int.class, int.class, Block.class, int.class, int.class));
			Reflection.enableSynchronized (WorldServer.class.getDeclaredMethod(Reflection.getObfuscateName(WorldStub.class, "getPendingBlockUpdates"         ), Chunk.class, boolean.class));
			Reflection.enableSynchronized (WorldServer.class.getDeclaredMethod(Reflection.getObfuscateName(WorldStub.class, "initialize"                     ), WorldSettings.class));
			
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
		GameRegistry.registerWorldGenerator (worldGeneratorByBuilding, 0);
	}
}
