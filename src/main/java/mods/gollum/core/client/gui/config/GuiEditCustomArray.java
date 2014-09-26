package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;

import mods.gollum.core.client.gui.config.entry.AddButtonEntry;
import mods.gollum.core.client.gui.config.entry.ArrayCustomEntry;
import mods.gollum.core.client.gui.config.entry.ConfigJsonTypeEntry;
import mods.gollum.core.client.gui.config.entry.JsonEntry;
import mods.gollum.core.client.gui.config.properties.ValueProperty;
import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.tools.simplejson.Json;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.GuiEditArrayEntries.BooleanEntry;
import cpw.mods.fml.client.config.GuiEditArrayEntries.DoubleEntry;
import cpw.mods.fml.client.config.GuiEditArrayEntries.IntegerEntry;
import cpw.mods.fml.client.config.GuiEditArrayEntries.StringEntry;

public class GuiEditCustomArray extends GuiGollumConfig {
	
//	protected ArrayList<Class> valuesType;
	protected Object[] values;
	protected Object[] defaultValues;
	
	public GuiEditCustomArray(GuiConfig parent, IConfigElement configElement, int slotIndex, ArrayCustomEntry entry, Object[] values, Object[] defaultValues) {
		super(parent, getFields(parent, entry, values, defaultValues), entry);
		
//		this.valuesType    = new ArrayList<Class>();
		this.values        = values;
		this.defaultValues = defaultValues;
		
//		for(Object value : values) {
//			this.valuesType.add(value.getClass());
//		}
	}
	
	@Override
	public void initGui() {
		
		boolean needsRefresh = this.needsRefresh;
		super.initGui();
		
		if (needsRefresh) {
			
			for(int i = this.defaultValues.length; i < this.values.length; i++) {
				
				Object deafultValue = ((ArrayCustomEntry)this.entry).createNewLine ();
				
				this.addNewEntry(i, this.values[i], deafultValue);
			}
			
			this.entryList.listEntries.add(new AddButtonEntry (this));
			super.initGui();
		}
		
		this.entryList.controlX   += 22;
		this.entryList.scrollBarX += 22;
		
	}
	
	private static List<IConfigElement> getFields(GuiConfig parent, ArrayCustomEntry entry, Object[] values, Object[] defaultValues) {
		ArrayList<IConfigElement> fields = new ArrayList<IConfigElement>();
		
		for(int i = 0; i < values.length; i++) {
			
			if (i < defaultValues.length) {
				ValueProperty prop     = new ValueProperty(getMod(parent), values[i], defaultValues[i]);
				IConfigElement element = prop.createConfigElement ();

				if (element != null) {
					fields.add(element);
				}
			}
		}
		
		return fields;
	}
	
	public void saveChanges() {
		
		List tmp = new ArrayList<Object>();
		
		for (IConfigEntry entry : this.entryList.listEntries) {
			
			Object newValue = entry.getCurrentValue();
			
			if (entry instanceof JsonEntry) {
				newValue = ((JsonEntry)entry).getValue();
			}else
			if (entry instanceof ConfigJsonTypeEntry) {
				newValue = ((ConfigJsonTypeEntry)entry).getValue();
			}
			if (newValue != null) {
				log.debug("Save line : "+newValue);
				tmp.add(newValue);
			}
		}
		
		this.entry.setValueFromChildScreen(tmp.toArray());
	}
	
	protected boolean doneAction() {
		
		this.saveChanges();
		
		this.mc.displayGuiScreen(this.parentScreen);
		return true;
	}
	
	public void addNewEntry(int index) {
		
		log.debug ("More pressed");

		Object o  = ((ArrayCustomEntry)this.entry).createNewLine ();
		Object oD = ((ArrayCustomEntry)this.entry).createNewLine ();
		
		this.addNewEntry(index, o, oD);
	}
	
	public void addNewEntry (int index, Object value, Object defaultValue) {
		
		IConfigEntry newEntry = null;
		
		if (value != null && defaultValue != null) {
			ValueProperty  prop      = new ValueProperty(this.mod, value, defaultValue);
			IConfigElement element = prop.createConfigElement ();
			if (element != null) {
				try {
					
					newEntry = (IConfigEntry) element.getConfigEntryClass().getConstructor(
						GuiConfig.class,
						GuiConfigEntries.class,
						IConfigElement.class
					).newInstance(this, this.entryList, element);
					
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		if (newEntry != null) {
			this.entryList.listEntries.add(index, newEntry);
		}
	}
	
	public void removeEntry(int index) {

		log.debug ("Minus pressed");
		
		this.entryList.listEntries.remove(index);
//		this.canAddMoreEntries = !configElement.isListLengthFixed() 
//			&& (configElement.getMaxListLength() == -1 || this.listEntries.size() - 1 < configElement.getMaxListLength());
//		keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
	}
}
