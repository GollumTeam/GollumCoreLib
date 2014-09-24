package mods.gollum.core.tools.simplejson;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;

public class JsonArray extends Json {
	
	public JsonArray () {
		 this.value = new ArrayList<Json>();
	}
	
	public String strValue()    { return ((List<Json>)this.value).size()+""; }
	public boolean boolValue()  { return ((List<Json>)this.value).size() > 0; }
	
	public Json child (int  i) { return this.containsKey(i) ? ((List<Json>)value).get(i) : create(); }
	public Json child (String  key) { 
		try  {
			return child (Integer.parseInt(key));
		} catch (Exception e) {
		}
		return create(); 
	}

	public Collection<Json> allChild () { return ((List<Json>)this.value); }
	public Set<Entry<String, Json>> allChildWithKey ()  {
		HashSet<Entry<String, Json>> h = new HashSet<Entry<String, Json>>(); 
		
		int i = 0;
		for(Json val : ((List<Json>)this.value)) {
			h.add(new AbstractMap.SimpleEntry<String, Json>(i+"", val));
			i++;
		}
		
		return h;
	}
	
	public int size() { return ((List<Json>)this.value).size(); }
	
	public void add(Json child) {
		((List<Json>)this.value).add(child);
	}
	
	public boolean containsKey (int i)      { return i < ((List<Json>)value).size(); }
	public boolean containsKey (String key) { 
		try  {
			return containsKey (Integer.parseInt(key));
		} catch (Exception e) {
		}
		return false; 
	}
	
	public boolean contains (Json json) { return ((List<Json>)this.value).contains(json); }
	
	
	public boolean equals (Object obj) {
		
		if (this.size() != ((JsonArray)obj).size()) {
			return false;
		}
		
		for (int i = 0; i < this.size(); i++) {
			if (!((List<Json>)this.value).get(i).equals (((List<Json>)((JsonArray)obj).value).get(i))) {
				return false;
			}
		}
		
		return true;
	}
	
	public TYPE getType () {
		return TYPE.ARRAY;
	}
	
	public void setValue(Object value) {
		if (value instanceof List) {
			List<Json> tmpList = new ArrayList<Json>();
			for (Object json : ((List)value)) {
				if (!(json instanceof Json)) {
					return;
				}
				tmpList.add((Json) json);
			}
			this.value = tmpList;
		}
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		
		JsonArrayNodeBuilder ar = JsonNodeBuilders.anArrayBuilder();
		for (Json val : (List<Json>)this.value) {
			ar.withElement(val.json());
		}
		return ar;
	}
}
