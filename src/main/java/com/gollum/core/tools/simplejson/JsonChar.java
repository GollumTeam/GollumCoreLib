package com.gollum.core.tools.simplejson;

import com.gollum.core.tools.simplejson.Json.TYPE;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class JsonChar extends Json {
	
	public JsonChar(char c) {
		this.value = c;
	}
	
	public char charValue ()  { return (Character)this.value; }
	public String  strValue() { return ((int)charValue())+""; }
	
	public TYPE getType () {
		return TYPE.CHAR;
	}
	
	public void setValue(Object value) {
		try {
			this.value = (char) (Byte.parseByte(value.toString()) & 0x00FF);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		this.value = (char)0;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder argoJson() {
		return JsonNodeBuilders.aNumberBuilder(((byte)((Character)value).charValue())+"");
	}
}
