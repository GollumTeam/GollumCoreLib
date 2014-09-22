package mods.gollum.core.common.config.dom;

import java.util.ArrayList;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class ArrayConfigDom extends ConfigDom {
	
	public ArrayList<ConfigDom> values = new ArrayList<ConfigDom>();
	
	public Object value () {
		return this.values;
	}
	
	public String strValue()    { return this.values.size()+""; }
	public boolean boolValue()  { return this.values.size() > 0; }
	
	public ConfigDom child (int  i) { return (i < values.size())? values.get(i) : create(); }
	public ConfigDom child (String  key) { 
		try  {
			return child (Integer.parseInt(key));
		} catch (Exception e) {
		}
		return create(); 
	}

	public int size() { return this.values.size(); }
	
	public void add(ConfigDom child) {
		this.values.add(child);
	}
	
	public boolean equals (ArrayConfigDom obj) {
		
		if (this.size() != obj.size()) {
			return false;
		}
		
		for (int i = 0; i < this.size(); i++) {
			if (!this.values.get(i).equals (obj.values.get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		
		JsonArrayNodeBuilder ar = JsonNodeBuilders.anArrayBuilder();
		for (ConfigDom value : values) {
			ar.withElement(value.json());
		}
		return ar;
	}
}
