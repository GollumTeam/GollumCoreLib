package mods.gollum.core.client.gui.config.properties;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import mods.gollum.core.client.gui.config.element.CustomElement;
import mods.gollum.core.client.gui.config.entry.BlockEntry;
import mods.gollum.core.client.gui.config.entry.GollumCategoryEntry;
import mods.gollum.core.client.gui.config.entry.ItemEntry;
import mods.gollum.core.client.gui.config.entry.JsonEntry;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.ConfigProp.Type;
import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.common.mod.GollumMod;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import cpw.mods.fml.client.config.IConfigElement;

public abstract class GollumProperty extends Property {

	protected boolean isValid     = false;
	protected boolean isNative = false;
	public GollumMod mod;
	protected ConfigProp anno;
	private Type type;
	
	public GollumProperty(GollumMod mod, Type type) {
		super ("", "", type);
		this.mod = mod;
		this.type = type;
	}
	
	protected static Type getType(Class clazz) {
		
		Type type = null;
		
		if (
			clazz.isAssignableFrom(String.class) ||
			clazz.isAssignableFrom(String[].class)
		) {
			type = Property.Type.STRING;
		}
		if (
			clazz.isAssignableFrom(Long.TYPE        ) ||
			clazz.isAssignableFrom(Integer.TYPE     ) ||
			clazz.isAssignableFrom(Short.TYPE       ) ||
			clazz.isAssignableFrom(Byte.TYPE        ) ||
			clazz.isAssignableFrom(Character.TYPE   ) ||
			clazz.isAssignableFrom(Long.class       ) ||
			clazz.isAssignableFrom(Integer.class    ) ||
			clazz.isAssignableFrom(Short.class      ) ||
			clazz.isAssignableFrom(Byte.class       ) ||
			clazz.isAssignableFrom(Character.class  ) ||
			
			clazz.isAssignableFrom(long[].class     ) ||
			clazz.isAssignableFrom(int[].class      ) ||
			clazz.isAssignableFrom(short[].class    ) ||
			clazz.isAssignableFrom(byte[].class     ) ||
			clazz.isAssignableFrom(char[].class     ) ||
			clazz.isAssignableFrom(Long[].class     ) ||
			clazz.isAssignableFrom(Integer[].class  ) ||
			clazz.isAssignableFrom(Short[].class    ) ||
			clazz.isAssignableFrom(Byte[].class     ) ||
			clazz.isAssignableFrom(Character[].class)
		) {
			type = Property.Type.INTEGER;
		}
		if (
			clazz.isAssignableFrom(Float.TYPE    ) ||
			clazz.isAssignableFrom(Double.TYPE   ) ||
			clazz.isAssignableFrom(Float.class   ) ||
			clazz.isAssignableFrom(Double.class  ) ||
			
			clazz.isAssignableFrom(float[].class ) ||
			clazz.isAssignableFrom(double[].class) ||
			clazz.isAssignableFrom(Float[].class ) ||
			clazz.isAssignableFrom(Double[].class) 
		) {
			type = Property.Type.DOUBLE;
		}
		if (
			clazz.isAssignableFrom(Boolean.TYPE   ) ||
			clazz.isAssignableFrom(Boolean.class  ) ||
			
			clazz.isAssignableFrom(boolean[].class) ||
			clazz.isAssignableFrom(Boolean[].class)
		) {
			type = Property.Type.BOOLEAN;
		}
		return type;
	}
	
	protected void initNative(ConfigProp anno, Class clazz, Object value, Object valueDefault, String name) {
		
		if (this.getType() != null && anno != null) {
			
			String[] values = null;
			String[] valuesDefault = null;
			
			if (clazz.isAssignableFrom(Character.TYPE) || clazz.isAssignableFrom(Character.class)) { // Fixe affichage
				value        = (byte)((Character)value).charValue();
				valueDefault = (byte)((Character)valueDefault).charValue();
			}
			
			if (value.getClass().isArray()) {
				values = new String[Array.getLength(value)];
				for (int i = 0; i < Array.getLength(value); i++) {
					
					Object o = Array.get(value, i);
					if (clazz.isAssignableFrom(char[].class) || clazz.isAssignableFrom(Character[].class)) { // Fixe affichage
						o = (byte)((Character)o).charValue();
					}
					values[i] = o.toString();
				}
				valuesDefault = new String[Array.getLength(valueDefault)];
				for (int i = 0; i < Array.getLength(valueDefault); i++) {
					
					Object o = Array.get(valueDefault, i);
					if (clazz.isAssignableFrom(char[].class) || clazz.isAssignableFrom(Character[].class)) { // Fixe affichage
						o = (byte)((Character)o).charValue();
					}
					valuesDefault[i] = o.toString();
				}
			}
			
			this.initNativeDatas(name, anno, value, values, valueDefault, valuesDefault);
			
			// Limit short byte char
			if (clazz.isAssignableFrom(Short.TYPE) || clazz.isAssignableFrom(Short.class)) {
				this.setLimitShort();
			}
			if (clazz.isAssignableFrom(Byte.TYPE) || clazz.isAssignableFrom(Byte.class)) {
				this.setLimitByte();
			}
			if (clazz.isAssignableFrom(Character.TYPE) || clazz.isAssignableFrom(Character.class)) {
				this.setLimitChar();
			}
			
			this.markUnchange();
			
			this.isValid = true;
			this.isNative = true;
			
		}
	}
	
	protected void initNativeDatas (String name, ConfigProp anno, Object value, String[] values, Object valueDefault, String[] valuesDefault) {
		
		this.anno = anno;
		
		if (anno.type() == ConfigProp.Type.MOD) {
			this.type = Type.MOD_ID; 
		}
		this.setName(name);
		this.comment = anno.info();
		this
			.setLanguageKey(this.mod.i18n().trans("config."+name))
			.setRequiresMcRestart(anno.mcRestart())
			.setRequiresWorldRestart(anno.worldRestart())
			.setIsListLengthFixed(anno.isListLengthFixed ())
		;
		
		if (values != null) {
			this.setValue("");
			this.setDefaultValue("");
			this.setValues(values);
			this.setDefaultValues(valuesDefault);
			
			try {
				Field fIsList = Property.class.getDeclaredField("isList");
				fIsList.setAccessible(true);
				fIsList.set(this, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.setValues(new String[0]);
			this.setDefaultValues(new String[0]);
			this.setValue(value+"");
			this.setDefaultValue(valueDefault+"");

			try {
				Field fIsList = Property.class.getDeclaredField("isList");
				fIsList.setAccessible(true);
				fIsList.set(this, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (!anno.minValue ().equals("")) {
			try {
				this.setMinValue(Integer.parseInt(anno.minValue ()));
			} catch (Exception e) {
				try { this.setMinValue(Double.parseDouble(anno.minValue())); } catch (Exception e2) {}
			}
		}
		
		if (!anno.maxValue ().equals("")) {
			try {
				this.setMaxValue(Integer.parseInt(anno.maxValue ()));
			} catch (Exception e) {
				try { this.setMaxValue(Double.parseDouble(anno.maxValue())); } catch (Exception e2) {}
			}
		}
		
		if (!anno.maxListLength ().equals("")) {
			try { this.setMaxListLength(Integer.parseInt(anno.maxListLength())); } catch (Exception e) {}
		}
		if (!anno.pattern ().equals("")) {
			try { this.setValidationPattern(Pattern.compile(anno.pattern())); } catch (Exception e) {}
		}
		if (
			anno.validValues ().length > 1 ||
			(
				anno.validValues ().length == 1 && 
				!anno.validValues ()[0].equals("")
			)
		) {
			this.setValidValues(anno.validValues());
		}
		
	}
	
	public void setLimitShort () {
		if (Long.parseLong(this.getMinValue()) < -32768L) {
			this.setMinValue(-32768);
		}
		if (Long.parseLong(this.getMaxValue()) > 32767) {
			this.setMaxValue(32767);
		}
	}
	public void setLimitByte () {
		if (Long.parseLong(this.getMinValue()) < -128L) {
			this.setMinValue(-128);
		}
		if (Long.parseLong(this.getMaxValue()) > 127L) {
			this.setMaxValue(127);
		}
	}
	public void setLimitChar () {
		if (Long.parseLong(this.getMinValue()) < 0) {
			this.setMinValue(0);
		}
		if (Long.parseLong(this.getMaxValue()) > 15L) {
			this.setMaxValue(15);
		}
	}
	
	public String getDefault() {
		return (!super.getDefault().equals("")) ? super.getDefault() : getNullValue()+"";
	}
	
	private Object getNullValue() {
		if (type == Type.BOOLEAN) { return false; }
		if (type == Type.DOUBLE)  { return 0; }
		if (type == Type.INTEGER) { return 0; }
		return "";
	}
	
	public Type getType() {
		return this.type;
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public void markUnchange () {
		try {
			Field fChanged = Property.class.getDeclaredField("changed");
			fChanged.setAccessible(true);
			fChanged.set(this, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isNative() {
		return isNative;
	}
	
	public IConfigElement createConfigElement() {
		if (this.isValid()) {
			if (this.isNative) {
				
				if (this.anno.type() == ConfigProp.Type.ITEM) {
					return new CustomElement(ItemEntry.class, this);
				} else
				
				if (this.anno.type() == ConfigProp.Type.BLOCK) {
					return new CustomElement(BlockEntry.class, this);
				}  else 
				
				if (this.anno.type() == ConfigProp.Type.SLIDER) {
					return new CustomElement(NumberSliderEntry.class, this);
				} else 
				
				{
					return new ConfigElement(this);
				}
			} else {
				return this.createCustomConfigElement();
			}
		}
		return null;
	}
	
	public abstract IConfigElement createCustomConfigElement ();
}
