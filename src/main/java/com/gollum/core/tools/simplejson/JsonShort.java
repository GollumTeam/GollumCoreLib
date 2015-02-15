package com.gollum.core.tools.simplejson;

import com.gollum.core.tools.simplejson.Json.TYPE;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class JsonShort extends Json {
	
	public JsonShort(short s) {
		this.value = s;
	}
	
	@Override
	public short shortValue()  { return (Short)this.value; }
	
	public TYPE getType () {
		return TYPE.SHORT;
	}
	
	public void setValue(Object value) {
		try {
			this.value = Short.parseShort(value.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		this.value = (short)0;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder argoJson() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
