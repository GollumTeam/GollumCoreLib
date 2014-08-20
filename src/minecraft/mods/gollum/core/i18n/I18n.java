package mods.gollum.core.i18n;

import net.minecraft.util.StatCollector;
import mods.gollum.core.context.ModContext;
import mods.gollum.core.mod.GollumMod;

public class I18n {
	
	String modId;
	
	public I18n() {
		modId = ModContext.instance().getCurrent().getModid().toLowerCase();
	}
	
	public String trans (String key, Object ... args) {
		return StatCollector.translateToLocalFormatted(this.modId+"."+key, args);
	}
}
