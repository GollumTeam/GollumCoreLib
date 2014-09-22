package mods.gollum.core.common.config.type;

import mods.gollum.core.tools.simplejson.Json;

public interface IConfigJsonType {
	
	public void readConfig (Json json) throws Exception;

	public Json writeConfig();
	
}
