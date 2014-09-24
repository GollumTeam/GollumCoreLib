package mods.gollum.core.common.config.type;

import mods.gollum.core.tools.simplejson.Json;

public interface IConfigJsonType {
	
	public void readConfig (Json json);

	public Json writeConfig();
	
}
