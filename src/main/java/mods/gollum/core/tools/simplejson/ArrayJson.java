package mods.gollum.core.tools.simplejson;

import java.util.ArrayList;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class ArrayJson extends Json {
	
	public ArrayJson () {
		 this.value = new ArrayList<Json>();
	}
	
	public String strValue()    { return ((ArrayList<Json>)this.value).size()+""; }
	public boolean boolValue()  { return ((ArrayList<Json>)this.value).size() > 0; }
	
	public Json child (int  i) { return (i < ((ArrayList<Json>)value).size()) ? ((ArrayList<Json>)value).get(i) : create(); }
	public Json child (String  key) { 
		try  {
			return child (Integer.parseInt(key));
		} catch (Exception e) {
		}
		return create(); 
	}

	public int size() { return ((ArrayList<Json>)this.value).size(); }
	
	public void add(Json child) {
		((ArrayList<Json>)this.value).add(child);
	}
	
	public boolean equals (Object obj) {
		
		if (this.size() != ((ArrayJson)obj).size()) {
			return false;
		}
		
		for (int i = 0; i < this.size(); i++) {
			if (!((ArrayList<Json>)this.value).get(i).equals (((ArrayList<Json>)((ArrayJson)obj).value).get(i))) {
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
		for (Json val : (ArrayList<Json>)this.value) {
			ar.withElement(val.json());
		}
		return ar;
	}
}
