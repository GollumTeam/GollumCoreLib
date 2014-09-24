package mods.gollum.core.client.gui.config.element;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import mods.gollum.core.client.gui.config.entries.entry.ArrayCustomEntry;
import mods.gollum.core.client.gui.config.properties.GollumProperty;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class CustomElement implements IConfigElement {
	
	private Class< ? extends IConfigEntry> classEntry;
	private Object value;
	private Object defaultValue;
	private Object[] values;
	private Object[] defaultValues;
	private GollumProperty property;
	private boolean isArray = false;
	
	public CustomElement(Class< ? extends IConfigEntry> classEntry, GollumProperty property) {
		super();
		
		this.property     = property;
		this.classEntry   = classEntry;
		this.value        = property.getString();
		this.defaultValue = property.getDefault();
		
	}

	public CustomElement(Class< ? extends IConfigEntry> classEntry, GollumProperty property, Object[] values, Object[] defaultValues) {
		this.property      = property;
		this.classEntry    = classEntry;
		this.values        = values;
		this.defaultValues = defaultValues;
		this.isArray = true;
	}
	
	public CustomElement(Class< ? extends IConfigEntry> classEntry, GollumProperty property, Object value, Object defaultValue) {

		this.property     = property;
		this.classEntry   = classEntry;
		this.value        = value;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public Class<? extends IConfigEntry> getConfigEntryClass() {
		return (this.isArray) ? ArrayCustomEntry.class : this.classEntry;
	}

	@Override
	public boolean isProperty() {
		return true;
	}

	@Override
	public Class getArrayEntryClass() {
		return this.classEntry;
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
		return null;
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
		return this.isArray;
	}

	@Override
	public boolean isListLengthFixed() {
		return property.isListLengthFixed();
	}

	@Override
	public int getMaxListLength() {
		return property.getMaxListLength();
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
		return this.defaultValues;
	}

	@Override
	public void setToDefault() {
		if (this.isArray) {
			this.values = Arrays.copyOf(this.defaultValues, this.defaultValues.length);
			
			if (this.defaultValue instanceof Json[]) {
				
				for (int i = 0; i < ((Json[])this.defaultValue).length; i++) {
					((Json[])this.values)[i] = (Json) ((Json[])this.defaultValues)[i].clone();
				}
			}
		} else {
			if (this.defaultValue instanceof Json) {
				this.value = ((Json)this.defaultValue).clone();
			} else {
				this.value = this.defaultValue;
			}
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
		
		Object[] list = Arrays.copyOf(this.values, this.values.length);
		
		if (this.values instanceof Json[]) {
			
			for (int i = 0; i < ((Json[])this.values).length; i++) {
				((Json[])list)[i] = (Json) ((Json[])this.values)[i].clone();
			}
		}
		return list;
	}

	@Override
	public void set(Object value) {
		this.value = value;
	}

	@Override
	public void set(Object[] aVal) {
		
		this.values = Arrays.copyOf(aVal, this.values.length);
		
		if (aVal instanceof Json[]) {
			
			for (int i = 0; i < ((Json[])this.values).length; i++) {
				((Json[])this.values)[i] = (Json) ((Json[])aVal)[i].clone();
			}
		}
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
