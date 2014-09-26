package mods.gollum.core.client.gui.config.properties;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import mods.gollum.core.client.gui.config.element.CustomElement;
import mods.gollum.core.client.gui.config.entry.ConfigJsonTypeEntry;
import mods.gollum.core.client.gui.config.entry.JsonEntry;
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
			
			init(anno, f.getType(), f.get(configLoad.config), f.get(configLoad.configDefault), f.getName());
			
		}
		
	}
	
	@Override
	public IConfigElement createCustomConfigElement() {
		try {
			
			Object o  = f.get(this.configLoad.config);
			Object oD = f.get(this.configLoad.configDefault);
			setName(this.f.getName());
			
			return buildCustomConfigElement(o, oD);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
