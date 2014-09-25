package mods.gollum.core.client.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import mods.gollum.core.client.gui.config.entries.GuiEditArrayCustomEntries;
import mods.gollum.core.client.gui.config.entries.entry.ArrayCustomEntry;
import mods.gollum.core.client.gui.config.properties.JsonProperty;
import mods.gollum.core.client.gui.config.properties.ValueProperty;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiEditArray;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.config.HoverChecker;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;


public class GuiEditCustomArray extends GuiGollumConfig {
	
	public GuiEditCustomArray(GuiConfig parent, IConfigElement configElement, ArrayCustomEntry entry, Object[] values, Object[] defaultValues) {
		super(parent, getFields(parent, values, defaultValues), entry);
		

		this.entryList.controlX   += 22;
		this.entryList.scrollBarX += 22;
		
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
