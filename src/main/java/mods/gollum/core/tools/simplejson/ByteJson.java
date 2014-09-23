package mods.gollum.core.tools.simplejson;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class ByteJson extends Json {
	
	public ByteJson(byte b) {
		this.value = b;
	}
	
	public byte byteValue () { return (Byte)this.value; }
	
	public TYPE getType () {
		return TYPE.BYTE;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
