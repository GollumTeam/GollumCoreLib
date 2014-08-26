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

	public int transInt (String key, Object ... args) {
		String s = StatCollector.translateToLocalFormatted(this.modId+"."+key, args);
		int rtn = 0; try { rtn = Integer.parseInt(s); } catch (Exception e) {}
		return rtn;
	}
	public long transLong (String key, Object ... args) {
		String s = StatCollector.translateToLocalFormatted(this.modId+"."+key, args);
		long rtn = 0; try { rtn = Long.parseLong(s); } catch (Exception e) {}
		return rtn;
	}
	public boolean transBoolean (String key, Object ... args) {
		String s = StatCollector.translateToLocalFormatted(this.modId+"."+key, args);
		boolean rtn = false; try { rtn = Boolean.parseBoolean(s); } catch (Exception e) {}
		return rtn;
	}
	public float transFloat (String key, Object ... args) {
		String s = StatCollector.translateToLocalFormatted(this.modId+"."+key, args);
		float rtn = 0; try { rtn = Float.parseFloat(s); } catch (Exception e) {}
		return rtn;
	}
	public double transDouble (String key, Object ... args) {
		String s = StatCollector.translateToLocalFormatted(this.modId+"."+key, args);
		double rtn = 0; try { rtn = Double.parseDouble(s); } catch (Exception e) {}
		return rtn;
	}
}
