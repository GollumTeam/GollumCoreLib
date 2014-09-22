package mods.gollum.core.common.config.type;

public interface IConfigClass {
	
	public void readConfig (String configs) throws Exception;

	public String writeConfig();
	
}
