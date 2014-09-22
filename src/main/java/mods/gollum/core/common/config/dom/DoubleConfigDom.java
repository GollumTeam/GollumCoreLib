package mods.gollum.core.common.config.dom;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class DoubleConfigDom extends ConfigDom {
	
	public DoubleConfigDom(double d) {
		this.value = d;
	}
	public double doubleValue () { return (Double)this.value; }
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
