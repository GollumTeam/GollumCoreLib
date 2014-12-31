package mods.gollum.core.client.gui.config.element;

import mods.gollum.core.client.gui.config.entry.ArrayEntry;
import mods.gollum.core.client.gui.config.entry.BooleanEntry;
import mods.gollum.core.client.gui.config.entry.ByteEntry;
import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.client.gui.config.entry.DoubleEntry;
import mods.gollum.core.client.gui.config.entry.FloatEntry;
import mods.gollum.core.client.gui.config.entry.IntegerEntry;
import mods.gollum.core.client.gui.config.entry.LongEntry;
import mods.gollum.core.client.gui.config.entry.ShortEntry;
import mods.gollum.core.client.gui.config.entry.StringEntry;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.ConfigProp;

public abstract class ConfigElement {
	
	private String name;
	protected Object value = null;
	protected Object defaultValue = null;
	
	public ConfigElement (String name) {
		this.name = name;
	}
	
	public abstract ConfigProp getConfigProp();
	
	public Class< ? extends ConfigEntry> getEntryClass() {
		return this.getEntryClass(this.getType());
	}
	
	private Class< ? extends ConfigEntry> getEntryClass(Class clazz) {
		
//		if (this.anno.type() == ConfigProp.Type.ITEM)      { return ItemEntry          .class; }
//		if (this.anno.type() == ConfigProp.Type.BLOCK)     { return BlockEntry         .class; }
////		if (this.anno.type() == ConfigProp.Type.SLIDER)    { return SliderEntry        .class; }
//		if (Json           .class.isAssignableFrom(clazz)) { return JsonEntry          .class; }
//		if (IConfigJsonType.class.isAssignableFrom(clazz)) { return ConfigJsonTypeEntry.class; }
		if (String         .class.isAssignableFrom(clazz)) { return StringEntry        .class; }
		if (Long           .class.isAssignableFrom(clazz) || Long     .TYPE.isAssignableFrom(clazz)) { return LongEntry.class; }
		if (Integer        .class.isAssignableFrom(clazz) || Integer  .TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
		if (Short          .class.isAssignableFrom(clazz) || Short    .TYPE.isAssignableFrom(clazz)) { return ShortEntry.class; }
		if (Byte           .class.isAssignableFrom(clazz) || Byte     .TYPE.isAssignableFrom(clazz)) { return ByteEntry.class; }
		if (Character      .class.isAssignableFrom(clazz) || Character.TYPE.isAssignableFrom(clazz)) { return ByteEntry.class; }
		if (Double         .class.isAssignableFrom(clazz) || Double   .TYPE.isAssignableFrom(clazz)) { return DoubleEntry .class; }
		if (Float          .class.isAssignableFrom(clazz) || Float    .TYPE.isAssignableFrom(clazz)) { return FloatEntry .class; }
		if (Boolean        .class.isAssignableFrom(clazz) || Boolean  .TYPE.isAssignableFrom(clazz)) { return BooleanEntry.class; }
		
		if (clazz.isArray()) {
			return ArrayEntry.class;
		}
		
		return null;
	}
	
	public abstract Class getType ();
	
	public String getName() {
		return this.name;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		Object value = this.value;
		
		try {
			Object cloned = value.getClass().getMethod("clone").invoke(value);
			value = cloned;
		} catch (Exception e) {
		}
		
		return value;
	}

	/**
	 * @return the defaultValue
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}
	
	public Object newValue() {
		
		Class clazz = this.getEntryClass (this.value.getClass().getComponentType());
		String newValue= this.getConfigProp().newValue();
		
		if (clazz == StringEntry.class) {
			return newValue;
		} else
		if (clazz == LongEntry.class) {
			try {
				return Long.parseLong(newValue);
			} catch (Exception e) {
				return (long)0;
			}
		} else
		if (clazz == IntegerEntry.class) {
			try {
				return Integer.parseInt(newValue);
			} catch (Exception e) {
				return (int)0;
			}
		} else
		if (clazz == ShortEntry.class) {
			try {
				return Short.parseShort(newValue);
			} catch (Exception e) {
				return (short)0;
			}
		} else
		if (clazz == ByteEntry.class) {
			try {
				return Byte.parseByte(newValue);
			} catch (Exception e) {
				return (byte)0;
			}
		} else
		if (clazz == DoubleEntry.class) {
			try {
				return Double.parseDouble(newValue);
			} catch (Exception e) {
				return (double)0;
			}
		} else
		if (clazz == FloatEntry.class) {
			try {
				return Float.parseFloat(newValue);
			} catch (Exception e) {
				return (float)0;
			}
		} else
		if (clazz == BooleanEntry.class) {
			try {
				return Boolean.parseBoolean(newValue);
			} catch (Exception e) {
				return false;
			}
		}
		
		return null;
	}

	/**
	 * @param value the value to set
	 */
	public ConfigElement setValue(Object value) {
		this.value = value;
		return this;
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public ConfigElement setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public Object getMin() {

		Object min = null;
		String str = this.getConfigProp().minValue();
		Class clazz = this.getEntryClass();
		
		if (str != null && !str.equals("")) {
			try {
				if (DoubleEntry.class.isAssignableFrom(clazz)) {
					min = Double.parseDouble(str);
				} else {
					min = Long.parseLong(str);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (min == null) {
			if (clazz == DoubleEntry.class) {
				min = (double)Double.MIN_VALUE;
			} else
			if (clazz == FloatEntry.class) {
				min = (double)Float.MIN_VALUE;
			} else
			if (clazz == LongEntry.class) {
				min = (long)Long.MIN_VALUE;
			} else
			if (clazz == IntegerEntry.class) {
				min = (long)Integer.MIN_VALUE;
			} else
			if (clazz == ShortEntry.class) {
				min = (long)Short.MIN_VALUE;
			} else
			if (clazz == ByteEntry.class) {
				min = (long)Byte.MIN_VALUE;
			}
		} else {
			if (clazz == DoubleEntry.class && (Double)min < Double.MIN_VALUE) {
				min = (double)Double.MIN_VALUE;
			} else
			if (clazz == FloatEntry.class && (Double)min < Float.MIN_VALUE) {
				min = (double)Float.MIN_VALUE;
			} else
			if (clazz == LongEntry.class && (Long)min < Long.MIN_VALUE) {
				min = (long)Long.MIN_VALUE;
			} else
			if (clazz == IntegerEntry.class && (Long)min < Integer.MIN_VALUE) {
				min = (long)Integer.MIN_VALUE;
			} else
			if (clazz == ShortEntry.class && (Long)min < Short.MIN_VALUE) {
				min = (long)Short.MIN_VALUE;
			} else
			if (clazz == ByteEntry.class && (Long)min < Byte.MIN_VALUE) {
				min = (long)Byte.MIN_VALUE;
			}
		}
		
		return min;
	}

	public Object getMax() {
		
		Object max = null;
		String str = this.getConfigProp().maxValue();
		Class clazz = this.getEntryClass();
		
		if (str != null && !str.equals("")) {
			try {
				if (DoubleEntry.class.isAssignableFrom(clazz)) {
					max = Double.parseDouble(str);
				} else {
					max = Long.parseLong(str);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (max == null) {
			if (clazz == DoubleEntry.class) {
				max = (double)Double.MAX_VALUE;
			} else
			if (clazz == FloatEntry.class) {
				max = (double)Float.MAX_VALUE;
			} else
			if (clazz == LongEntry.class) {
				max = Long.MAX_VALUE;
			} else
			if (clazz == IntegerEntry.class) {
				max = (long)Integer.MAX_VALUE;
			} else
			if (clazz == ShortEntry.class) {
				max = (long)Short.MAX_VALUE;
			} else
			if (clazz == ByteEntry.class) {
				max = (long)Byte.MAX_VALUE;
			}
		} else {
			if (clazz == DoubleEntry.class && (Double)max > Double.MAX_VALUE) {
				max = (double)Double.MAX_VALUE;
			} else
			if (clazz == FloatEntry.class && (Double)max > Float.MAX_VALUE) {
				max = (double)Float.MAX_VALUE;
			} else
			if (clazz == LongEntry.class && (Long)max > Long.MAX_VALUE) {
				max = (long)Long.MAX_VALUE;
			} else
			if (clazz == IntegerEntry.class && (Long)max > Integer.MAX_VALUE) {
				max = (long)Integer.MAX_VALUE;
			} else
			if (clazz == ShortEntry.class && (Long)max > Short.MAX_VALUE) {
				max = (long)Short.MAX_VALUE;
			} else
			if (clazz == ByteEntry.class && (Long)max > Byte.MAX_VALUE) {
				max = (long)Byte.MAX_VALUE;
			}
		}
		
		return max;
	}

}
