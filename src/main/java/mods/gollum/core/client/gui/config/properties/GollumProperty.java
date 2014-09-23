package mods.gollum.core.client.gui.config.properties;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import mods.gollum.core.client.gui.config.JsonElement;
import mods.gollum.core.client.gui.config.entries.GollumCategoryEntry;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.common.mod.GollumMod;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.IConfigElement;

public abstract class GollumProperty extends Property {

	protected boolean isValid     = false;
	protected boolean isNative = false;
	protected GollumMod mod;
	private ConfigLoad configLoad;
	private Field f;
	
	
	public GollumProperty(GollumMod mod, Type type) {
		super ("", "", type);
		this.mod = mod;
	}
	
	protected void init (String name, ConfigProp anno, Object value, String[] values, Object valueDefault, String[] valuesDefault) {
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
			this.setValue(value.toString());
			this.setDefaultValue(valueDefault.toString());

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
				return new ConfigElement(this);
			} else {
				return this.createCustomConfigElement();
			}
		}
		return null;
	}
	
	public abstract IConfigElement createCustomConfigElement ();
}
