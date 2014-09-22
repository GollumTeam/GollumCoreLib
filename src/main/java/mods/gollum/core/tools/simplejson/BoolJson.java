package mods.gollum.core.tools.simplejson;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class BoolJson extends Json {
	
	public BoolJson(boolean b) {
		this.value = b;
	}
	
	public boolean boolValue () { return (Boolean)this.value; }
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return ((Boolean)value) ? JsonNodeBuilders.aTrueBuilder() : JsonNodeBuilders.aFalseBuilder();
	}
}
