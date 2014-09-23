package mods.gollum.core.tools.simplejson;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class ShortJson extends Json {
	
	public ShortJson(short s) {
		this.value = s;
	}
	
	@Override
	public short shortValue()  { return (Short)this.value; }
	
	public TYPE getType () {
		return TYPE.SHORT;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
