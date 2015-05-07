package com.gollum.core.common.mod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;
import com.gollum.core.common.i18n.I18n;
import com.gollum.core.common.log.Logger;
import com.gollum.core.tools.handler.GCLArrayGuiHandler;
import com.gollum.core.tools.handler.GCLGuiHandler;
import com.gollum.core.tools.registry.BlockRegistry;
import com.gollum.core.tools.registry.GCLNetworkRegistry;
import com.gollum.core.tools.registry.InventoryRegistry;
import com.gollum.core.tools.registry.ItemRegistry;

import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

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
	
	public void handler (FMLPreInitializationEvent event) {
		
		ModContext.instance ().setCurrent(this);
		
		// Creation du logger
		try {
			Field fieldLog = this.getClass().getDeclaredField("log");
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
		
		// Creation de l'i18n
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
		
		this.preInit(event);
		
		BlockRegistry.instance().registerAll();
		ItemRegistry.instance().registerAll();
		
		ModContext.instance ().pop();
	}
	
	public void handler (FMLInitializationEvent event) {
		
		ModContext.instance ().setCurrent(this);
		
		this.init(event);
		
		this.initGuiCommon ();
		if (ModGollumCoreLib.proxy.isRemote()) {
			this.initGuiClient();
		}
		
		// Enregistrement du handler de gui d'inventaire simplifié
		GCLNetworkRegistry.instance().registerGuiHandler(new GCLGuiHandler(InventoryRegistry.instance().getGuiInventoryList()));
		
		// Enregistrement de tous les Gui groupé
		NetworkRegistry.instance().registerGuiHandler(this, new GCLArrayGuiHandler(GCLNetworkRegistry.instance().getGuiHandlers()));
		
		ModContext.instance ().pop();
	}
	
	public void handler (FMLPostInitializationEvent event) {
		
		ModContext.instance ().setCurrent(this);
		
		this.postInit(event);
		
		ModContext.instance ().pop();
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
