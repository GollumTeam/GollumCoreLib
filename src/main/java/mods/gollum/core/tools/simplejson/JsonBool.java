package mods.gollum.core.tools.simplejson;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class JsonBool extends Json {
	
	public JsonBool(boolean b) {
		this.value = b;
	}
	
	public boolean boolValue () { return (Boolean)this.value; }
	
	public TYPE getType () {
		return TYPE.BOOLEAN;
	}
	
	public void setValue(Object value) {
		try {
			this.value = Boolean.parseBoolean(value.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return ((Boolean)value) ? JsonNodeBuilders.aTrueBuilder() : JsonNodeBuilders.aFalseBuilder();
	}
}
