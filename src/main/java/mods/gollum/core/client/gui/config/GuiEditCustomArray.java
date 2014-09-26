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
	
	protected ArrayList<Class> valuesType = new ArrayList<Class>();
	
	public GuiEditCustomArray(GuiConfig parent, IConfigElement configElement, int slotIndex, ArrayCustomEntry entry, Object[] values, Object[] defaultValues) {
		super(parent, getFields(parent, values, defaultValues), entry);
		
		for(Object value : values) {
			this.valuesType.add(value.getClass());
		}
	}
	
	@Override
	public void initGui() {
		
		boolean needsRefresh = this.needsRefresh;
		super.initGui();
		
		if (needsRefresh) {
			this.entryList.listEntries.add(new AddButtonEntry (this));
			super.initGui();
		}
		
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
	
	public void saveChanges() {
		
		List tmp = new ArrayList<Object>();
		
		for (IConfigEntry entry : this.entryList.listEntries) {
			Object newValue = entry.getCurrentValue();
			if (entry instanceof ConfigJsonTypeEntry) {
				
			}
			
			if (newValue != null) {
				if (newValue instanceof Json) {
					newValue = ((Json) newValue).clone();
				} else
				if (newValue instanceof IConfigJsonType) {
					try {
						
						IConfigJsonType configJsonType = (IConfigJsonType)newValue;
						IConfigJsonType copy           = (IConfigJsonType)newValue.getClass().newInstance();
						
						copy.readConfig(configJsonType.writeConfig());
						
						newValue = copy;
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
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

		Object o  = ((ArrayCustomEntry)this.entry).createNewSubEntry ();
		Object oD = ((ArrayCustomEntry)this.entry).createNewSubEntry ();
		
		IConfigEntry newEntry = null;
		
		if (o != null && oD != null) {
			ValueProperty  prop      = new ValueProperty(this.mod, o, oD);
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
