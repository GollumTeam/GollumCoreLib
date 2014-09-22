package mods.gollum.core.common.config;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import mods.gollum.core.client.gui.config.JsonElement;
import mods.gollum.core.client.gui.config.entries.GollumCategoryEntry;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.IConfigElement;

public class GollumProperty extends Property {

	private boolean isValid     = false;
	private boolean isNative = false;
	private ConfigLoad configLoad;
	private Field f;
	
	
	public GollumProperty(Field f, ConfigLoad configLoad, String currentCategory) throws Exception {
		super ("", "", getType(f));
		
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
				
				if (value.getClass().isArray()) {
					values = new String[Array.getLength(value)];
					for (int i = 0; i < Array.getLength(value); i++) {
						values[i] = Array.get(value, i).toString();
					}
					valuesDefault = new String[Array.getLength(valueDefault)];
					for (int i = 0; i < Array.getLength(valueDefault); i++) {
						valuesDefault[i] = Array.get(valueDefault, i).toString();
					}
				}
				
				this.setName(f.getName());
				this.comment = anno.info();
				this
					.setLanguageKey(configLoad.mod.i18n().trans("config."+f.getName()))
					.setRequiresMcRestart(anno.mcRestart())
					.setRequiresWorldRestart(anno.worldRestart())
					.setIsListLengthFixed(anno.isListLengthFixed ())
				;
				
				if (values != null) {
					this.setValue("");
					this.setDefaultValue("");
					this.setValues(values);
					this.setDefaultValues(valuesDefault);
					
					Field fIsList = Property.class.getDeclaredField("isList");
					fIsList.setAccessible(true);
					fIsList.set(this, true);
					
				} else {
					this.setValues(new String[0]);
					this.setDefaultValues(new String[0]);
					this.setValue(value.toString());
					this.setDefaultValue(valueDefault.toString());
					
					Field fIsList = Property.class.getDeclaredField("isList");
					fIsList.setAccessible(true);
					fIsList.set(this, false);
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
				
				// Limit short float byte
				if (f.getType().isAssignableFrom(Byte.TYPE) || f.getType().isAssignableFrom(Byte.class)) {
					if (Long.parseLong(this.getMinValue()) < -128L) {
						this.setMinValue(-128);
					}
					if (Long.parseLong(this.getMaxValue()) > 127L) {
						this.setMaxValue(127);
					}
				}
				if (f.getType().isAssignableFrom(Short.TYPE) || f.getType().isAssignableFrom(Short.class)) {
					if (Long.parseLong(this.getMinValue()) < -32768L) {
						this.setMinValue(-32768);
					}
					if (Long.parseLong(this.getMaxValue()) > 32767) {
						this.setMaxValue(32767);
					}
				}
				
				Field fChanged = Property.class.getDeclaredField("changed");
				fChanged.setAccessible(true);
				fChanged.set(this, false);
				
				this.isValid = true;
				this.isNative = true;
			}
			
			if (IConfigJsonType.class.isAssignableFrom(f.getType())) {
				this.isValid = true;
				this.isNative = false;
			}
		}
		
	}
	
	public boolean isValid() {
		return isValid;
	}
	
	public boolean isNative() {
		return isNative;
	}
	
	private static Type getType(Field f) {
		
		Type type = null;
		
		// TODO field IConfigClass
		if (
			f.getType().isAssignableFrom(String.class) ||
			f.getType().isAssignableFrom(String[].class)
		) {
			type = Property.Type.STRING;
		}
		if (
			f.getType().isAssignableFrom(Long.TYPE      ) ||
			f.getType().isAssignableFrom(Integer.TYPE   ) ||
			f.getType().isAssignableFrom(Short.TYPE     ) ||
			f.getType().isAssignableFrom(Byte.TYPE      ) ||
			f.getType().isAssignableFrom(Long.class     ) ||
			f.getType().isAssignableFrom(Integer.class  ) ||
			f.getType().isAssignableFrom(Short.class    ) ||
			f.getType().isAssignableFrom(Byte.class     ) ||
			
			f.getType().isAssignableFrom(long[].class   ) ||
			f.getType().isAssignableFrom(int[].class    ) ||
			f.getType().isAssignableFrom(short[].class  ) ||
			f.getType().isAssignableFrom(byte[].class   ) ||
			f.getType().isAssignableFrom(Long[].class   ) ||
			f.getType().isAssignableFrom(Integer[].class) ||
			f.getType().isAssignableFrom(Short[].class  ) ||
			f.getType().isAssignableFrom(Byte[].class   )
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

	public IConfigElement createConfigElement() {
		if (this.isValid()) {
			if (this.isNative) {
				return new ConfigElement(this);
			} else {
				try {
					Json value = ((IConfigJsonType)f.get(this.configLoad.config)).writeConfig();
					Json defaultValue = ((IConfigJsonType)f.get(this.configLoad.config)).writeConfig();
					return new JsonElement(f.getName(), this.configLoad.mod.i18n().trans("config."+f.getName()), value, defaultValue);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
				
		}
		return null;
	}

}
