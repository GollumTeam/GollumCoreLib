package mods.gollum.core.tools.simplejson;

import java.util.ArrayList;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class JsonString extends Json {

	public JsonString(String s) {
		this.value = s;
	}
	
	public TYPE getType () {
		return TYPE.STRING;
	}
	
	public void setValue(Object value) {
		this.value = value.toString();
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder argoJson() {
		return JsonNodeBuilders.aStringBuilder(this.value.toString());
	}
}
