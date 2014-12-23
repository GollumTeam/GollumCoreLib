package mods.gollum.core.client.gui.config.element;

import java.lang.reflect.Field;

import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.ConfigProp;

public class FieldElement extends ConfigElement {

	private ConfigLoad configLoad;
	private Field field;
	private ConfigProp prop;
	
	public FieldElement(Field f, ConfigProp prop, ConfigLoad configLoad) {
		super(f.getName());
		this.field      = f;
		this.prop       = prop;
		this.configLoad = configLoad;
		
		try {
			this.value        = f.get(this.configLoad.config);
			this.defaultValue = f.get(this.configLoad.configDefault);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ConfigProp getConfigProp() {
		return this.prop;
	}

	@Override
	public Class getType() {
		return this.field.getType();
	}

}
