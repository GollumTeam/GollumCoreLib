package mods.gollum.core.client.gui.config;

import java.lang.reflect.Field;

import net.minecraftforge.common.config.ConfigElement;
import mods.gollum.core.client.gui.config.entries.BlockEntry;
import mods.gollum.core.client.gui.config.entries.ItemEntry;
import mods.gollum.core.client.gui.config.entries.JsonEntry;
import mods.gollum.core.client.gui.config.properties.GollumProperty;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.tools.simplejson.Json;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;

public class CustomElement extends ConfigElement {
	
	private Class< ? extends IConfigEntry> classEntry;
	private Object value;
	private Object defaultValue;

	public CustomElement(Class< ? extends IConfigEntry> classEntry, GollumProperty property) {
		super(property);
		
		this.classEntry   = classEntry;
		this.value        = property.getString();
		this.defaultValue = property.getDefault();
		
	}

	public CustomElement(Class< ? extends IConfigEntry> classEntry, GollumProperty property, Object value, Object defaultValue) {
		super(property);
		
		this.classEntry   = classEntry;
		this.value        = value;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public Class<? extends IConfigEntry> getConfigEntryClass() {
		return this.classEntry;
	}

}
