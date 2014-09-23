package mods.gollum.core.tools.simplejson;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class DoubleJson extends Json {
	
	public DoubleJson(double d) {
		this.value = d;
	}
	public double doubleValue () { return (Double)this.value; }
	
	public TYPE getType () {
		return TYPE.DOUBLE;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
