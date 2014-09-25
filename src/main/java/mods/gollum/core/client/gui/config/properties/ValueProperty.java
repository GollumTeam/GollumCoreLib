package mods.gollum.core.client.gui.config.properties;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import mods.gollum.core.client.gui.config.element.CustomElement;
import mods.gollum.core.client.gui.config.entry.ConfigJsonTypeEntry;
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
		
		init(anno, value.getClass(), value, defaultValue, "");
		
	}
	
	@Override
	public IConfigElement createCustomConfigElement() {
		return buildCustomConfigElement(this.value, this.defaultValue);
	}


}
