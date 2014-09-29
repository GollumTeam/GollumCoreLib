package mods.gollum.core.client.gui.config.properties;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import scala.actors.threadpool.Arrays;
import mods.gollum.core.client.gui.config.element.CustomElement;
import mods.gollum.core.client.gui.config.entry.ArrayEntry;
import mods.gollum.core.client.gui.config.entry.BlockEntry;
import mods.gollum.core.client.gui.config.entry.BooleanEntry;
import mods.gollum.core.client.gui.config.entry.ConfigJsonTypeEntry;
import mods.gollum.core.client.gui.config.entry.DoubleEntry;
import mods.gollum.core.client.gui.config.entry.GollumCategoryEntry;
import mods.gollum.core.client.gui.config.entry.IntegerEntry;
import mods.gollum.core.client.gui.config.entry.ItemEntry;
import mods.gollum.core.client.gui.config.entry.JsonEntry;
import mods.gollum.core.client.gui.config.entry.StringEntry;
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
	
	public GollumMod mod;
	protected ConfigProp anno;
	protected Type type;
	protected Class clazz;
	private boolean isValid = false;
	
	public GollumProperty(Class clazz, GollumMod mod, Type type) {
		super ("", "", type);
		this.mod = mod;
		this.type = type;
		this.clazz = clazz;
	}
	
	protected void init(Object value, Object valueDefault, String name) {
		
		if (anno != null) {
			if (this.getClassEntryForClass() != null) {
				
				String[] values = null;
				String[] valuesDefault = null;
				
				if (clazz.isAssignableFrom(Character.TYPE) || clazz.isAssignableFrom(Character.class)) { // Fixe affichage
					value        = (byte)((Character)value)       .charValue();
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
				
				this.initDatas(name, anno, value, values, valueDefault, valuesDefault);
				
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
			}
		}
	}
	
	protected void initDatas (String name, ConfigProp anno, Object value, String[] values, Object valueDefault, String[] valuesDefault) {
		
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
		if (Long           .class.isAssignableFrom(clazz) || Long     .TYPE.isAssignableFrom(clazz)) { return Type.INTEGER; }
		if (Integer        .class.isAssignableFrom(clazz) || Integer  .TYPE.isAssignableFrom(clazz)) { return Type.INTEGER; }
		if (Short          .class.isAssignableFrom(clazz) || Short    .TYPE.isAssignableFrom(clazz)) { return Type.INTEGER; }
		if (Integer        .class.isAssignableFrom(clazz) || Byte     .TYPE.isAssignableFrom(clazz)) { return Type.INTEGER; }
		if (Character      .class.isAssignableFrom(clazz) || Character.TYPE.isAssignableFrom(clazz)) { return Type.INTEGER; }
		if (Double         .class.isAssignableFrom(clazz) || Double   .TYPE.isAssignableFrom(clazz)) { return Type.DOUBLE ; }
		if (Float          .class.isAssignableFrom(clazz) || Float    .TYPE.isAssignableFrom(clazz)) { return Type.DOUBLE ; }
		if (Boolean        .class.isAssignableFrom(clazz) || Boolean  .TYPE.isAssignableFrom(clazz)) { return Type.BOOLEAN; }
		return this.type;
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
	
	public IConfigElement createConfigElement() {
		if (this.isValid) {
			return this.buildConfigElement();
		}
		return null;
	}


	public abstract IConfigElement buildConfigElement();
	
	protected Class getClassEntryForClass () {

		if (this.anno.type() == ConfigProp.Type.ITEM)      { return ItemEntry          .class; }
		if (this.anno.type() == ConfigProp.Type.BLOCK)     { return BlockEntry         .class; }
//		if (this.anno.type() == ConfigProp.Type.SLIDER)    { return SliderEntry        .class; }
		if (Json           .class.isAssignableFrom(clazz)) { return JsonEntry          .class; }
		if (IConfigJsonType.class.isAssignableFrom(clazz)) { return ConfigJsonTypeEntry.class; }
		if (String         .class.isAssignableFrom(clazz)) { return StringEntry        .class; }
		if (Long           .class.isAssignableFrom(clazz) || Long     .TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
		if (Integer        .class.isAssignableFrom(clazz) || Integer  .TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
		if (Short          .class.isAssignableFrom(clazz) || Short    .TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
		if (Integer        .class.isAssignableFrom(clazz) || Byte     .TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
		if (Character      .class.isAssignableFrom(clazz) || Character.TYPE.isAssignableFrom(clazz)) { return IntegerEntry.class; }
		if (Double         .class.isAssignableFrom(clazz) || Double   .TYPE.isAssignableFrom(clazz)) { return DoubleEntry .class; }
		if (Float          .class.isAssignableFrom(clazz) || Float    .TYPE.isAssignableFrom(clazz)) { return DoubleEntry .class; }
		if (Boolean        .class.isAssignableFrom(clazz) || Boolean  .TYPE.isAssignableFrom(clazz)) { return BooleanEntry.class; }
		
		if (clazz.isArray()) {
			
			Class subType = clazz.getComponentType();

			if (Json           .class.isAssignableFrom(subType)) { return JsonEntry          .class; }
			if (IConfigJsonType.class.isAssignableFrom(subType)) { return ConfigJsonTypeEntry.class; }
			
			return ArrayEntry.class;
		}
		return null;
	}
	
	protected CustomElement buildConfigElement(Object o, Object oD) {
		
		Class classEntry = this.getClassEntryForClass();
			
		if (classEntry != null) {
			
			if (o instanceof Object[]) {
				Object[] oAr  = Arrays.copyOf((Object[])o , ((Object[])o) .length);
				Object[] oDAr = Arrays.copyOf((Object[])oD, ((Object[])oD).length);
				return new CustomElement(classEntry, this, oAr, oDAr);
			}
			
			if (o instanceof Character || Character.TYPE.isAssignableFrom(o.getClass())) {
				o = (byte)((Character)o).charValue();
			}
			if (oD instanceof Character || Character.TYPE.isAssignableFrom(oD.getClass())) {
				oD = (byte)((Character)oD).charValue();
			}
			
			return new CustomElement(classEntry, this, o, oD);
		}
		
		
		
		return null;
	}
}
