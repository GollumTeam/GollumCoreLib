package mods.gollum.core.common.config;

public interface IConfigClass {
	
	public void readConfig (String configs) throws Exception;

	public String writeConfig();
	
}
