package mods.gollum.core.tools.simplejson;

import java.util.ArrayList;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class ArrayJson extends Json {
	
	public ArrayList<Json> values = new ArrayList<Json>();
	
	public Object value () {
		return this.values;
	}
	
	public String strValue()    { return this.values.size()+""; }
	public boolean boolValue()  { return this.values.size() > 0; }
	
	public Json child (int  i) { return (i < values.size())? values.get(i) : create(); }
	public Json child (String  key) { 
		try  {
			return child (Integer.parseInt(key));
		} catch (Exception e) {
		}
		return create(); 
	}

	public int size() { return this.values.size(); }
	
	public void add(Json child) {
		this.values.add(child);
	}
	
	public boolean equals (ArrayJson obj) {
		
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
		for (Json value : values) {
			ar.withElement(value.json());
		}
		return ar;
	}
}
