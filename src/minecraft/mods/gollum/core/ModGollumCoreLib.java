
package mods.gollum.core;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.blocks.BlockSpawner;
import mods.gollum.core.config.ConfigLoader;
import mods.gollum.core.config.ConfigProp;
import mods.gollum.core.facory.BlockFactory;
import mods.gollum.core.log.Logger;
import mods.gollum.core.tileentities.TileEntityBlockSpawner;
import mods.gollum.core.version.VersionChecker;
import net.minecraft.block.Block;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ModGollumCoreLib.MODID, name = ModGollumCoreLib.MODNAME, version = ModGollumCoreLib.VERSION, acceptedMinecraftVersions = ModGollumCoreLib.MINECRAFT_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModGollumCoreLib {

	public final static String MODID = "GollumCoreLib";
	public final static String MODNAME = "Gollum Core Lib";
	public final static String VERSION = "1.1.0";
	public final static String MINECRAFT_VERSION = "1.6.4";
	
	@ConfigProp (info = "Log display level (DEBUG, INFO, WARNING, SEVERE, NONE)")
	public static String level = "WARNING";
	
	@ConfigProp (info = "Display version checker message")
	public static boolean versionChecker = true;
	
	@ConfigProp(group = "Blocks Ids")
	public static int blockSpawnerID = 1243;
	
	public static Block blockSpawner;
	
	/**
	 * Gestion des logs
	 */
	public static Logger log;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		// Creation du logger
		log = new Logger(event);
		
		// Charge la configuration
		ConfigLoader configLoader = new ConfigLoader(this.getClass(), event);
		configLoader.loadConfig();

		// Affecte la config
		VersionChecker.setDisplay(versionChecker);
		
		// Gestion de la nivaeu de log
		Logger.setLevelDisplay(level);
		
		// Creation du checker de version
		new VersionChecker(this);
	}
	
	/** 2 **/
	@EventHandler
	public void init(FMLInitializationEvent event) {

		// Initialisation des blocks
		this.initBlocks ();

		// Initialisation les TileEntities
		this.initTileEntities ();
	}

	/** 3 **/
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

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
		
		BlockFactory factory = new BlockFactory ();
		
		// Création des blocks
		this.blockSpawner = factory.create (new BlockSpawner(this.blockSpawnerID), "GCLBlockSpawner");
		
	}
	
}
