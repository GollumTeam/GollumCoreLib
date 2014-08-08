
package mods.gollum.core;

import mods.gollum.core.config.ConfigLoader;
import mods.gollum.core.config.ConfigProp;
import mods.gollum.core.log.Logger;
import mods.gollum.core.version.VersionChecker;
import net.minecraft.block.Block;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = ModGollumCoreLib.MODID, name = ModGollumCoreLib.MODNAME, version = ModGollumCoreLib.VERSION, acceptedMinecraftVersions = ModGollumCoreLib.MINECRAFT_VERSION)
public class ModGollumCoreLib {
	

	public final static String MODID = "GollumCoreLib";
	public final static String MODNAME = "Gollum Core Lib";
	public final static String VERSION = "1.1.0";
	public final static String MINECRAFT_VERSION = "1.7.2";

	@ConfigProp (info = "Log display level (DEBUG, INFO, WARNING, SEVERE, NONE)")
	public static String level = "WARNING";
	
	@ConfigProp (info = "Display version checker message")
	public static boolean versionChecker = true;

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
		Logger.setLevelDisplay(level);;
		
	}
	
	/** 2 **/
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
	}

	/** 3 **/
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
	
}
