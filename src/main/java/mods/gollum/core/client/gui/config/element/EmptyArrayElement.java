package mods.gollum.core.client.gui.config.element;

import java.util.List;
import java.util.regex.Pattern;

import cpw.mods.fml.client.config.ConfigGuiType;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.IConfigElement;

public class EmptyArrayElement implements IConfigElement {
	
	
	public EmptyArrayElement() {
		
	}
	
	@Override
	public Class<? extends IConfigEntry> getConfigEntryClass() {
		return null;
	}

	@Override
	public boolean isProperty() {
		return true;
	}

	@Override
	public Class getArrayEntryClass() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getQualifiedName() {
		return "";
	}

	@Override
	public String getLanguageKey() {
		return "";
	}

	@Override
	public String getComment() {
		return "";
	}

	@Override
	public List getChildElements() {
		return null;
	}

	@Override
	public ConfigGuiType getType() {
		return ConfigGuiType.STRING
		;
	}
	

	@Override
	public boolean isList() {
		return true;
	}

	@Override
	public boolean isListLengthFixed() {
		return false;
	}

	@Override
	public int getMaxListLength() {
		return 0;
	}

	@Override
	public boolean isDefault() {
		return true;
	}

	@Override
	public Object getDefault() {
		return null;
	}

	@Override
	public Object[] getDefaults() {
		return new Object[0];
	}

	@Override
	public void setToDefault() {
	}

	@Override
	public boolean requiresWorldRestart() {
		return false;
	}

	@Override
	public boolean showInGui() {
		return true;
	}

	@Override
	public boolean requiresMcRestart() {
		return false;
	}

	@Override
	public Object get() {
		return null;
	}

	@Override
	public Object[] getList() {
		return new Object[0];
	}

	@Override
	public void set(Object value) {
	}

	@Override
	public void set(Object[] aVal) {
	}

	@Override
	public String[] getValidValues() {
		return new String[0];
	}

	@Override
	public Object getMinValue() {
		return 0;
	}

	@Override
	public Object getMaxValue() {
		return 0;
	}

	@Override
	public Pattern getValidationPattern() {
		return null;
	}

}
