package mods.gollum.core.tools.simplejson;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class CharJson extends Json {
	
	public CharJson(char c) {
		this.value = c;
	}
	
	public char charValue () { return (Character)this.value; }
	
	public TYPE getType () {
		return TYPE.CHAR;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(((byte)((Character)value).charValue())+"");
	}
}
