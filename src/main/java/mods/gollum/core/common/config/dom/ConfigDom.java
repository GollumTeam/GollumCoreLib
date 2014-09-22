package mods.gollum.core.common.config.dom;

import java.lang.reflect.Array;
import java.util.Map.Entry;

import com.google.gson.JsonNull;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonStringNode;
import mods.gollum.core.common.config.type.ItemStackConfigType;

public class ConfigDom {
	
	public Object value;
	
	public static class EntryObject {
		
		public String key;
		public ConfigDom dom;

		public EntryObject(String key, Object value) {
			this.key = key;
			this.dom = create(value);
		}
	}

	public static NullConfigDom create() {
		return new NullConfigDom();
	}
	
	public static ConfigDom create(ConfigDom cd) { return create(cd.value());}
	
	public static StringConfigDom create(String str) { return new StringConfigDom (str); }
	public static LongConfigDom   create(long l)     { return new LongConfigDom (l);     }
	public static IntConfigDom    create(int i)      { return new IntConfigDom (i);      }
	public static ShortConfigDom  create(short s)    { return new ShortConfigDom (s);    }
	public static ByteConfigDom   create(byte b)     { return new ByteConfigDom (b);     }	
	public static DoubleConfigDom create(double d)   { return new DoubleConfigDom (d);   }
	public static FloatConfigDom  create(float f)    { return new FloatConfigDom (f);    }
	public static BoolConfigDom   create(boolean b)  { return new BoolConfigDom (b);     }

	public static ArrayConfigDom create(Object... objs) { return createArray(objs); }
	public static ArrayConfigDom createArray(Object... objs) {
		ArrayConfigDom ar = new ArrayConfigDom ();
		for (Object i : objs) {
			ar.add(i);
		}
		return ar;
	}

	public static ObjectConfigDom create(EntryObject... objs) { return createObject(objs); }
	public static ObjectConfigDom createObject(EntryObject... objs) {
		
		ObjectConfigDom o = new ObjectConfigDom ();
		for (EntryObject entry : objs) {
			o.add(entry.key, entry.dom);
		}
		return o;
	}
	
	public static ConfigDom create(Object o) {
		
		if (o.getClass().isArray()) {
			ArrayConfigDom ar = new ArrayConfigDom ();
			for (int i = 0; i < Array.getLength(o); i++) {
				ar.add(Array.get(o, i));
			}
			return ar;
		}

		if (o instanceof String     ) { return create ((String)       o); }
		if (o instanceof EntryObject) { return create ((EntryObject)  o); }
		if (o instanceof JsonNode)    { return create ((JsonNode)     o); }
		if (o instanceof Long    || o.getClass().isAssignableFrom(Long.TYPE   ) ) { return create (((Long)   o).longValue());    }
		if (o instanceof Integer || o.getClass().isAssignableFrom(Integer.TYPE) ) { return create (((Integer)o).intValue());     }
		if (o instanceof Short   || o.getClass().isAssignableFrom(Short.TYPE  ) ) { return create (((Short)  o).shortValue());   }
		if (o instanceof Byte    || o.getClass().isAssignableFrom(Byte.TYPE   ) ) { return create (((Byte)   o).byteValue());    }
		if (o instanceof Double  || o.getClass().isAssignableFrom(Double.TYPE ) ) { return create (((Double) o).doubleValue());  }
		if (o instanceof Float   || o.getClass().isAssignableFrom(Float.TYPE  ) ) { return create (((Float)  o).floatValue());   }
		if (o instanceof Boolean || o.getClass().isAssignableFrom(Boolean.TYPE) ) { return create (((Boolean)o).booleanValue()); }
		
		return create(o.toString());
	}
	
	public Object  value ()      { return this.value;  }
	public String  strValue()    { return (value() != null) ? value().toString () : "";    }
	public long    longValue()   { try { return Long.parseLong       (this.strValue()); } catch (Exception e) {} return 0; }
	public int     intValue()    { return new Long    (longValue()) .intValue()  ; }
	public short   shortValue()  { return new Integer (intValue())  .shortValue(); }
	public byte    byteValue()   { return new Short   (shortValue()).byteValue() ; }
	public float   floatValue()  { try { return Float.parseFloat     (this.strValue()); } catch (Exception e) {} return 0; }
	public double  doubleValue() { try { return Double.parseDouble   (this.strValue()); } catch (Exception e) {} return 0; }
	public boolean boolValue()   { try { return Boolean.parseBoolean (this.strValue()); } catch (Exception e) {} return false; }
	
	public int size() { return 0; }

	public ConfigDom child (int  i)      { return create(); }
	public ConfigDom child (String  key) { return create(); }

	public void add(ConfigDom child) {}
	public void add(Object child) {
		this.add(create (child));
	}
	
	public void add(String key, ConfigDom child) {}
	public void add(String key, Object child) {
		this.add (key, create (child));
	}
	
	public boolean equals (ConfigDom obj) {
		return this.value() == obj.value();
	}
	
	/////////////////////
	// Convert to json //
	/////////////////////
	
	public static ConfigDom create (JsonNode json) {
		
		if (json.isObjectNode()) {
			ObjectConfigDom o = createObject();
			for (Entry<JsonStringNode, JsonNode> entry : json.getFields().entrySet()) {
				o.add(entry.getKey().getText(), entry.getValue());
			}
			return o;
		}
		if (json.isArrayNode()) {
			ArrayConfigDom ar = createArray ();
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
