package mods.gollum.core.common.config.dom;

import java.util.HashMap;
import java.util.Map.Entry;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonObjectNodeBuilder;

public class ObjectConfigDom extends ConfigDom {
	
	public HashMap<String, ConfigDom> values = new HashMap<String, ConfigDom>();
	
	public Object value () {
		return this.values;
	}
	public String strValue()    { return this.values.size()+""; }
	public boolean boolValue()  { return this.values.size() > 0; }

	public ConfigDom child (int  i)      { return child (i+""); }
	public ConfigDom child (String  key) { return (this.values.containsKey(key))? values.get(key) : create(); }

	public int size() { return this.values.size(); }
	
	public void add(String key, ConfigDom child) {
		if (this.values.containsKey(key)) {
			this.values.remove(key);
		}
		this.values.put(key, child);
	}
	
	public boolean equals (ObjectConfigDom obj) {
		
		if (this.size() != obj.size()) {
			return false;
		}
		
		for (Entry<String, ConfigDom> entry : this.values.entrySet()) {
			
			if (!obj.values.containsKey(entry.getKey()) || !this.values.get(entry.getKey()).equals (obj.values.get(entry.getKey()))) {
				return false;
			}
		}
		
		return true;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public JsonNodeBuilder json() {
		
		JsonObjectNodeBuilder o = JsonNodeBuilders.anObjectBuilder();
		for (Entry<String, ConfigDom> entry : values.entrySet()) {
			o.withField(entry.getKey(), entry.getValue().json());
		}
		return o;
	}
}
