package com.gollum.core.common.mod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.client.gui.config.ConfigModGuiFactory;
import com.gollum.core.common.config.Config;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.i18n.I18n;
import com.gollum.core.common.log.Logger;
import com.gollum.core.tools.handler.GCLArrayGuiHandler;
import com.gollum.core.tools.handler.GCLGuiHandler;
import com.gollum.core.tools.registry.BlockRegistry;
import com.gollum.core.tools.registry.GCLNetworkRegistry;
import com.gollum.core.tools.registry.InventoryRegistry;
import com.gollum.core.tools.registry.ItemRegistry;

import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public abstract class GollumMod {

	private String modId;
	private String modName;
	private String version;
	private String minecraftVersion;

	private int mobId = 0;
	
	public GollumMod() {

		this.modId = "Error";
		for (Annotation annotation : this.getClass().getAnnotations()) {
			if (annotation instanceof Mod) {
				this.modId = ((Mod)annotation).modid();
			}
		}
		this.modName = "Error";
		for (Annotation annotation : this.getClass().getAnnotations()) {
			if (annotation instanceof Mod) {
				this.modName = ((Mod)annotation).name();
			}
		}
		
		this.version = "0.0.0 [DEV]";
		
		for (Annotation annotation : this.getClass().getAnnotations()) {
			if (annotation instanceof Mod) {;
			this.version = ((Mod)annotation).version();
			}
		}
		
		this.minecraftVersion = Loader.instance().getMinecraftModContainer().getVersion();
		
	}
	
	/**
	 * Renvoie le modID du MOD
	 */
	public String getModId () {
		return this.modId;
	}
	
	/**
	 * Renvoie le modName du MOD
	 */
	public String getModName () {
		return this.modName;
	}
	
	/**
	 * Renvoie la version du MOD
	 */
	public String getVersion () {
		return version;
	}
	
	/**
	 * Renvoie la version de Minecraft
	 */
	public String getMinecraftVersion () {
		return this.minecraftVersion;
	}
	
	/**
	 * @return conatiner of mod
	 */
	public ModContainer getContainer() {
		return Loader.instance().getIndexedModList().get(this.getModId());
	}
	
	public int nextMobID() {
		return ++this.mobId ;
	}
	
	public I18n i18n() {
		I18n i18n = null;
		
		try {
			Field f = this.getClass().getDeclaredField("i18n");
			f.setAccessible(true);
			i18n = (I18n) f.get(this);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return i18n;
	}

	public Logger log() {
		Logger log = null;
		
		try {
			Field f = this.getClass().getDeclaredField("log");
			f.setAccessible(true);
			log = (Logger) f.get(this);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return log;
	}
	
	/**
	 * Set gollum gui config
	 */
	protected void initGuiConfig() {
		ModContainer container = this.getContainer();
		if (container instanceof FMLModContainer) {
			
			// Set gollum gui config
			try {
				Field f = (container.getClass().getDeclaredField("descriptor"));
				f.setAccessible(true);
				Map<String, Object> descriptor = (Map<String, Object>)f.get(container);
				if (!descriptor.containsKey("guiFactory")) {
					descriptor.put ("guiFactory", ConfigModGuiFactory.class.getName());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	protected void initLogguer() {
		try {
			Field fieldLog = this.getClass().getDeclaredField("logger");
			if (fieldLog != null) {
				fieldLog.set(null, new Logger());
			}
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	protected void initI18n() {
		try {
			Field fieldI18n = this.getClass().getDeclaredField("i18n");
			if (fieldI18n != null) {
				fieldI18n.set(null, new I18n());
			}
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	protected void initConfig() {
		try {
			Field fieldConfig = this.getClass().getDeclaredField("config");
			if (fieldConfig != null) {
				fieldConfig.set(null, (fieldConfig.getType().newInstance()));
				Config config = (Config) fieldConfig.get(null);
				config.loadConfig();
			}
		} catch (NoSuchFieldException e) {
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
	
	public void handler (FMLPreInitializationEvent event) {
		
		ModContext.instance ().setCurrent(this);
		
		// Creation du logger
		this.initLogguer();

		// Creation de l'i18n
		this.initI18n();
		
		// Set gollum gui config
		this.initGuiConfig();
		
		// Creation de la config
		this.initConfig();
		
		this.preInit(event);

		// Enregistre les block et item
		BlockRegistry.instance().registerAll();
		ItemRegistry .instance().registerAll();
		
		// Enregistre les rendus
		ModGollumCoreLib.proxy.registerObjectRenders();
		
		ModContext.instance ().pop();
	}
	
	public void handler (FMLInitializationEvent event) {
		
		ModContext.instance ().setCurrent(this);
		
		this.init(event);
		
		this.initGuiCommon ();
		if (ModGollumCoreLib.proxy.isRemote()) {
			this.initGuiClient();
		}
		
		// Init recipe from JSON
		CraftingHelper.loadRecipes(true);
		
		// Enregistrement du handler de gui d'inventaire simplifié
		GCLNetworkRegistry.instance().registerGuiHandler(new GCLGuiHandler(InventoryRegistry.instance().getGuiInventoryList()));
		
		// Enregistrement de tous les Gui groupé
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GCLArrayGuiHandler(GCLNetworkRegistry.instance().getGuiHandlers()));
		
		ModContext.instance ().pop();
	}
	
	public void handler (FMLPostInitializationEvent event) {
		
		ModContext.instance ().setCurrent(this);
		
		this.postInit(event);
		
		ModContext.instance ().pop();
	}
	
	public boolean equals (Object o) {
		if (o instanceof GollumMod) {
			return this.getModId().equals(((GollumMod) o).getModId());
		}
		return this == o;
	}
	
	/////////////////////
	// Extends methods //
	/////////////////////

	/**
	 * Initialisation des GUI côté serveur et client
	 */
	public void initGuiCommon () {}
	
	/**
	 * Initialisation des GUI côté client
	 */
	public void initGuiClient () {}
	
	public abstract void preInit (FMLPreInitializationEvent event);
	public abstract void init(FMLInitializationEvent event);
	public abstract void postInit(FMLPostInitializationEvent event);
	
}
