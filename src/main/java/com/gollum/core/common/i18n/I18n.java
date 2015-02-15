package com.gollum.core.common.i18n;

import com.gollum.core.common.context.ModContext;

import net.minecraft.util.StatCollector;

public class I18n {
	
	String modId;
	
	public I18n() {
		modId = ModContext.instance().getCurrent().getModId().toLowerCase();
	}
	
	public String trans (String key, Object ... args) {
		return StatCollector.translateToLocalFormatted(this.key(key), args);
	}
	
	public String key (String key) {
		return this.modId+"."+key;
	}
	
	public boolean transExist (String key) {
		return !this.key(key).equals(this.trans(key));
	}
	
}
