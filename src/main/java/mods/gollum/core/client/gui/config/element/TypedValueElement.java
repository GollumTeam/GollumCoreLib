package mods.gollum.core.client.gui.config.element;

import java.lang.reflect.Field;

import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.ConfigProp;

public class TypedValueElement extends ConfigElement {
	
	private Class type;
	private ConfigProp prop;
	
	public TypedValueElement(Class type, String name, Object value, Object defaultValue, ConfigProp prop) {
		super(name);
		this.type         = type;
		this.value        = value;
		this.defaultValue = defaultValue;
		this.prop         = prop;
	}
	
	@Override
	public ConfigProp getConfigProp() {
		return this.prop;
	}

	@Override
	public Class getType() {
		return this.type;
	}

}
