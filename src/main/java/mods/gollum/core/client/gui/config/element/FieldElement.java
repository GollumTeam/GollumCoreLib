package mods.gollum.core.client.gui.config.element;

import java.lang.reflect.Field;

import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.common.config.ConfigProp;

public class FieldElement extends ConfigElement {
	
	private Field field;
	private ConfigProp prop;
	
	public FieldElement(Field f, ConfigProp prop) {
		super(f.getName());
		this.field = f;
		this.prop = prop;
	}
	
	@Override
	public ConfigProp getConfigProp() {
		return this.prop;
	}

	@Override
	protected Class getType() {
		return this.field.getType();
	}

}
