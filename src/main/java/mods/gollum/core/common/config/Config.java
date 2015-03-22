package mods.gollum.core.common.config;

import mods.gollum.core.common.context.ModContext;


public abstract class Config<T> {
	
	private String fileName;
	private String relativePath = "";
	
	public Config() {
		this(ModContext.instance().getCurrent().getModId());
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
	
	public T loadConfig() {
		// Charge la configuration
		ConfigLoader configLoader = new ConfigLoader(this);
		configLoader.loadConfig();
		return (T) this;
	}
}
