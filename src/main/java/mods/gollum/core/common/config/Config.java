package mods.gollum.core.common.config;

import mods.gollum.core.common.context.ModContext;


public abstract class Config<T> implements Cloneable {
	
	private String fileName;
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
	
	public T loadConfig() {
		// Charge la configuration
		ConfigLoader configLoader = new ConfigLoader(this);
		configLoader.loadConfig();
		return (T) this;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		Config<T> cloned = null;
		try {
			cloned = (Config<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return cloned;
	}
	
	public boolean isMain() {
		return isMain;
	}
}
