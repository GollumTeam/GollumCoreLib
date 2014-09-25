package mods.gollum.core.client.gui.config.properties;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import mods.gollum.core.client.gui.config.element.CustomElement;
import mods.gollum.core.client.gui.config.entries.entry.JsonEntry;
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
		super (configLoad.mod, getType(f.getType()));
		
		this.configLoad = configLoad;
		this.f = f;
		
		f.setAccessible(true);
		ConfigProp anno = f.getAnnotation(ConfigProp.class);
		
		if (anno != null && anno.group().equals (currentCategory)) {
			
			initNative(anno, f.getType(), f.get(configLoad.config), f.get(configLoad.configDefault), f.getName());
			
			if (
				IConfigJsonType  .class.isAssignableFrom(f.getType()) ||
				IConfigJsonType[].class.isAssignableFrom(f.getType())
			) {
				
				this.markUnchange();
				
				this.isValid = true;
				this.isNative = false;
			}
		}
		
	}
	
	@Override
	public IConfigElement createCustomConfigElement() {
		try {
			
			Object o  = f.get(this.configLoad.config);
			Object oD = f.get(this.configLoad.configDefault);
			
			if (o instanceof IConfigJsonType) {
				
				Json value        = ((IConfigJsonType)o ).writeConfig();
				Json defaultValue = ((IConfigJsonType)oD).writeConfig();
				
				this.setName(f.getName());
				return new CustomElement(JsonEntry.class, this, value, defaultValue);
			}
			
			if (f.get(this.configLoad.config) instanceof IConfigJsonType[]) {
				
				IConfigJsonType[] oAr  = (IConfigJsonType[])o;
				IConfigJsonType[] oDAr = (IConfigJsonType[])oD;
				
				Json[] values        = new Json[oAr .length];
				Json[] defaultValues = new Json[oDAr.length];
				
				for (int i = 0; i < oAr.length; i++) {
					values[i] = oAr[i].writeConfig();
				}
				for (int i = 0; i < oAr.length; i++) {
					defaultValues[i] = oDAr[i].writeConfig();
				}
				
				this.setName(f.getName());
				return new CustomElement(JsonEntry.class, this, values, defaultValues);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
