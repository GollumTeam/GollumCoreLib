package mods.gollum.core.i18n;

import net.minecraft.util.StatCollector;
import mods.gollum.core.mod.ModMetaInfos;

public class I18n {
	
	String modId;
	
	public I18n(Object mod) {
		modId = new ModMetaInfos(mod).getModid().toLowerCase();
	}
	
	public String trans (String key, Object ... args) {
		return StatCollector.translateToLocalFormatted(this.modId+"."+key, args);
	}
}
