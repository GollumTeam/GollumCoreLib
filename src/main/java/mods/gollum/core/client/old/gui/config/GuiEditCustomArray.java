package mods.gollum.core.client.old.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

import mods.gollum.core.client.old.gui.config.entry.AddButtonEntry;
import mods.gollum.core.client.old.gui.config.entry.ArrayEntry;
import mods.gollum.core.client.old.gui.config.entry.ConfigJsonTypeEntry;
import mods.gollum.core.client.old.gui.config.entry.JsonEntry;
import mods.gollum.core.client.old.gui.config.properties.ValueProperty;
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

	protected Object[] values;
	protected Object[] undoValues;
	protected Object[] defaultValues;
	
	public GuiEditCustomArray(GuiConfig parent, int slotIndex, ArrayEntry entry, Object[] values, Object[] defaultValues) {
		super(parent, getFields(parent, entry, values, defaultValues), entry);

		this.values        = values;
		this.undoValues    = entry.getBeforeValues();
		this.defaultValues = defaultValues;
	}
	
	private static List<IConfigElement> getFields(GuiConfig parent, ArrayEntry entry, Object[] values, Object[] defaultValues) {
		ArrayList<IConfigElement> fields = new ArrayList<IConfigElement>();
		
		for(int i = 0; i < values.length; i++) {

			if (i < defaultValues.length && defaultValues[i] != null) {
				
				ValueProperty prop     = new ValueProperty(getMod(parent), values[i], defaultValues[i]);
				IConfigElement element = prop.createConfigElement ();
				
				if (element != null) {
					fields.add(element);
				}
			}
		}
		
		return fields;
	}
	
	@Override
	public void initGui() {
		
		boolean needsRefresh = this.needsRefresh;
		super.initGui();
		
		if (needsRefresh) {
			
			for(int i = 0; i < this.values.length; i++) {
				
				if (i >= this.defaultValues.length || this.defaultValues[i] == null) {
					Object defaultValue = ((ArrayEntry)this.entry).createNewLine ();
					
					this.addNewEntry(i, this.values[i], defaultValue);
				}
			}
			
			super.initGui();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		Object[] currentValues = this.getCurrentsValues();
		
		this.btnUndo .enabled = (this.entryList.areAnyEntriesEnabled(true) && this.entryList.hasChangedEntry(true)      ) || !Arrays.deepEquals(this.undoValues   , currentValues);
		this.btnReset.enabled = (this.entryList.areAnyEntriesEnabled(true) && !this.entryList.areAllEntriesDefault(true)) || !Arrays.deepEquals(this.defaultValues, currentValues);
		this.btnUndo .drawButton(this.mc, mouseX, mouseY);
		this.btnReset.drawButton(this.mc, mouseX, mouseY);
		
		this.entryList.drawScreenPost(mouseX, mouseY, partialTicks);
		if (this.undoHoverChecker.checkHover(mouseX, mouseY)) {
			this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.undoAll"), 300), mouseX, mouseY);
		}
		if (this.resetHoverChecker.checkHover(mouseX, mouseY)) {
			this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.resetAll"), 300), mouseX, mouseY);
		}
		if (this.checkBoxHoverChecker.checkHover(mouseX, mouseY)) {
			this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.applyGlobally"), 300), mouseX, mouseY);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			
			if (doneAction ()) {
				return;
			}
			
		} else if (button.id == 2001) {
			this.values = Arrays.copyOf(this.defaultValues, this.defaultValues.length);
			this.rebuildAllEntries ();
		} else if (button.id == 2002) {
			this.values = Arrays.copyOf(this.undoValues, this.undoValues.length);
			this.rebuildAllEntries ();
		}
	}
	
	public void rebuildAllEntries () {
		this.entryList.listEntries.clear();
		for (int i = 0; i < this.values.length; i++) {
			
			Object defaultValue = null; 
			if (i < this.defaultValues.length) {
				defaultValue = this.defaultValues[i];
			} else {
				defaultValue = ((ArrayEntry)this.entry).createNewLine ();
			}
			this.addNewEntry(i, this.values[i], defaultValue);
		}
	}
	
	public Object[] getCurrentsValues () {

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
				tmp.add(newValue);
			}
		}
		
		return tmp.toArray();
	}
	
	public void saveChanges() {
		this.entry.setValueFromChildScreen(this.getCurrentsValues());
	}
	
	protected boolean doneAction() {
		
		this.saveChanges();
		
		this.mc.displayGuiScreen(this.parentScreen);
		return true;
	}
	
	public void addNewEntry(int index) {
		
		log.debug ("More pressed");

		Object o  = ((ArrayEntry)this.entry).createNewLine ();
		Object oD = ((ArrayEntry)this.entry).createNewLine ();
		
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
	}
	
	public boolean isArray() {
		return true;
	}
}
