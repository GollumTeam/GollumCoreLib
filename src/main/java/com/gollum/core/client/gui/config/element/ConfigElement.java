package com.gollum.core.client.gui.config.element;

import java.util.Set;

import com.gollum.core.client.gui.config.entry.ArrayEntry;
import com.gollum.core.client.gui.config.entry.BiomeEntry;
import com.gollum.core.client.gui.config.entry.BiomeIdEntry;
import com.gollum.core.client.gui.config.entry.BlockEntry;
import com.gollum.core.client.gui.config.entry.BooleanEntry;
import com.gollum.core.client.gui.config.entry.ByteEntry;
import com.gollum.core.client.gui.config.entry.ConfigEntry;
import com.gollum.core.client.gui.config.entry.ConfigJsonTypeEntry;
import com.gollum.core.client.gui.config.entry.DoubleEntry;
import com.gollum.core.client.gui.config.entry.FloatEntry;
import com.gollum.core.client.gui.config.entry.IntegerEntry;
import com.gollum.core.client.gui.config.entry.ItemEntry;
import com.gollum.core.client.gui.config.entry.JsonEntry;
import com.gollum.core.client.gui.config.entry.JsonObjectEntry;
import com.gollum.core.client.gui.config.entry.ListEntry;
import com.gollum.core.client.gui.config.entry.ListInlineEntry;
import com.gollum.core.client.gui.config.entry.LongEntry;
import com.gollum.core.client.gui.config.entry.ModIdEntry;
import com.gollum.core.client.gui.config.entry.ShortEntry;
import com.gollum.core.client.gui.config.entry.SliderEntry;
import com.gollum.core.client.gui.config.entry.StringEntry;
import com.gollum.core.common.config.ConfigProp;
import com.gollum.core.common.config.JsonConfigProp;
import com.gollum.core.common.config.type.ConfigJsonType;
import com.gollum.core.tools.simplejson.Json;

public abstract class ConfigElement {
	
	private String name;
	protected Object value = null;
	protected Object defaultValue = null;
	
	public static final double DOUBLE_MAX = 9999999.0D;
	public static final double DOUBLE_MIN = -9999999.0D;
	public static final float  FLOAT_MAX  = 99999.0F;
	public static final float  FLOAT_MIN  = -99999.0F;
	
	public ConfigElement (String name) {
		this.name = name;
	}
	
	public abstract ConfigProp getConfigProp();
	
	public Class< ? extends ConfigEntry> getEntryClass() {
		return this.getEntryClass(this.getType());
	}
	
	private Class< ? extends ConfigEntry> getEntryClass(Class clazz) {
		
		ConfigProp prop = this.getConfigProp();
		
		if (!prop.entryClass().equals("")) {
			try {
				return (Class<? extends ConfigEntry>) Class.forName(prop.entryClass());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (clazz == null) {
			return null;
		}

		if (Set           .class.isAssignableFrom(clazz)) { return JsonObjectEntry.class;     }
		if (Json          .class.isAssignableFrom(clazz)) { return JsonEntry.class;           }
		if (ConfigJsonType.class.isAssignableFrom(clazz)) { return ConfigJsonTypeEntry.class; }
		if (String.class.isAssignableFrom(clazz)) {
			if (prop.type() == ConfigProp.Type.LIST_INLINE) { return ListInlineEntry.class; }
			if (
				prop.validValues() != null && 
				(
					prop.validValues().length > 1 ||
					(prop.validValues().length == 1 && !prop.validValues()[0].equals(""))
				)
			) {
				return ListEntry.class;
			}
			if (prop.type() == ConfigProp.Type.MOD)   { return ModIdEntry.class; }
			if (prop.type() == ConfigProp.Type.ITEM)  { return ItemEntry .class; }
			if (prop.type() == ConfigProp.Type.BLOCK) { return BlockEntry.class; }
			if (prop.type() == ConfigProp.Type.BIOME) { return BiomeEntry.class; }
			
			return StringEntry.class;
		}
		
		if (
			Long   .class.isAssignableFrom(clazz) || Long   .TYPE.isAssignableFrom(clazz) ||
			Integer.class.isAssignableFrom(clazz) || Integer.TYPE.isAssignableFrom(clazz) ||
			Short  .class.isAssignableFrom(clazz) || Short  .TYPE.isAssignableFrom(clazz) ||
			Byte   .class.isAssignableFrom(clazz) || Byte   .TYPE.isAssignableFrom(clazz) ||
			Double .class.isAssignableFrom(clazz) || Double .TYPE.isAssignableFrom(clazz) ||
			Float  .class.isAssignableFrom(clazz) || Float  .TYPE.isAssignableFrom(clazz)
		) {
			if (prop.type() == ConfigProp.Type.SLIDER) {
				return SliderEntry.class;
			}
			if (prop.type() == ConfigProp.Type.BIOME) { return BiomeIdEntry.class; }
			if (Byte           .class.isAssignableFrom(clazz) || Byte   .TYPE.isAssignableFrom(clazz)) { return ByteEntry   .class; }
			if (Short          .class.isAssignableFrom(clazz) || Short  .TYPE.isAssignableFrom(clazz)) { return ShortEntry  .class; }
			if (Integer        .class.isAssignableFrom(clazz) || Integer.TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
			if (Long           .class.isAssignableFrom(clazz) || Long   .TYPE.isAssignableFrom(clazz)) { return LongEntry   .class; }
			if (Float          .class.isAssignableFrom(clazz) || Float  .TYPE.isAssignableFrom(clazz)) { return FloatEntry  .class; }
			if (Double         .class.isAssignableFrom(clazz) || Double .TYPE.isAssignableFrom(clazz)) { return DoubleEntry .class; }
		}
		if (Boolean.class.isAssignableFrom(clazz) || Boolean  .TYPE.isAssignableFrom(clazz)) { return BooleanEntry.class; }
		
		if (clazz.isArray()) {
			return ArrayEntry.class;
		}
		
		return null;
	}
	
	public Class getType () {
		return null;
	}
	
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
		
		Class  componentType = this.value.getClass().getComponentType();
		Class  clazz         = this.getEntryClass (componentType);
		String newValue      = this.getConfigProp().newValue();
		
		if (clazz == StringEntry.class) {
			return newValue;
		} else
		if (!newValue.equals("") && clazz != null && ListEntry.class.isAssignableFrom(clazz)) {
			return newValue;
		}  else
		if (clazz == ConfigJsonTypeEntry.class) {
			try {
				return componentType.newInstance();
			} catch (Exception e) {
				return null;
			}
		} else
		if (clazz == JsonEntry.class) {
			
			if (this.getConfigProp() instanceof JsonConfigProp) {
				return ((JsonConfigProp) this.getConfigProp()).newJsonValue();
			}
			
		} else
		if (clazz == SliderEntry.class) {
			
			try {
				if (Byte   .class.isAssignableFrom(componentType) || Byte   .TYPE.isAssignableFrom(componentType)) { return Byte   .parseByte   (newValue); }
				if (Short  .class.isAssignableFrom(componentType) || Short  .TYPE.isAssignableFrom(componentType)) { return Short  .parseShort  (newValue); }
				if (Integer.class.isAssignableFrom(componentType) || Integer.TYPE.isAssignableFrom(componentType)) { return Integer.parseInt    (newValue); }
				if (Long   .class.isAssignableFrom(componentType) || Long   .TYPE.isAssignableFrom(componentType)) { return Long   .parseLong   (newValue); }
				if (Float  .class.isAssignableFrom(componentType) || Float  .TYPE.isAssignableFrom(componentType)) { return Float  .parseFloat  (newValue); }
				if (Double .class.isAssignableFrom(componentType) || Double .TYPE.isAssignableFrom(componentType)) { return Double .parseDouble (newValue); }
			} catch (Exception e) {
				if (Byte   .class.isAssignableFrom(componentType) || Byte   .TYPE.isAssignableFrom(componentType)) { return (byte)0; }
				if (Short  .class.isAssignableFrom(componentType) || Short  .TYPE.isAssignableFrom(componentType)) { return (short)0; }
				if (Integer.class.isAssignableFrom(componentType) || Integer.TYPE.isAssignableFrom(componentType)) { return (int)0; }
				if (Long   .class.isAssignableFrom(componentType) || Long   .TYPE.isAssignableFrom(componentType)) { return (long)0; }
				if (Float  .class.isAssignableFrom(componentType) || Float  .TYPE.isAssignableFrom(componentType)) { return (double)0; }
				if (Double .class.isAssignableFrom(componentType) || Double .TYPE.isAssignableFrom(componentType)) { return (float)0; }
			}
		}  else
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
				if (
					DoubleEntry.class.isAssignableFrom(clazz) ||
					SliderEntry.class.isAssignableFrom(clazz)
				) {
					min = Double.parseDouble(str);
				} else {
					min = Long.parseLong(str);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (min == null) {
			
			if (clazz == SliderEntry.class) {
				Class clazz2 = this.getValue().getClass();
				if (Double.class.isAssignableFrom(clazz2) || Double.TYPE.isAssignableFrom(clazz2)) {
					min = this.DOUBLE_MIN;
				} else
				if (Float.class.isAssignableFrom(clazz2) || Float.TYPE.isAssignableFrom(clazz2)) {
					min = new Double (this.FLOAT_MIN);
				} else
				if (Long.class.isAssignableFrom(clazz2) || Long.TYPE.isAssignableFrom(clazz2)) {
					min = this.DOUBLE_MIN;
				} else
				if (Integer.class.isAssignableFrom(clazz2) || Integer.TYPE.isAssignableFrom(clazz2)) {
					min = new Double (this.FLOAT_MIN);
				} else
				if (Short.class.isAssignableFrom(clazz2) || Short.TYPE.isAssignableFrom(clazz2)) {
					min = new Double (Short.MIN_VALUE);
				} else
				if (Byte.class.isAssignableFrom(clazz2) || Byte.TYPE.isAssignableFrom(clazz2)) {
					min = new Double (Byte.MIN_VALUE);
				}
				
			} else
			
			if (clazz == DoubleEntry.class) {
				min = new Double(this.DOUBLE_MIN);
			} else
			if (clazz == FloatEntry.class) {
				min = new Double(this.FLOAT_MIN);
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
			if (clazz == SliderEntry.class) {
				Class clazz2 = this.getValue().getClass();
				if (
					(Double.class.isAssignableFrom(clazz2) || Double.TYPE.isAssignableFrom(clazz2)) &&
					(Double)min < this.DOUBLE_MIN
				) {
					min = this.DOUBLE_MIN;
				} else
				if (
					(Float.class.isAssignableFrom(clazz2) || Float.TYPE.isAssignableFrom(clazz2)) &&
					(Double)min < this.FLOAT_MIN
				) {
					min = new Double(this.FLOAT_MIN);
				} else
				if (
					(Long.class.isAssignableFrom(clazz2) || Long.TYPE.isAssignableFrom(clazz2)) &&
					(Double)min < this.DOUBLE_MIN
				) {
					min = new Double(this.DOUBLE_MIN);
				} else
				if (
					(Integer.class.isAssignableFrom(clazz2) || Integer.TYPE.isAssignableFrom(clazz2)) &&
					(Double)min < this.FLOAT_MIN
				) {
					min = new Double(this.FLOAT_MIN);
				} else
				if (
					(Short.class.isAssignableFrom(clazz2) || Short.TYPE.isAssignableFrom(clazz2) &&
					(Double)min < Short.MIN_VALUE
				)) {
					min = new Double(Short.MIN_VALUE);
				} else
				if (
					(Byte.class.isAssignableFrom(clazz2) || Byte.TYPE.isAssignableFrom(clazz2)) &&
					(Double)min < Byte.MIN_VALUE
					
				) {
					min = new Double(Byte.MIN_VALUE);
				}
				
			} else
			
			if (clazz == DoubleEntry.class && (Double)min < this.DOUBLE_MIN) {
				min = new Double(this.DOUBLE_MIN);
			} else
			if (clazz == FloatEntry.class && (Double)min < this.FLOAT_MIN) {
				min = new Double(this.FLOAT_MIN);
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
			if (clazz == SliderEntry.class) {
				Class clazz2 = this.getValue().getClass();
				if (Double.class.isAssignableFrom(clazz2) || Double.TYPE.isAssignableFrom(clazz2)) {
					max = this.DOUBLE_MAX;
				} else
				if (Float.class.isAssignableFrom(clazz2) || Float.TYPE.isAssignableFrom(clazz2)) {
					max = new Double(this.FLOAT_MAX);
				} else
				if (Long.class.isAssignableFrom(clazz2) || Long.TYPE.isAssignableFrom(clazz2)) {
					max = this.DOUBLE_MAX;
				} else
				if (Integer.class.isAssignableFrom(clazz2) || Integer.TYPE.isAssignableFrom(clazz2)) {
					max = new Double(this.FLOAT_MAX);
				} else
				if (Short.class.isAssignableFrom(clazz2) || Short.TYPE.isAssignableFrom(clazz2)) {
					max = new Double(Short.MAX_VALUE);
				} else
				if (Byte.class.isAssignableFrom(clazz2) || Byte.TYPE.isAssignableFrom(clazz2)) {
					max = new Double(Byte.MAX_VALUE);
				}
				
			} else
			if (clazz == DoubleEntry.class) {
				max = new Double(this.DOUBLE_MAX);
			} else
			if (clazz == FloatEntry.class) {
				max = new Double(this.FLOAT_MAX);
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
			
			if (clazz == SliderEntry.class) {
				Class clazz2 = this.getValue().getClass();
				if (
					(Double.class.isAssignableFrom(clazz2) || Double.TYPE.isAssignableFrom(clazz2)) &&
					(Double)max > this.DOUBLE_MAX
				) {
					max = this.DOUBLE_MAX;
				} else
				if (
					(Float.class.isAssignableFrom(clazz2) || Float.TYPE.isAssignableFrom(clazz2)) &&
					(Double)max > this.FLOAT_MAX
				) {
					max = new Double (this.FLOAT_MAX);
				} else
				if (
					(Long.class.isAssignableFrom(clazz2) || Long.TYPE.isAssignableFrom(clazz2)) &&
					(Long)max > this.DOUBLE_MAX
				) {
					max = this.DOUBLE_MAX;
				} else
				if (
					(Integer.class.isAssignableFrom(clazz2) || Integer.TYPE.isAssignableFrom(clazz2)) &&
					(Long)max > this.FLOAT_MAX
				) {
					max = new Double (Integer.MAX_VALUE);
				} else
				if (
					(Short.class.isAssignableFrom(clazz2) || Short.TYPE.isAssignableFrom(clazz2) &&
					(Long)max > Short.MAX_VALUE
					
				)) {
					max = new Double (Short.MAX_VALUE);
				} else
				if (
					(Byte.class.isAssignableFrom(clazz2) || Byte.TYPE.isAssignableFrom(clazz2)) &&
					(Long)max > Byte.MAX_VALUE
					
				) {
					max = new Double (Byte.MAX_VALUE);
				}
				
			} else
			
			if (clazz == DoubleEntry.class && (Double)max > this.DOUBLE_MAX) {
				max = new Double(this.DOUBLE_MAX);
			} else
			if (clazz == FloatEntry.class && (Double)max > this.FLOAT_MAX) {
				max = new Double(this.FLOAT_MAX);
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
