package mods.gollum.core.common.config.dom;

import java.util.ArrayList;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class StringConfigDom extends ConfigDom {

	public StringConfigDom(String s) {
		this.value = s;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aStringBuilder(this.value.toString());
	}
}
