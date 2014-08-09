package mods.gollum.core.mod;

import java.lang.annotation.Annotation;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;

public class ModMetaInfos {

	Object mod;
	
	public ModMetaInfos(Object mod) {
		this.mod = mod;
	}

	/**
	 * Renvoie le modID du MOD
	 * @return String
	 */
	public String getModid () {
		String modid = "Error";
		
		for (Annotation annotation : this.mod.getClass().getAnnotations()) {
			if (annotation instanceof Mod) {
				modid = ((Mod)annotation).modid();
			}
		}
		
		return modid;
	}
	
	/**
	 * Renvoie la version du MOD
	 * @return String
	 */
	public String getVersion () {
		String version = "0.0.0 [DEV]";
		
		for (Annotation annotation : this.mod.getClass().getAnnotations()) {
			if (annotation instanceof Mod) {;
				version = ((Mod)annotation).version();
			}
		}
		
		return version;
	}
	

	/**
	 * Renvoie la version de Minecraft
	 * @return String
	 */
	public String getMinecraftVersion () {
		return Loader.instance().getMinecraftModContainer().getVersion();
	}
	
}
