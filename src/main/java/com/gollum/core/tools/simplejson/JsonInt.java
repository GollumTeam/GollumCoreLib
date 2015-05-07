package com.gollum.core.tools.simplejson;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class JsonInt extends Json {
	
	public JsonInt(int i) {
		this.value = i;
	}
	
	public int intValue () { return (Integer)this.value; }
	
	public TYPE getType () {
		return TYPE.INT;
	}
	
	public void setValue(Object value) {
		try {
			this.value = Integer.parseInt(value.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		this.value = 0;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder argoJson() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
