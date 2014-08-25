package mods.gollum.core.common.i18n;

import net.minecraft.util.StatCollector;
import mods.gollum.core.common.context.ModContext;
import mods.gollum.core.common.mod.GollumMod;

public class I18n {
	
	String modId;
	
	public I18n() {
		modId = ModContext.instance().getCurrent().getModId().toLowerCase();
	}
	
	public String trans (String key, Object ... args) {
		return StatCollector.translateToLocalFormatted(this.modId+"."+key, args);
	}
}
