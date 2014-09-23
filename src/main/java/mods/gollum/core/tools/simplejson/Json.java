package mods.gollum.core.tools.simplejson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.gson.JsonNull;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonStringNode;
import mods.gollum.core.common.config.JsonConfigProp;
import mods.gollum.core.common.config.type.ItemStackConfigType;
import mods.gollum.core.tools.simplejson.Json.EntryObject;

public class Json {
	
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
		FLOAT,
		BOOLEAN
	}
	
	public Object value;
	private ArrayList<JsonComplement> complements = new ArrayList<JsonComplement>();
	
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
		
		public EntryObject addComplement(JsonComplement jsonComplement) {
			this.dom.addComplement(jsonComplement);
			return this;
		}
	}
	
	public static Json create(Json json) { return create(json.value()).addComplements(json.getComplements());}
	
	public static NullJson   create()           { return new NullJson();       }
	public static StringJson create(String str) { return new StringJson (str); }
	public static LongJson   create(long l)     { return new LongJson (l);     }
	public static IntJson    create(int i)      { return new IntJson (i);      }
	public static ShortJson  create(short s)    { return new ShortJson (s);    }
	public static ByteJson   create(byte b)     { return new ByteJson (b);     }
	public static CharJson   create(char c)     { return new CharJson (c);     }
	public static DoubleJson create(double d)   { return new DoubleJson (d);   }
	public static FloatJson  create(float f)    { return new FloatJson (f);    }
	public static BoolJson   create(boolean b)  { return new BoolJson (b);     }

	public static ArrayJson create(Object... objs) { return createArray(objs); }
	public static ArrayJson createArray(Object... objs) {
		ArrayJson ar = new ArrayJson ();
		for (Object i : objs) {
			ar.add(i);
		}
		return ar;
	}

	public static ObjectJson create(EntryObject... objs) { return createObject(objs); }
	public static ObjectJson createObject(EntryObject... objs) {
		
		ObjectJson o = new ObjectJson ();
		for (EntryObject entry : objs) {
			o.add(entry.key, entry.dom);
		}
		return o;
	}
	
	public static Json create(Object o) {
		
		if (o.getClass().isArray()) {
			ArrayJson ar = new ArrayJson ();
			for (int i = 0; i < Array.getLength(o); i++) {
				ar.add(Array.get(o, i));
			}
			return ar;
		}

		if (o instanceof String     ) { return create ((String)       o); }
		if (o instanceof EntryObject) { return create ((EntryObject)  o); }
		if (o instanceof JsonNode)    { return create ((JsonNode)     o); }
		if (o instanceof Long      || o.getClass().isAssignableFrom(Long.TYPE     )) { return create (((Long)     o).longValue());    }
		if (o instanceof Integer   || o.getClass().isAssignableFrom(Integer.TYPE  )) { return create (((Integer)  o).intValue());     }
		if (o instanceof Short     || o.getClass().isAssignableFrom(Short.TYPE    )) { return create (((Short)    o).shortValue());   }
		if (o instanceof Byte      || o.getClass().isAssignableFrom(Byte.TYPE     )) { return create (((Byte)     o).byteValue());    }
		if (o instanceof Character || o.getClass().isAssignableFrom(Character.TYPE)) { return create (((Character)o).charValue());    }
		if (o instanceof Double    || o.getClass().isAssignableFrom(Double.TYPE   )) { return create (((Double)   o).doubleValue());  }
		if (o instanceof Float     || o.getClass().isAssignableFrom(Float.TYPE    )) { return create (((Float)    o).floatValue());   }
		if (o instanceof Boolean   || o.getClass().isAssignableFrom(Boolean.TYPE  )) { return create (((Boolean)  o).booleanValue()); }
		
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

	public void add(Json child) {}
	public void add(Object child) {
		this.add(create (child));
	}
	
	public void add(String key, Json child) {}
	public void add(String key, Object child) {
		this.add (key, create (child));
	}
	
	public boolean equals (Object obj) {
		if (obj instanceof Json) { return this.value().equals(((Json)obj).value());	}
		return false;
	}

	public Json addComplement(JsonComplement jsonComplement) {
		this.complements.add(jsonComplement);
		return this;
	}

	private Json addComplements(ArrayList<JsonComplement> complements) {
		this.complements.addAll(complements);
		return this;
	}
	
	public ArrayList<JsonComplement> getComplements() {
		return this.complements;
	}
	
	public JsonComplement getComplement(Class search) {
		for (JsonComplement complement: this.complements) {
			if (complement.getClass().isAssignableFrom(search)) {
				return complement;
			}
		}
		return null;
	}
	
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public static Json create (JsonNode json) {
		
		if (json.isObjectNode()) {
			ObjectJson o = createObject();
			for (Entry<JsonStringNode, JsonNode> entry : json.getFields().entrySet()) {
				o.add(entry.getKey().getText(), entry.getValue());
			}
			return o;
		}
		if (json.isArrayNode()) {
			ArrayJson ar = createArray ();
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
	
	public JsonNodeBuilder json() {
		return JsonNodeBuilders.aNullBuilder();
	}
	
}
