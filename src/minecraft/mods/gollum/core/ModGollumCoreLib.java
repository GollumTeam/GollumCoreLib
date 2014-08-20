
package mods.gollum.core;

import mods.castledefenders.ModCastleDefenders;
import mods.gollum.core.blocks.BlockSpawner;
import mods.gollum.core.config.ConfigGollumCoreLib;
import mods.gollum.core.config.ConfigLoader;
import mods.gollum.core.config.ConfigProp;
import mods.gollum.core.facory.BlockFactory;
import mods.gollum.core.log.Logger;
import mods.gollum.core.mod.GollumMod;
import mods.gollum.core.sound.SoundRegistry;
import mods.gollum.core.tileentities.TileEntityBlockSpawner;
import mods.gollum.core.version.VersionChecker;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = ModGollumCoreLib.MODID, name = ModGollumCoreLib.MODNAME, version = ModGollumCoreLib.VERSION, acceptedMinecraftVersions = ModGollumCoreLib.MINECRAFT_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ModGollumCoreLib extends GollumMod {

	public final static String MODID = "GollumCoreLib";
	public final static String MODNAME = "Gollum Core Lib";
	public final static String VERSION = "1.1.0";
	public final static String MINECRAFT_VERSION = "1.6.4";

	@Instance("ModGollumCoreLib")
	public static ModCastleDefenders instance;
	
	@SidedProxy(clientSide = "mods.gollum.core.ClientProxyGolumCoreLib", serverSide = "mods.gollum.core.CommonProxyGolumCoreLib")
	public static CommonProxyGolumCoreLib proxy;
	
	public ConfigGollumCoreLib config;
	
	public static Block blockSpawner;
	
	@EventHandler
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
		super.preInit (event);
		
		// Charge la configuration
		this.config = new ConfigGollumCoreLib(this);

		// Affecte la config
		VersionChecker.setDisplay(this.config.versionChecker);
		
		// Gestion de la nivaeu de log
		Logger.setLevelDisplay(this.config.level);
		
		// Creation du checker de version
		new VersionChecker(this);
	}
	
	/** 2 **/
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		// Enregistre les events
		this.proxy.registerEvents();
		
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
		this.blockSpawner = factory.create (new BlockSpawner(this.config.blockSpawnerID), "GCLBlockSpawner");
		
	}
	
}
