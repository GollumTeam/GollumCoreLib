package mods.gollum.core.config;

public interface IConfigClass {
	
	public void readConfig (String configs) throws Exception;

	public String writeConfig();
	
}
