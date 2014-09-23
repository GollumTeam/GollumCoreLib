package mods.gollum.core.tools.simplejson;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class FloatJson extends Json {
	
	public FloatJson(float f) {
		this.value = f;
	}
	
	public float floatValue () { return (Float)this.value; 	}
	
	public TYPE getType () {
		return TYPE.FLOAT;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
