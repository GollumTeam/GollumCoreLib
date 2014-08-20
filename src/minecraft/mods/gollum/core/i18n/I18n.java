package mods.gollum.core.i18n;

import net.minecraft.util.StatCollector;
import mods.gollum.core.mod.GollumMod;

public class I18n {
	
	String modId;
	
	public I18n(GollumMod mod) {
		modId = mod.getModid().toLowerCase();
	}
	
	public String trans (String key, Object ... args) {
		return StatCollector.translateToLocalFormatted(this.modId+"."+key, args);
	}
}
