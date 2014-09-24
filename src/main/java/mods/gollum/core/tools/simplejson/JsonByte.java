package mods.gollum.core.tools.simplejson;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class JsonByte extends Json {
	
	public JsonByte(byte b) {
		this.value = b;
	}
	
	public byte byteValue () { return (Byte)this.value; }
	
	public TYPE getType () {
		return TYPE.BYTE;
	}
	
	public void setValue(Object value) {
		try {
			this.value = Byte.parseByte(value.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
