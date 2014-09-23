package mods.gollum.core.tools.simplejson;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class IntJson extends Json {
	
	public IntJson(int i) {
		this.value = i;
	}
	
	public int intValue () { return (Integer)this.value; }
	
	public TYPE getType () {
		return TYPE.INT;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
