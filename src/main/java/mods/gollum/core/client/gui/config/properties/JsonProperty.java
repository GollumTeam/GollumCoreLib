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

public class JsonProperty extends GollumProperty {
	
	Json json;
	Json defaultJson;
	
	public JsonProperty(GollumMod mod, String name, Json json, Json defaultJson) {
		
		super (mod, getType(json));

		this.json        = json;
		this.defaultJson = defaultJson;
		
		if (this.getType() != null) {
			
			JsonConfigProp anno = (JsonConfigProp)json.getComplement(JsonConfigProp.class);
			anno = (anno == null) ? new JsonConfigProp() : anno;
			
			Object value        = json.value();
			Object valueDefault = defaultJson.value();
			
			if (json.isChar()) { // Fixe affichage
				value        = (value        != null) ? (byte)((Character)value)       .charValue() : 0;
				valueDefault = (valueDefault != null) ? (byte)((Character)valueDefault).charValue() : 0;
			}
			
			this.initDatas(name, anno, value, null, valueDefault, null);
			
			// Limit short byte char
			if (json.isShort()) { this.setLimitShort(); }
			if (json.isByte())  { this.setLimitByte();  }
			if (json.isChar())  { this.setLimitChar();  }
			
			this.markUnchange();
			
			this.isValid = true;
			this.isNative = true;
		}
		
		if (json.isObject()) {
			
			this.markUnchange();
			
			this.isValid = true;
			this.isNative = false;
		}
	}
	
	private static Type getType(Json json) {
		
		Type type = null;
		
		if (json.isString()) {
			type = Property.Type.STRING;
		}
		if (
			json.isLong()  ||
			json.isInt()   ||
			json.isShort() ||
			json.isByte()  ||
			json.isChar() 
		) {
			type = Property.Type.INTEGER;
		}
		if (
			json.isDouble()  ||
			json.isFloat() 
		) {
			type = Property.Type.DOUBLE;
		}
		if (json.isBool()) {
			type = Property.Type.BOOLEAN;
		}
		return type;
	}
	
	@Override
	public IConfigElement createCustomConfigElement() {
		
		if (this.json.isObject()) {
			this.setName("");
			return new CustomElement(JsonEntry.class, this, json.clone(), defaultJson.clone());
		}
		return null;
	}


}
