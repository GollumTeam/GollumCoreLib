package mods.gollum.core.client.gui.config.entry;

import static mods.gollum.core.ModGollumCoreLib.log;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import net.minecraft.client.Minecraft;

public class LongEntry extends StringEntry {
	
	public LongEntry(Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(mc, parent, configElement);
	}
	
	@Override
	public Object getValue() {
		
		Long value = new Long(0L);
		try {
			value = Long.parseLong(this.textFieldValue.getText());
		} catch (Exception e) {
		}
		
		return value;
	}
	
	private long getLongValue () {
		return new Long(this.getValue().toString());
	}
	
	@Override
	protected boolean validKeyTyped(char eventChar) {
		if (eventChar <= 31 || (eventChar >= '0' && eventChar <='9') || eventChar == '-') {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isValidValue() {
		
		Long min = (Long)this.configElement.getMin();
		Long max = (Long)this.configElement.getMax();
		
		return 
			this.textFieldValue.getText().equals(this.getValue().toString()) &&
			this.getLongValue() >= min &&
			this.getLongValue() <= max
		;
	}

}
