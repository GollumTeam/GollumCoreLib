package com.gollum.core.tools.simplejson;

import com.gollum.core.tools.simplejson.Json.TYPE;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class JsonLong extends Json {
	
	public JsonLong(long l) {
		this.value = l;
	}
	
	@Override
	public long longValue()  { return (Long)this.value; }
	
	public TYPE getType () {
		return TYPE.LONG;
	}
	
	public void setValue(Object value) {
		try {
			this.value = Long.parseLong(value.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		this.value = 0L;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder argoJson() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
