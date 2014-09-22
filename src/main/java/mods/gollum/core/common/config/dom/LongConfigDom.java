package mods.gollum.core.common.config.dom;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class LongConfigDom extends ConfigDom {
	
	public LongConfigDom(long l) {
		this.value = l;
	}
	
	@Override
	public long longValue()  { return (Long)this.value; }
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
