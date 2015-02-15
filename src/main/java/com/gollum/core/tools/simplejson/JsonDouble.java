package com.gollum.core.tools.simplejson;

import com.gollum.core.tools.simplejson.Json.TYPE;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class JsonDouble extends Json {
	
	public JsonDouble(double d) {
		this.value = d;
	}
	public double doubleValue () { return (Double)this.value; }
	
	public TYPE getType () {
		return TYPE.DOUBLE;
	}
	
	public void setValue(Object value) {
		try {
			this.value = Double.parseDouble(value.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		this.value = 0.0D;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder argoJson() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
