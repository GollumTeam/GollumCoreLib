package mods.gollum.core.tools.simplejson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import argo.format.CompactJsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonNodeFactories;
import argo.jdom.JsonRootNode;
import argo.jdom.JsonStringNode;

public class Json implements Cloneable {
	
	enum TYPE {
		NULL,
		OBJECT,
		ARRAY,
		STRING,
		LONG,
		INT,
		SHORT,
		BYTE,
		CHAR,
		DOUBLE,
		FLOAT,
		BOOLEAN
	}
	
	protected Object value;
	protected ArrayList<IJsonComplement> complements = new ArrayList<IJsonComplement>();
	
	public static class EntryObject {
		
		public String key;
		public Json dom;
		
		public EntryObject(String key, Object value) {
			this(key, create(value));
		}
		
		public EntryObject(String key, Json dom) {
			this.key = key;
			this.dom = dom;
		}
		
		public EntryObject addComplement(IJsonComplement jsonComplement) {
			this.dom.addComplement(jsonComplement);
			return this;
		}
	}
	
	public static Json create(Json json) { return create(json.value()).addComplements(json.getComplements());}
	
	public static JsonNull   create()           { return new JsonNull();       }
	public static JsonString create(String str) { return new JsonString (str); }
	public static JsonLong   create(long l)     { return new JsonLong (l);     }
	public static JsonInt    create(int i)      { return new JsonInt (i);      }
	public static JsonShort  create(short s)    { return new JsonShort (s);    }
	public static JsonByte   create(byte b)     { return new JsonByte (b);     }
	public static JsonChar   create(char c)     { return new JsonChar (c);     }
	public static JsonDouble create(double d)   { return new JsonDouble (d);   }
	public static JsonFloat  create(float f)    { return new JsonFloat (f);    }
	public static JsonBool   create(boolean b)  { return new JsonBool (b);     }

	public static JsonArray create(Object... objs) { return createArray(objs); }
	public static JsonArray createArray(Object... objs) {
		JsonArray ar = new JsonArray ();
		for (Object i : objs) {
			ar.add(i);
		}
		return ar;
	}

	public static JsonObject create(EntryObject... objs) { return createObject(objs); }
	public static JsonObject createObject(EntryObject... objs) {
		
		JsonObject o = new JsonObject ();
		for (EntryObject entry : objs) {
			o.add(entry.key, entry.dom);
		}
		return o;
	}
	
	public static JsonObject create(Map map) {
		JsonObject json = createObject();
		json.setValue(map);
		return json;
	}
	
	public static JsonArray create(List list) {
		JsonArray json = createArray();
		json.setValue(list);
		return json;
	}
	
	public static Json create(Object o) {
		
		if (o == null) {
			return create();
		}
		
		if (o.getClass().isArray()) {
			JsonArray ar = new JsonArray ();
			for (int i = 0; i < Array.getLength(o); i++) {
				ar.add(Array.get(o, i));
			}
			return ar;
		}

		if (o instanceof String     ) { return create ((String)      o); }
		if (o instanceof EntryObject) { return create ((EntryObject) o); }
		if (o instanceof JsonNode)    { return create ((JsonNode)    o); }
		if (o instanceof Json)        { return create ((Json)        o); }
		if (o instanceof Long      || o.getClass().isAssignableFrom(Long.TYPE     )) { return create (((Long)     o).longValue());    }
		if (o instanceof Integer   || o.getClass().isAssignableFrom(Integer.TYPE  )) { return create (((Integer)  o).intValue());     }
		if (o instanceof Short     || o.getClass().isAssignableFrom(Short.TYPE    )) { return create (((Short)    o).shortValue());   }
		if (o instanceof Byte      || o.getClass().isAssignableFrom(Byte.TYPE     )) { return create (((Byte)     o).byteValue());    }
		if (o instanceof Character || o.getClass().isAssignableFrom(Character.TYPE)) { return create (((Character)o).charValue());    }
		if (o instanceof Double    || o.getClass().isAssignableFrom(Double.TYPE   )) { return create (((Double)   o).doubleValue());  }
		if (o instanceof Float     || o.getClass().isAssignableFrom(Float.TYPE    )) { return create (((Float)    o).floatValue());   }
		if (o instanceof Boolean   || o.getClass().isAssignableFrom(Boolean.TYPE  )) { return create (((Boolean)  o).booleanValue()); }
		if (o instanceof Map       || o.getClass().isAssignableFrom(Boolean.TYPE  )) { return create ((Map)o);                        }
		if (o instanceof List      || o.getClass().isAssignableFrom(Boolean.TYPE  )) { return create ((List)o);                       }
		
		return create(o.toString());
	}
	
	public Object  value ()      { return this.value;  }
	public String  strValue()    { return (value() != null) ? value().toString () : "";    }
	public long    longValue()   { try { return Long.parseLong       (this.strValue()); } catch (Exception e) {} return 0; }
	public int     intValue()    { return new Long     (longValue()) .intValue()  ; }
	public short   shortValue()  { return new Integer  (intValue())  .shortValue(); }
	public byte    byteValue()   { return new Short    (shortValue()).byteValue() ; }
	public char    charValue()   { return (char)(shortValue() & 0x00FF); }
	public float   floatValue()  { try { return Float.parseFloat     (this.strValue()); } catch (Exception e) {} return 0; }
	public double  doubleValue() { try { return Double.parseDouble   (this.strValue()); } catch (Exception e) {} return 0; }
	public boolean boolValue()   { try { return Boolean.parseBoolean (this.strValue()); } catch (Exception e) {} return false; }
	
	public int size() { return 0; }

	public Json child (int  i)      { return create(); }
	public Json child (String  key) { return create(); }
	
	public Collection<Json> allChild () { return new ArrayList<Json>(); }
	public Set<Entry<String, Json>> allChildWithKey ()  { return new HashSet<Entry<String, Json>>(); }
	
	public boolean contains (Json json)      { return false; };
	public boolean containsKey (int i)      { return false; };
	public boolean containsKey (String key) { return false; };
	
	public void add(Json child) {}
	public void add(Object child) {
		this.add(create (child));
	}
	
	public void add(String key, Json child) {}
	public void add(String key, Object child) {
		this.add (key, create (child));
	}
	
	public boolean equals (Object obj) {
		if (obj instanceof Json) { 
			if (((Json)obj).value() == null || this.value() == null) {
				return this.value() == ((Json)obj).value();
			}
			return this.value().equals(((Json)obj).value());
		}
		return false;
	}

	public Json addComplement(IJsonComplement jsonComplement) {
		this.complements.add(jsonComplement);
		return this;
	}

	private Json addComplements(ArrayList<IJsonComplement> complements) {
		this.complements.addAll(complements);
		return this;
	}
	
	public ArrayList<IJsonComplement> getComplements() {
		return this.complements;
	}

	public void clear() {
	}
	
	public IJsonComplement getComplement(Class search) {
		for (IJsonComplement complement: this.complements) {
			if (search.isAssignableFrom(complement.getClass())) {
				return complement;
			}
		}
		return null;
	}
	
	public TYPE getType () {
		return TYPE.NULL;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isNull   () { return this.getType() == TYPE.NULL;   }
	public boolean isArray  () { return this.getType() == TYPE.ARRAY;   }
	public boolean isObject () { return this.getType() == TYPE.OBJECT;  }
	public boolean isString () { return this.getType() == TYPE.STRING;  }
	public boolean isLong   () { return this.getType() == TYPE.LONG;    }
	public boolean isInt    () { return this.getType() == TYPE.INT;     }
	public boolean isShort  () { return this.getType() == TYPE.SHORT;   }
	public boolean isByte   () { return this.getType() == TYPE.BYTE;    }
	public boolean isChar   () { return this.getType() == TYPE.CHAR;    }
	public boolean isDouble () { return this.getType() == TYPE.DOUBLE;  }
	public boolean isFloat  () { return this.getType() == TYPE.FLOAT;   }
	public boolean isBool   () { return this.getType() == TYPE.BOOLEAN; }
	
	public Object clone () {
		return create (this);
		
	}
	
	public String toString() {
		IJsonObjectDisplay display = (IJsonObjectDisplay)this.getComplement(IJsonObjectDisplay.class);
		if (display != null) {
			return display.display(this);
		}
		return toString(false);
	}
	
	public String toString (boolean pretty) {
		JsonRootNode json = JsonNodeFactories.object(
			JsonNodeFactories.field("root", this.argoJson().build())
		);
		String out;
		if (pretty) {
			out = new PrettyJsonFormatter().format(json);
		} else {
			out = new CompactJsonFormatter().format(json);
		}
		out = out.substring(1).trim().substring("\"root\":".length()).trim();
		out = out.substring(0, out.length() - ("}".length()));
		
		return out;
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public static Json create (JsonNode json) {
		
		if (json.isObjectNode()) {
			JsonObject o = createObject();
			for (Entry<JsonStringNode, JsonNode> entry : json.getFields().entrySet()) {
				o.add(entry.getKey().getText(), entry.getValue());
			}
			return o;
		}
		if (json.isArrayNode()) {
			JsonArray ar = createArray ();
			for (JsonNode child: json.getElements()) {
				ar.add(child);
			}
			return ar;
		}
		if (json.isStringValue())  { return create (json.getStringValue());  }
		if (json.isNumberValue())  { return create (json.getNumberValue());  }
		if (json.isBooleanValue()) { return create (json.getBooleanValue()); }
		
		return create();
	}
	
	public JsonNodeBuilder argoJson() {
		return JsonNodeBuilders.aNullBuilder();
	}
	
}
