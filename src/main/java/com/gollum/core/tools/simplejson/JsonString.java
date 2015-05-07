package com.gollum.core.tools.simplejson;

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
	
	public void clear() {
		this.value = "";
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder argoJson() {
		return JsonNodeBuilders.aStringBuilder(this.value.toString());
	}
}
