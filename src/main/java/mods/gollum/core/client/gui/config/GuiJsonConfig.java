package mods.gollum.core.client.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import mods.gollum.core.client.gui.config.entries.GollumCategoryEntry;
import mods.gollum.core.client.gui.config.properties.FieldProperty;
import mods.gollum.core.client.gui.config.properties.JsonProperty;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.mod.GollumMod;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.ModContainer;

public class GuiJsonConfig extends GuiGollumConfig {
	
	public GuiJsonConfig(GuiConfig parent, String name, Json value, Json defaultValue) {
		super(parent, getFields (parent, value, defaultValue), parent.title);
		
		this.titleLine2 = parent.titleLine2 + " > "+name;
	}
	
	private static List<IConfigElement> getFields(GuiConfig parent, Json value, Json defaultValue) {
		
		ArrayList<IConfigElement> fields = new ArrayList<IConfigElement>();
		
		if (value.isObject()) {
			
			for(Entry<String, Json> entry : value.allChildWithKey()) {
				
				JsonProperty prop      = new JsonProperty (getMod(parent), entry.getKey(), entry.getValue(), defaultValue.child(entry.getKey()));
				IConfigElement element = prop.createConfigElement ();
				
				if (element != null) {
					fields.add(element);
				}
			}
						
		}
		
		return fields;
	}
	
}