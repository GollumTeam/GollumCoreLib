package mods.gollum.core.client.gui.config.element;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import mods.gollum.core.client.gui.config.entry.ArrayEntry;
import mods.gollum.core.client.gui.config.entry.DoubleEntry;
import mods.gollum.core.client.gui.config.entry.IntegerEntry;
import mods.gollum.core.client.gui.config.entry.StringEntry;
import mods.gollum.core.client.gui.config.properties.GollumProperty;
import mods.gollum.core.common.config.type.IConfigJsonType;
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
	
	public CustomElement(Class< ? extends IConfigEntry> classEntry, GollumProperty property) {
		super();
		
		this.property     = property;
		this.classEntry   = classEntry;
		this.set(property.getString());
		
	}
	
	public CustomElement(Class< ? extends IConfigEntry> classEntry, GollumProperty property, Object[] values, Object[] defaultValues) {
		this.property      = property;
		this.classEntry    = classEntry;
		this.values        = values;
		for (int i = 0; i < this.values.length; i++) {
			this.values[i] = this.formatValue(this.values[i]);
		}
		this.defaultValues = defaultValues;
		for (int i = 0; i < this.values.length; i++) {
			if (i < this.defaultValues.length) {
				this.defaultValues[i] = this.formatValue(this.defaultValues[i]);
			}
		}
	}
	
	public CustomElement(Class< ? extends IConfigEntry> classEntry, GollumProperty property, Object value, Object defaultValue) {
		
		this.property     = property;
		this.classEntry   = classEntry;
		this.value        = this.formatValue (value);
		this.defaultValue = this.formatValue (defaultValue);
	}
	
	private Object formatValue(Object value) {
		
		if (this.getType() == ConfigGuiType.INTEGER) { try { value = Integer.parseInt    (value+""); } catch (Exception e) { value = 0;     }}
		if (this.getType() == ConfigGuiType.DOUBLE)  { try { value = Double .parseDouble (value+""); } catch (Exception e) { value = 0;     }}
		if (this.getType() == ConfigGuiType.BOOLEAN) { try { value = Boolean.parseBoolean(value+""); } catch (Exception e) { value = false; }}
		if (this.getType() == ConfigGuiType.STRING)  { value = value+""; }
		if (value instanceof Json) { value = (Json) ((Json)value).clone(); }
		
		return value;
	}
	
	@Override
	public Class<? extends IConfigEntry> getConfigEntryClass() {
		return (this.isList()) ? ArrayEntry.class : this.classEntry;
	}
	
	@Override
	public Class getArrayEntryClass() {
		return this.classEntry;
	}
	
	@Override
	public boolean isProperty() {
		return true;
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
			property.getType() == Property.Type.MOD_ID  ? ConfigGuiType.MOD_ID  : 
			ConfigGuiType.STRING
		;
	}
	
	@Override
	public boolean isList() {
		return this.property.isList();
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

		if (this.getType() == ConfigGuiType.BOOLEAN) { try { return Boolean.parseBoolean(this.defaultValue+""); } catch (Exception e) {} return false; }
		if (this.getType() == ConfigGuiType.DOUBLE)  { try { return Double .parseDouble (this.defaultValue+""); } catch (Exception e) {} return 0; }
		if (this.getType() == ConfigGuiType.INTEGER) { try { return Integer.parseInt    (this.defaultValue+""); } catch (Exception e) {} return 0; }
		
		return this.defaultValue != null ? this.defaultValue : this.getNullElement ();
	}

	@Override
	public Object[] getDefaults() {
		return this.defaultValues;
	}

	@Override
	public void setToDefault() {
		if (this.isList()) {
			this.values = Arrays.copyOf(this.defaultValues, this.defaultValues.length);
			
			if (this.defaultValue instanceof Json[]) {
				
				for (int i = 0; i < ((Json[])this.defaultValue).length; i++) {
					((Json[])this.values)[i] = (Json) ((Json[])this.defaultValues)[i].clone();
				}
			}
			if (this.defaultValue instanceof IConfigJsonType[]) {
				
				for (int i = 0; i < ((IConfigJsonType[])this.defaultValue).length; i++) {
					try {
						IConfigJsonType tmp = (IConfigJsonType) ((IConfigJsonType[])this.defaultValue)[i].getClass().newInstance();
						tmp.readConfig(((IConfigJsonType)((IConfigJsonType[])this.defaultValue)[i]).writeConfig());
						
						((IConfigJsonType[])this.values)[i] = tmp;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		} else {
			if (this.defaultValue instanceof Json) {
				this.value = ((Json)this.defaultValue).clone();
			} else
			if (this.defaultValue instanceof IConfigJsonType) {
				try {
					IConfigJsonType tmp = (IConfigJsonType)this.defaultValue.getClass().newInstance();
					tmp.readConfig(((IConfigJsonType)this.defaultValue).writeConfig());
					this.value = tmp;
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		return this.value != null ? this.value : this.getNullElement ();
	}

	private Object getNullElement() {
		if (this.getType() == ConfigGuiType.BOOLEAN) { return false; }
		if (this.getType() == ConfigGuiType.DOUBLE)  { return 0; }
		if (this.getType() == ConfigGuiType.INTEGER) { return 0; }
		return "";
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
		this.value = this.formatValue(value);
	}

	@Override
	public void set(Object[] aVal) {
		
		this.values = Arrays.copyOf(aVal, aVal.length);
		for (int i = 0; i < this.values.length; i++) {
			this.values[i] = this.formatValue(this.values[i]);
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
