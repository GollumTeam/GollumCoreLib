package com.gollum.autoreplace;

import com.gollum.autoreplace.common.CommonProxyGollumAutoReplace;
import com.gollum.autoreplace.common.config.ConfigGollumAutoReplace;
import com.gollum.autoreplace.inits.ModBlocks;
import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.i18n.I18n;
import com.gollum.core.common.log.Logger;
import com.gollum.core.common.mod.GollumMod;
import com.gollum.core.common.version.VersionChecker;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModGollumAutoReplace.MODID, name = ModGollumAutoReplace.MODNAME, version = ModGollumAutoReplace.VERSION, acceptedMinecraftVersions = ModGollumAutoReplace.MINECRAFT_VERSION, dependencies = ModGollumAutoReplace.DEPENDENCIES)
public class ModGollumAutoReplace extends GollumMod {

	public final static String MODID = "GollumAutoReplace";
	public final static String MODNAME = "Gollum Auto Replace";
	public final static String VERSION = "1.0.0";
	public final static String MINECRAFT_VERSION = "1.7.10";
	public final static String DEPENDENCIES = "required-after:"+ModGollumCoreLib.MODID;
	
	@Instance(ModGollumAutoReplace.MODID)
	public static ModGollumAutoReplace instance; 
	
	@SidedProxy(clientSide = "com.gollum.autoreplace.client.ClientProxyGollumAutoReplace", serverSide = "com.gollum.autoreplace.common.CommonProxyGollumAutoReplace")
	public static CommonProxyGollumAutoReplace proxy; 

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
	public static ConfigGollumAutoReplace config;
	
	@EventHandler public void handler(FMLPreInitializationEvent event)  { super.handler (event); }
	@EventHandler public void handler(FMLInitializationEvent event)     { super.handler (event); }
	@EventHandler public void handler(FMLPostInitializationEvent event) { super.handler (event); }
	
	/** 1 **/
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
		// Test la version du mod
		new VersionChecker();
		
		// Initialisation des blocks
		ModBlocks.init();
		
	}
	
	/** 2 **/ 
	@Override
	public void init(FMLInitializationEvent event) {
	}
	
	/** 3 **/
	@Override
	public void postInit(FMLPostInitializationEvent event) {
	}
	
}
