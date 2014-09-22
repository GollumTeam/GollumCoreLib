package mods.gollum.core.common.config.dom;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class BoolConfigDom extends ConfigDom {
	
	public BoolConfigDom(boolean b) {
		this.value = b;
	}
	
	public boolean boolValue () { return (Boolean)this.value; }
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return ((Boolean)value) ? JsonNodeBuilders.aTrueBuilder() : JsonNodeBuilders.aFalseBuilder();
	}
}
