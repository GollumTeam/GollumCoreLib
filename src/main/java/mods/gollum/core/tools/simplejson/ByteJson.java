package mods.gollum.core.tools.simplejson;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class ByteJson extends Json {
	
	public ByteJson(byte b) {
		this.value = b;
	}
	
	public byte byteValue () { return (Byte)this.value; }
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
