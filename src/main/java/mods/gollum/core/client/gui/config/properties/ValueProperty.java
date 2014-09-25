package mods.gollum.core.client.gui.config.properties;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import mods.gollum.core.client.gui.config.element.CustomElement;
import mods.gollum.core.client.gui.config.entry.JsonEntry;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.JsonConfigProp;
import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.common.mod.GollumMod;
import mods.gollum.core.tools.simplejson.Json;
import mods.gollum.core.tools.simplejson.IJsonComplement;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import cpw.mods.fml.client.config.IConfigElement;

public class ValueProperty extends GollumProperty {
	
	Object value;
	Object defaultValue;
	
	public ValueProperty(GollumMod mod, Object value, Object defaultValue) {
		super (mod, getType(value.getClass()));
		
		this.value = value;
		this.defaultValue = defaultValue;
		
		JsonConfigProp anno = new JsonConfigProp(); // TODO Reconstruire l'annotation
		
		initNative(anno, value.getClass(), value, defaultValue, "");
		
		if (
			value instanceof IConfigJsonType   ||
			value instanceof IConfigJsonType[] ||
			(value instanceof Json && ((Json)value).isObject())
		) {
			
			this.markUnchange();
			
			this.isValid  = true;
			this.isNative = false;
		}
	}
	
	@Override
	public IConfigElement createCustomConfigElement() {
		
		if (this.value instanceof IConfigJsonType) {
			return new CustomElement(JsonEntry.class, this, this.value, this.defaultValue);
		}
		if (this.value instanceof IConfigJsonType[]) {
			
			IConfigJsonType[] oAr  = (IConfigJsonType[])this.value;
			IConfigJsonType[] oDAr = (IConfigJsonType[])this.defaultValue;
			
			Json[] values        = new Json[oAr .length];
			Json[] defaultValues = new Json[oDAr.length];
			
			for (int i = 0; i < oAr.length; i++) {
				values[i] = oAr[i].writeConfig();
			}
			for (int i = 0; i < oAr.length; i++) {
				defaultValues[i] = oDAr[i].writeConfig();
			}
			
			return new CustomElement(JsonEntry.class, this, values, defaultValues);
		}
		if (value instanceof Json && ((Json)value).isObject()) {
			return new CustomElement(JsonEntry.class, this, ((Json)value).clone(), ((Json)value).clone());
		}
		
		return null;
	}


}
