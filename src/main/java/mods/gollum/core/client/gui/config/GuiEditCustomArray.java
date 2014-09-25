package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import mods.gollum.core.client.gui.config.entry.AddButtonEntry;
import mods.gollum.core.client.gui.config.entry.ArrayCustomEntry;
import mods.gollum.core.client.gui.config.properties.ValueProperty;
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
	
	public GuiEditCustomArray(GuiConfig parent, IConfigElement configElement, int slotIndex, ArrayCustomEntry entry, Object[] values, Object[] defaultValues) {
		super(parent, getFields(parent, values, defaultValues), entry);
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
					).newInstance(this, this, element);
					
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		// TODO faire les tabelau classique
		
		if (newEntry != null) {
			this.entryList.listEntries.add(index, newEntry);
		}
		
//        if (configElement.isList() && configElement.getType() == ConfigGuiType.BOOLEAN)
//            listEntries.add(index, new BooleanEntry(this.owningGui, this, this.configElement, Boolean.valueOf(true)));
//        else if (configElement.isList() && configElement.getType() == ConfigGuiType.INTEGER)
//            listEntries.add(index, new IntegerEntry(this.owningGui, this, this.configElement, 0));
//        else if (configElement.isList() && configElement.getType() == ConfigGuiType.DOUBLE)
//            listEntries.add(index, new DoubleEntry(this.owningGui, this, this.configElement, 0.0D));
//        else if (configElement.isList())
//            listEntries.add(index, new StringEntry(this.owningGui, this, this.configElement, ""));
//        this.canAddMoreEntries = !configElement.isListLengthFixed() 
//                && (configElement.getMaxListLength() == -1 || this.listEntries.size() - 1 < configElement.getMaxListLength());
//        keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
	}
	

	public void removeEntry(int index) {

		log.debug ("Minus pressed");
		
		this.entryList.listEntries.remove(index);
//		this.canAddMoreEntries = !configElement.isListLengthFixed() 
//			&& (configElement.getMaxListLength() == -1 || this.listEntries.size() - 1 < configElement.getMaxListLength());
//		keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_END);
    }
}
