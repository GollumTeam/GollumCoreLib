package mods.gollum.core.client.gui.config;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import mods.gollum.core.client.gui.config.entries.BlockEntry;
import mods.gollum.core.client.gui.config.entries.ItemEntry;
import mods.gollum.core.client.gui.config.entries.JsonEntry;
import mods.gollum.core.client.gui.config.properties.GollumProperty;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.type.IConfigJsonType;
import mods.gollum.core.tools.simplejson.Json;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class CustomElement implements IConfigElement {
	
	private Class< ? extends IConfigEntry> classEntry;
	private Object value;
	private Object defaultValue;
	private GollumProperty property;
	
	public CustomElement(Class< ? extends IConfigEntry> classEntry, GollumProperty property) {
		super();
		
		this.property     = property;
		this.classEntry   = classEntry;
		this.value        = property.getString();
		this.defaultValue = property.getDefault();
		
	}

	public CustomElement(Class< ? extends IConfigEntry> classEntry, GollumProperty property, Object value, Object defaultValue) {

		this.property     = property;
		this.classEntry   = classEntry;
		this.value        = value;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public Class<? extends IConfigEntry> getConfigEntryClass() {
		return this.classEntry;
	}

	@Override
	public boolean isProperty() {
		return true;
	}

	@Override
	public Class getArrayEntryClass() {
		return null; // TODO
	}

	@Override
	public String getName() {
		return this.property.getName();
	}

	@Override
	public String getQualifiedName() {
		return this.property.getName();
	}

	@Override
	public String getLanguageKey() {
		return this.property.getLanguageKey();
	}

	@Override
	public String getComment() {
		return this.property.comment;
	}

	@Override
	public List getChildElements() {
		return null; // TODO
	}

	@Override
	public ConfigGuiType getType() {
		return 
			property.getType() == Property.Type.BOOLEAN ? ConfigGuiType.BOOLEAN : 
			property.getType() == Property.Type.DOUBLE  ? ConfigGuiType.DOUBLE  :
			property.getType() == Property.Type.INTEGER ? ConfigGuiType.INTEGER :
			property.getType() == Property.Type.COLOR   ? ConfigGuiType.COLOR   :
			property.getType() == Property.Type.MOD_ID  ? ConfigGuiType.MOD_ID   : 
			ConfigGuiType.STRING
		;
	}
	

	@Override
	public boolean isList() {
		return false; // TODO
	}

	@Override
	public boolean isListLengthFixed() {
		return false; // TODO
	}

	@Override
	public int getMaxListLength() {
		return 0;
	}

	@Override
	public boolean isDefault() {
		return this.value.equals(this.defaultValue);
	}

	@Override
	public Object getDefault() {
		return this.defaultValue;
	}

	@Override
	public Object[] getDefaults() {
		return null; //
	}

	@Override
	public void setToDefault() {
		if (this.defaultValue instanceof Json) {
			this.value = ((Json)this.defaultValue).clone();
		} else {
			this.value = this.defaultValue;
		}
	}

	@Override
	public boolean requiresWorldRestart() {
		return property.requiresWorldRestart();
	}

	@Override
	public boolean showInGui() {
		return property.showInGui();
	}

	@Override
	public boolean requiresMcRestart() {
		return property.requiresMcRestart();
	}

	@Override
	public Object get() {
		return this.value;
	}

	@Override
	public Object[] getList() {
		return null;// TODO 
	}

	@Override
	public void set(Object value) {
		this.value = value;
	}

	@Override
	public void set(Object[] aVal) {
		// TODO
	}

	@Override
	public String[] getValidValues() {
		return this.property.getValidValues();
	}

	@Override
	public Object getMinValue() {
		return this.property.getMinValue();
	}

	@Override
	public Object getMaxValue() {
		return this.property.getMaxValue();
	}

	@Override
	public Pattern getValidationPattern() {
		return this.property.getValidationPattern();
	}

}
