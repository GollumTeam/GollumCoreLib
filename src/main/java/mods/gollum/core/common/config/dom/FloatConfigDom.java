package mods.gollum.core.common.config.dom;

import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class FloatConfigDom extends ConfigDom {
	
	public FloatConfigDom(float f) {
		this.value = f;
	}
	
	public float floatValue () { return (Float)this.value; 	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNumberBuilder(value+"");
	}
}
