package mods.gollum.core.mod;

import java.io.File;
import java.lang.annotation.Annotation;

import mods.castledefenders.config.ConfigCastleDefender;
import mods.gollum.core.i18n.I18n;
import mods.gollum.core.log.Logger;
import mods.gollum.core.version.VersionChecker;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.Side;

public abstract class GollumMod {

	// Gestion des logs
	public static Logger log;
	
	// Gestion de l'i18n
	public static I18n i18n;
	
	private String modid;
	private String version;
	private String minecraftVersion;

	private int mobId = 0;
	
	public GollumMod() {
		
		this.modid = "Error";
		for (Annotation annotation : this.getClass().getAnnotations()) {
			if (annotation instanceof Mod) {
				this.modid = ((Mod)annotation).modid();
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
	public String getModid () {
		return this.modid;
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
	
	public void preInit(FMLPreInitializationEvent event) {
		
		// Creation du logger
		log = new Logger(event);
		
		// Creation du logger
		i18n = new I18n(this);
	}


	public int nextMobID() {
		return ++this.mobId ;
	}
	
}
