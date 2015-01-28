package mods.gollum.core.common.config.type;

import mods.gollum.core.tools.simplejson.Json;

public abstract class ConfigJsonType {
	
	public abstract void readConfig(Json json);
	
	public abstract Json writeConfig();
	
	public boolean equals (Object obj) {
		
		if (obj instanceof ConfigJsonType) {
			return ((ConfigJsonType)obj).writeConfig().equals(this.writeConfig());
		}
		
		return false;
	}
}
