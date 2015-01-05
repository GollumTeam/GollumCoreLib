package mods.gollum.core.tools.simplejson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import mods.gollum.core.tools.simplejson.Json.TYPE;
import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonObjectNodeBuilder;

public class JsonObject extends Json {
	
	public JsonObject () {
		this.value = new HashMap<String, Json>();
	}
	
	public String strValue()    { return ((HashMap<String, Json>)this.value).size()+""; }
	public boolean boolValue()  { return ((HashMap<String, Json>)this.value).size() > 0; }
	
	public Json child (int  i)      { return child (i+""); }
	public Json child (String  key) { return this.containsKey(key) ? ((HashMap<String, Json>)this.value).get(key) : create(); }
	
	public Collection<Json> allChild () { return ((HashMap<String, Json>)this.value).values(); }
	public Set<Entry<String, Json>> allChildWithKey ()  { return ((HashMap<String, Json>)this.value).entrySet(); }
	
	public int size() { return ((HashMap<String, Json>)this.value).size(); }
	
	public void add(String key, Json child) {
		if (((HashMap<String, Json>)this.value).containsKey(key)) {
			((HashMap<String, Json>)this.value).remove(key);
		}
		((HashMap<String, Json>)this.value).put(key, child);
	}
	
	public boolean containsKey (int i)      { return this.containsKey(i+""); }
	public boolean containsKey (String key) { return ((HashMap<String, Json>)this.value).containsKey(key); }
	
	public boolean contains (Json json) { return ((HashMap<String, Json>)this.value).containsValue (json); }
	
	public void clear() {
		((HashMap<String, Json>)this.value).clear();
	}
	
	public boolean equals (Object obj) {
		
		if (obj instanceof JsonObject) {
			
			if (this.size() != ((JsonObject)obj).size()) {
				return false;
			}
			
			for (Entry<String, Json> entry : ((HashMap<String, Json>)this.value).entrySet()) {
				
				if (
					!((HashMap<String, Json>)((JsonObject)obj).value).containsKey(entry.getKey()) ||
					!((HashMap<String, Json>)this.value).get(entry.getKey()).equals (((HashMap<String, Json>)((JsonObject)obj).value).get(entry.getKey()))
				) {
					return false;
				}
			}
		}
		return true;
	}
	
	public TYPE getType () {
		return TYPE.OBJECT;
	}
	
	public void setValue(Object value) {
		if (value instanceof Map) {
			HashMap<String, Json> tmpMap = new HashMap<String, Json>();
			for (Entry<Object, Object> entry : ((Map<Object, Object>)value).entrySet()) {
				if (!(entry.getKey() instanceof String) || !(entry.getValue() instanceof Json)) {
					return;
				}
				tmpMap.put((String) entry.getKey(), (Json) entry.getValue());
			}
			this.value = tmpMap;
		}
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder argoJson() {
		
		JsonObjectNodeBuilder o = JsonNodeBuilders.anObjectBuilder();
		for (Entry<String, Json> entry : ((HashMap<String, Json>)this.value).entrySet()) {
			o.withField(entry.getKey(), entry.getValue().argoJson());
		}
		return o;
	}
}
