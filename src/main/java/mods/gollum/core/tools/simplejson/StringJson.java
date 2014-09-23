package mods.gollum.core.tools.simplejson;

import java.util.ArrayList;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class StringJson extends Json {

	public StringJson(String s) {
		this.value = s;
	}
	
	public TYPE getType () {
		return TYPE.STRING;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aStringBuilder(this.value.toString());
	}
}
