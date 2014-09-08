
package mods.gollum.core;

import mods.gollum.core.common.CommonProxyGolumCoreLib;
import mods.gollum.core.common.blocks.BlockProximitySpawn;
import mods.gollum.core.common.command.CommandBuilding;
import mods.gollum.core.common.config.ConfigGollumCoreLib;
import mods.gollum.core.common.context.ModContext;
import mods.gollum.core.common.creativetab.GollumCreativeTabs;
import mods.gollum.core.common.event.WorldHandler;
import mods.gollum.core.common.i18n.I18n;
import mods.gollum.core.common.items.ItemBuilding;
import mods.gollum.core.common.log.Logger;
import mods.gollum.core.common.mod.GollumMod;
import mods.gollum.core.common.tileentities.TileEntityBlockSpawner;
import mods.gollum.core.common.version.VersionChecker;
import mods.gollum.core.common.worldgenerator.WorldGeneratorByBuilding;
import mods.gollum.core.common.worldgenerator.WorldGeneratorByBuildingLoader;
import mods.gollum.core.tools.registry.BlockRegistry;
import mods.gollum.core.tools.registry.ItemRegistry;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ModGollumCoreLib.MODID, name = ModGollumCoreLib.MODNAME, version = ModGollumCoreLib.VERSION, acceptedMinecraftVersions = ModGollumCoreLib.MINECRAFT_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModGollumCoreLib extends GollumMod {

	public final static String MODID = "GollumCoreLib";
	public final static String MODNAME = "Gollum Core Lib";
	public final static String VERSION = "2.0.0";
	public final static String MINECRAFT_VERSION = "1.6.4";

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
	
	public static BlockProximitySpawn blockProximitySpawn;
	public static ItemBuilding itemBuilding;
	
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
		this.config = new ConfigGollumCoreLib().loadConfig();
		
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
		this.initBlocks ();
		
		// Initialisation des items
		this.initItem ();
		
		BlockRegistry.instance().registerAll();
		ItemRegistry.instance().registerAll();
	}
	
	/** 2 **/
	public void init(FMLInitializationEvent event) {
		
		// Enregistre les events
		this.proxy.registerEvents();
		
		// Initialisation les TileEntities
		this.initTileEntities ();
		
		// Set de l'icon du tab creative
		this.tabBuildingStaff.setIcon(this.itemBuilding);
		
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
	 * // Nom des TileEntities
	 */
	private void initTileEntities () {
		GameRegistry.registerTileEntity(TileEntityBlockSpawner.class, "GollumCoreLib:BlockSpawner");
	}
	
	/**
	 * Initialisation des blocks
	 */
	public void initBlocks () {
		
		// Création des blocks
		this.blockProximitySpawn = new BlockProximitySpawn (this.config.blockSpawnerID, "BlockProximitySpawn");
		
	}
	
	/**
	 * Initialisation des items
	 */
	public void initItem () {
		
		// Création des items
		this.itemBuilding = (ItemBuilding)new ItemBuilding(this.config.itemBuildingID, "ItemBuilding").setCreativeTab(this.tabBuildingStaff);
		
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
