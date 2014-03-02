
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

@Mod(modid = "GollumCoreLib", name = "Gollum Core Lib", version = "1.0.0", acceptedMinecraftVersions = "1.7.2")
public class ModGollumCoreLib {
	

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
	public void load(FMLInitializationEvent event) {
		
	}

	/** 3 **/
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
	
}
