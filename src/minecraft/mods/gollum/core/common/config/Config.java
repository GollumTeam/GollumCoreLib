package mods.gollum.core.common.config;

import mods.gollum.core.common.context.ModContext;


public abstract class Config {
	
	private String fileName;
	
	public Config() {
		this(ModContext.instance().getCurrent().getModId());
	}
	
	public Config(String fileName) {
		this.fileName = fileName;
		
		// Charge la configuration
		ConfigLoader configLoader = new ConfigLoader(this);
		configLoader.loadConfig();
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
}
