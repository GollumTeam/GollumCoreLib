package mods.gollum.core.common.config.dom;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class ShortConfigDom extends ConfigDom {
	
	public ShortConfigDom(short s) {
		this.value = s;
	}
	
	@Override
	public short shortValue()  { return (Short)this.value; }
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
