package com.gollum.core.tools.simplejson;

import com.gollum.core.tools.simplejson.Json.TYPE;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class JsonFloat extends Json {
	
	public JsonFloat(float f) {
		this.value = f;
	}
	
	public float floatValue () { return (Float)this.value; 	}
	
	public TYPE getType () {
		return TYPE.FLOAT;
	}
	
	public void setValue(Object value) {
		try {
			this.value = Float.parseFloat(value.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		this.value = 0.0F;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder argoJson() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
