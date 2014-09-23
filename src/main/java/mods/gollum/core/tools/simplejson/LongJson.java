package mods.gollum.core.tools.simplejson;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class LongJson extends Json {
	
	public LongJson(long l) {
		this.value = l;
	}
	
	@Override
	public long longValue()  { return (Long)this.value; }
	
	public TYPE getType () {
		return TYPE.LONG;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
