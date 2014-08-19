package mods.gollum.core.config;

import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;

public interface IConfigJsonClass {
	
	public void readConfig (JsonNode json) throws Exception;

	public JsonRootNode writeConfig();
	
}
