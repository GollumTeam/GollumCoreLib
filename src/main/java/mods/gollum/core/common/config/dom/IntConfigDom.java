package mods.gollum.core.common.config.dom;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class IntConfigDom extends ConfigDom {
	
	public IntConfigDom(int i) {
		this.value = i;
	}
	
	public int intValue () { return (Integer)this.value; }
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
