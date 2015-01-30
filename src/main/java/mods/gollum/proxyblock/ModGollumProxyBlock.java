package mods.gollum.proxyblock;

import mods.gollum.core.common.i18n.I18n;
import mods.gollum.core.common.log.Logger;
import mods.gollum.core.common.mod.GollumMod;
import mods.gollum.core.common.version.VersionChecker;
import mods.gollum.proxyblock.common.CommonProxyGollumProxyBlock;
import mods.gollum.proxyblock.inits.ModBlocks;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModGollumProxyBlock.MODID, name = ModGollumProxyBlock.MODNAME, version = ModGollumProxyBlock.VERSION, acceptedMinecraftVersions = ModGollumProxyBlock.MINECRAFT_VERSION, dependencies = ModGollumProxyBlock.DEPENDENCIES)
public class ModGollumProxyBlock extends GollumMod {

	public final static String MODID = "GollumProxyBlock";
	public final static String MODNAME = "Gollum Proxy Block";
	public final static String VERSION = "1.0.0";
	public final static String MINECRAFT_VERSION = "1.7.10";
	public final static String DEPENDENCIES = "required-after:GollumCoreLib";
	
	@Instance(ModGollumProxyBlock.MODID)
	public static ModGollumProxyBlock instance; 
	
	@SidedProxy(clientSide = "mods.gollum.proxyblock.client.ClientProxyGollumProxyBlock", serverSide = "mods.gollum.proxyblock.common.CommonProxyGollumProxyBlock")
	public static CommonProxyGollumProxyBlock proxy; 

	/**
	 * Gestion des logs
	 */
	public static Logger log;
	
	/**
	 * Gestion de l'i18n
	 */
	public static I18n i18n;
	
//	/**
//	 * La configuration
//	 */
//	public static ConfigGollumProxyBlock config;
	
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
		ModBlocks.blockProxy1.setTarget(Blocks.gold_block);
	}
	
}
