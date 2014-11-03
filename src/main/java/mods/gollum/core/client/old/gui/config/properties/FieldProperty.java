package mods.gollum.core.client.old.gui.config.properties;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import mods.gollum.core.client.old.gui.config.element.CustomElement;
import mods.gollum.core.client.old.gui.config.entry.ConfigJsonTypeEntry;
import mods.gollum.core.client.old.gui.config.entry.JsonEntry;
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
		super (f.getType(), configLoad.mod, Type.STRING);
		
		this.configLoad = configLoad;
		this.f = f;
		
		f.setAccessible(true);
		this.anno = f.getAnnotation(ConfigProp.class);
		
		if (this.anno != null && this.anno.group().equals (currentCategory)) {
			
			init(f.get(configLoad.config), f.get(configLoad.configDefault), f.getName());
			
		}
		
	}
	
	@Override
	public IConfigElement buildConfigElement() {
		try {
			
			Object o  = f.get(this.configLoad.config);
			Object oD = f.get(this.configLoad.configDefault);
			setName(this.f.getName());
			
			return buildConfigElement(o, oD);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
