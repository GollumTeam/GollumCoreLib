package com.gollum.core.client.gui.config.element;

import com.gollum.core.common.config.ConfigProp;

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
