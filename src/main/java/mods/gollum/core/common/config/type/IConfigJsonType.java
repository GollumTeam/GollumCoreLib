package mods.gollum.core.common.config.type;

import mods.gollum.core.common.config.dom.ConfigDom;

public interface IConfigJsonType {
	
	public void readConfig (ConfigDom json) throws Exception;

	public ConfigDom writeConfig();
	
}
