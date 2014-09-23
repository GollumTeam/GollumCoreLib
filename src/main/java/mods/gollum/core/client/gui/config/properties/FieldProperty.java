package mods.gollum.core.client.gui.config.properties;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import mods.gollum.core.client.gui.config.CustomElement;
import mods.gollum.core.client.gui.config.entries.JsonEntry;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.config.IConfigElement;

public class FieldProperty extends GollumProperty {
	
	private ConfigLoad configLoad;
	private Field f;
	
	
	public FieldProperty(Field f, ConfigLoad configLoad, String currentCategory) throws Exception {
		super (configLoad.mod, getType(f));
		
		this.configLoad = configLoad;
		this.f = f;
		
		f.setAccessible(true);
		ConfigProp anno = f.getAnnotation(ConfigProp.class);
		
		if (anno.group().equals (currentCategory)) {
			
			if (this.getType() != null && anno != null) {
				
				Object value = f.get(configLoad.config);
				String[] values = null;
				Object valueDefault = f.get(configLoad.configDefault);
				String[] valuesDefault = null;
				
				if (f.getType().isAssignableFrom(Character.TYPE) || f.getType().isAssignableFrom(Character.class)) { // Fixe affichage
					value        = (byte)((Character)value).charValue();
					valueDefault = (byte)((Character)valueDefault).charValue();
				}
				
				if (value.getClass().isArray()) {
					values = new String[Array.getLength(value)];
					for (int i = 0; i < Array.getLength(value); i++) {
						
						Object o = Array.get(value, i);
						if (f.getType().isAssignableFrom(char[].class) || f.getType().isAssignableFrom(Character[].class)) { // Fixe affichage
							o = (byte)((Character)o).charValue();
						}
						values[i] = o.toString();
					}
					valuesDefault = new String[Array.getLength(valueDefault)];
					for (int i = 0; i < Array.getLength(valueDefault); i++) {
						
						Object o = Array.get(valueDefault, i);
						if (f.getType().isAssignableFrom(char[].class) || f.getType().isAssignableFrom(Character[].class)) { // Fixe affichage
							o = (byte)((Character)o).charValue();
						}
						valuesDefault[i] = o.toString();
					}
				}
				
				this.init(f.getName(), anno, value, values, valueDefault, valuesDefault);
				
				// Limit short byte char
				if (f.getType().isAssignableFrom(Short.TYPE) || f.getType().isAssignableFrom(Short.class)) {
					this.setLimitShort();
				}
				if (f.getType().isAssignableFrom(Byte.TYPE) || f.getType().isAssignableFrom(Byte.class)) {
					this.setLimitByte();
				}
				if (f.getType().isAssignableFrom(Character.TYPE) || f.getType().isAssignableFrom(Character.class)) {
					this.setLimitChar();
				}
				
				this.markUnchange();
				
				this.isValid = true;
				this.isNative = true;
			}
			
			if (IConfigJsonType.class.isAssignableFrom(f.getType())) {
				this.isValid = true;
				this.isNative = false;
			}
		}
		
	}
	
	private static Type getType(Field f) {
		
		Type type = null;
		
		if (
			f.getType().isAssignableFrom(String.class) ||
			f.getType().isAssignableFrom(String[].class)
		) {
			type = Property.Type.STRING;
		}
		if (
			f.getType().isAssignableFrom(Long.TYPE        ) ||
			f.getType().isAssignableFrom(Integer.TYPE     ) ||
			f.getType().isAssignableFrom(Short.TYPE       ) ||
			f.getType().isAssignableFrom(Byte.TYPE        ) ||
			f.getType().isAssignableFrom(Character.TYPE   ) ||
			f.getType().isAssignableFrom(Long.class       ) ||
			f.getType().isAssignableFrom(Integer.class    ) ||
			f.getType().isAssignableFrom(Short.class      ) ||
			f.getType().isAssignableFrom(Byte.class       ) ||
			f.getType().isAssignableFrom(Character.class  ) ||
			
			f.getType().isAssignableFrom(long[].class     ) ||
			f.getType().isAssignableFrom(int[].class      ) ||
			f.getType().isAssignableFrom(short[].class    ) ||
			f.getType().isAssignableFrom(byte[].class     ) ||
			f.getType().isAssignableFrom(char[].class     ) ||
			f.getType().isAssignableFrom(Long[].class     ) ||
			f.getType().isAssignableFrom(Integer[].class  ) ||
			f.getType().isAssignableFrom(Short[].class    ) ||
			f.getType().isAssignableFrom(Byte[].class     ) ||
			f.getType().isAssignableFrom(Character[].class)
		) {
			type = Property.Type.INTEGER;
		}
		if (
			f.getType().isAssignableFrom(Float.TYPE    ) ||
			f.getType().isAssignableFrom(Double.TYPE   ) ||
			f.getType().isAssignableFrom(Float.class   ) ||
			f.getType().isAssignableFrom(Double.class  ) ||
			
			f.getType().isAssignableFrom(float[].class ) ||
			f.getType().isAssignableFrom(double[].class) ||
			f.getType().isAssignableFrom(Float[].class ) ||
			f.getType().isAssignableFrom(Double[].class) 
		) {
			type = Property.Type.DOUBLE;
		}
		if (
			f.getType().isAssignableFrom(Boolean.TYPE   ) ||
			f.getType().isAssignableFrom(Boolean.class  ) ||
			
			f.getType().isAssignableFrom(boolean[].class) ||
			f.getType().isAssignableFrom(Boolean[].class)
		) {
			type = Property.Type.BOOLEAN;
		}
		return type;
	}

	@Override
	public IConfigElement createCustomConfigElement() {
		try {
			if (f.get(this.configLoad.config) instanceof IConfigJsonType) {
				Json value = ((IConfigJsonType)f.get(this.configLoad.config)).writeConfig();
				Json defaultValue = ((IConfigJsonType)f.get(this.configLoad.config)).writeConfig();
				return new CustomElement(JsonEntry.class, f.getName(), this.mod.i18n().trans("config."+f.getName()), value, defaultValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
