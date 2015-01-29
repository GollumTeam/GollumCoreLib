package mods.gollum.core.common.config.type;

import mods.gollum.core.tools.simplejson.Json;

public abstract class ConfigJsonType implements Cloneable{
	
	public abstract void readConfig(Json json);
	
	public abstract Json writeConfig();
	
	public boolean equals (Object obj) {
		
		if (obj instanceof ConfigJsonType) {
			return ((ConfigJsonType)obj).writeConfig().equals(this.writeConfig());
		}
		
		return false;
	}
	
	public Object clone () {
		try {
			Object o = this.getClass().newInstance();
			((ConfigJsonType)o).readConfig(this.writeConfig());
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return this;
	}
}
