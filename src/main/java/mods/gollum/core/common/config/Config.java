package mods.gollum.core.common.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.common.context.ModContext;
import mods.gollum.core.tools.simplejson.Json;


public abstract class Config implements Cloneable {
	
	private String fileName = "";
	private String relativePath = "";
	private boolean isMain = false;
	
	public Config() {
		this(ModContext.instance().getCurrent().getModId());
		this.isMain = true;
	}

	public Config(String fileName) {
		this.fileName = fileName;
	}
	
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public String getRelativePath() {
		return this.relativePath;
	}
	
	public Config loadConfig() {
		// Charge la configuration
		ConfigLoader configLoader = new ConfigLoader(this);
		configLoader.loadConfig();
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		Config cloned = null;
		try {
			cloned = (Config) super.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cloned;
	}
	
	
	public boolean equals (Object value) {
		
		boolean equal = true;
		
		if (value instanceof Config) {
			try {
				for (Field f : this.getClass().getDeclaredFields()) {
					if ( f.isAccessible() && Modifier.isStatic(f.getModifiers()) ) {
						if ( f.get(this) == null ) {
							if (f.get(value) != null) {
								return false;
							}
						} else {
							if (!f.get(this).equals(f.get(value))) {
								return false;
							}
						}
					}
				}
			} catch (Exception e) {
				return false;
			}
		} else{
			return false;
		}
		
		return true;
	}
	
	public boolean isMain() {
		return isMain;
	}
}
