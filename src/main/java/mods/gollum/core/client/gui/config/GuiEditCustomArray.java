package mods.gollum.core.client.gui.config;

import java.util.ArrayList;
import java.util.List;

import mods.gollum.core.client.gui.config.entries.entry.ArrayCustomEntry;
import mods.gollum.core.client.gui.config.properties.ValueProperty;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class GuiEditCustomArray extends GuiGollumConfig {
	
	public GuiEditCustomArray(GuiConfig parent, IConfigElement configElement, ArrayCustomEntry entry, Object[] values, Object[] defaultValues) {
		super(parent, getFields(parent, values, defaultValues), entry);
		
//		try {
//			this.setFinalStatic (GuiConfig.class.getDeclaredField("entryList"), new GuiConfigEntries(this, mc)); 
//			this.setFinalStatic (GuiConfig.class.getDeclaredField("initEntries"), entryList.listEntries); 
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
//	void setFinalStatic(Field field, Object newValue) throws Exception {
//		field.setAccessible(true);
//		
//		Field modifiersField = Field.class.getDeclaredField("modifiers");
//		modifiersField.setAccessible(true);
//		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
//		
//		field.set(this, newValue);
//		
//		modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL);
//	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		this.entryList.controlX   += 22;
		this.entryList.scrollBarX += 22;
	}
	
	private static List<IConfigElement> getFields(GuiConfig parent, Object[] values, Object[] defaultValues) {
		ArrayList<IConfigElement> fields = new ArrayList<IConfigElement>();
		
		for(int i = 0; i < values.length; i++) {
			
			ValueProperty prop      = new ValueProperty(getMod(parent), values[i], defaultValues[i]);
			IConfigElement element = prop.createConfigElement ();
			
			if (element != null) {
				fields.add(element);
			}
		}
		
		return fields;
	}
}
