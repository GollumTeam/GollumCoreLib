package mods.gollum.core.common.config;

import mods.gollum.core.common.context.ModContext;


public abstract class Config<T> {
	
	private String fileName;
	
	public Config() {
		this(ModContext.instance().getCurrent().getModId());
	}
	
	public Config(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
	public T loadConfig() {
		// Charge la configuration
		ConfigLoader configLoader = new ConfigLoader(this);
		configLoader.loadConfig();
		return (T) this;
	}
}
