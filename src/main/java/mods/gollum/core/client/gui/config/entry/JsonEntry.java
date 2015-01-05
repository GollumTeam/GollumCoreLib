package mods.gollum.core.client.gui.config.entry;

import java.lang.reflect.Array;
import java.util.Collection;

import cpw.mods.fml.client.config.HoverChecker;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.client.gui.config.element.TypedValueElement;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.JsonConfigProp;
import mods.gollum.core.tools.simplejson.IJsonComplement;
import mods.gollum.core.tools.simplejson.Json;
import mods.gollum.core.tools.simplejson.JsonArray;
import mods.gollum.core.tools.simplejson.JsonByte;
import mods.gollum.core.tools.simplejson.JsonDouble;
import mods.gollum.core.tools.simplejson.JsonFloat;
import mods.gollum.core.tools.simplejson.JsonInt;
import mods.gollum.core.tools.simplejson.JsonLong;
import mods.gollum.core.tools.simplejson.JsonShort;
import mods.gollum.core.tools.simplejson.JsonString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;

public class JsonEntry extends ConfigEntry {
	
	ConfigEntry proxy;
	
	public JsonEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		ConfigProp prop   = this.configElement.getConfigProp();
		
		init(this.configElement.getValue(), this.configElement.getDefaultValue(), prop);
	}
	
	protected void init(Object value, Object valueDefault, ConfigProp prop) {
		
		IJsonComplement newProp = ((Json)value).getComplement(JsonConfigProp.class);
		if (newProp != null) {
			prop = (ConfigProp) newProp;
		}
		
		if (((Json)value).isArray()) {
			
			Json[] json = new Json[((Json)value).size()];
			for (int i = 0; i < ((Json)value).size(); i++) {
				json[i] = ((Json)value).child(i);
			}
			Json[] jsonDefault = new Json[((Json)valueDefault).size()];
			for (int i = 0; i < ((Json)valueDefault).size(); i++) {
				jsonDefault[i] = ((Json)valueDefault).child(i);
			}
			
			TypedValueElement nConfigElement = new TypedValueElement(Json.class, this.getName(), json, jsonDefault, prop);
			
			this.proxy = new ArrayEntry(this.index, this.mc, this.parent, nConfigElement);
		
		} else if (((Json)value).isObject()) {
			
			TypedValueElement nConfigElement = new TypedValueElement(Json.class, this.getName(), value, valueDefault, prop);
			
			this.proxy = new JsonObjectEntry(this.index, this.mc, this.parent, nConfigElement);
		} else if (((Json)value).isString()) {
			
			JsonString json        = (JsonString)value;
			JsonString jsonDefault = (JsonString)valueDefault;
			
			TypedValueElement nConfigElement = new TypedValueElement(String.class, this.getName(), json.strValue(), jsonDefault.strValue(), prop);
			
			this.proxy = new StringEntry(this.index, this.mc, this.parent, nConfigElement);
		} else if (((Json)value).isLong()) {
			
			JsonLong json        = (JsonLong)value;
			JsonLong jsonDefault = (JsonLong)valueDefault;
			
			TypedValueElement nConfigElement = new TypedValueElement(Long.class, this.getName(), json.longValue(), jsonDefault.longValue(), prop);
			
			this.proxy = new LongEntry(this.index, this.mc, this.parent, nConfigElement);
		} else if (((Json)value).isInt()) {
			
			JsonInt json        = (JsonInt)value;
			JsonInt jsonDefault = (JsonInt)valueDefault;
			
			TypedValueElement nConfigElement = new TypedValueElement(Integer.class, this.getName(), json.intValue(), jsonDefault.intValue(), prop);
			
			this.proxy = new IntegerEntry(this.index, this.mc, this.parent, nConfigElement);
		} else if (((Json)value).isShort()) {
			
			JsonShort json        = (JsonShort)value;
			JsonShort jsonDefault = (JsonShort)valueDefault;
			
			TypedValueElement nConfigElement = new TypedValueElement(Short.class, this.getName(), json.shortValue(), jsonDefault.shortValue(), prop);
			
			this.proxy = new ShortEntry(this.index, this.mc, this.parent, nConfigElement);
		} else if (((Json)value).isByte()) {
			
			JsonByte json        = (JsonByte)value;
			JsonByte jsonDefault = (JsonByte)valueDefault;
			
			TypedValueElement nConfigElement = new TypedValueElement(Byte.class, this.getName(), json.byteValue(), jsonDefault.byteValue(), prop);
			
			this.proxy = new ByteEntry(this.index, this.mc, this.parent, nConfigElement);
		} else if (((Json)value).isDouble()) {
			
			JsonDouble json        = (JsonDouble)value;
			JsonDouble jsonDefault = (JsonDouble)valueDefault;
			
			TypedValueElement nConfigElement = new TypedValueElement(Double.class, this.getName(), json.doubleValue(), jsonDefault.doubleValue(), prop);
			
			this.proxy = new DoubleEntry(this.index, this.mc, this.parent, nConfigElement);
		} else if (((Json)value).isFloat()) {
			
			JsonFloat json        = (JsonFloat)value;
			JsonFloat jsonDefault = (JsonFloat)valueDefault;
			
			TypedValueElement nConfigElement = new TypedValueElement(Float.class, this.getName(), json.floatValue(), jsonDefault.floatValue(), prop);
			
			this.proxy = new FloatEntry(this.index, this.mc, this.parent, nConfigElement);
		}
	}
	
	protected Json getOldValue () {
		return (Json)this.configElement.getValue();
	}
	
	@Override
	public Object getValue() {
		
		Json oldValue = this.getOldValue();
		Object value = this.proxy.getValue();
		Json json = null;

		if (oldValue.isArray()) {
			json = new JsonArray();
			for (int i = 0; i < Array.getLength(value); i++) {
				json.add(Array.get(value, i));
			}
		} else if (oldValue.isObject()) {
			json = (Json) this.proxy.getValue();
		} else if (oldValue.isString()) {
			json = Json.create(value.toString());
		} else if (oldValue.isLong()) {
			json = Json.create(Long.parseLong (value.toString()));
		} else if (oldValue.isInt()) {
			json = Json.create(Integer.parseInt (value.toString()));
		} else if (oldValue.isShort()) {
			json = Json.create(Short.parseShort (value.toString()));
		} else if (oldValue.isByte()) {
			json = Json.create(Byte.parseByte (value.toString()));
		} else if (oldValue.isDouble()) {
			json = Json.create(Double.parseDouble (value.toString()));
		} else if (oldValue.isFloat()) {
			json = Json.create(Float.parseFloat (value.toString()));
		}
		
		return json;
	}
	
	@Override
	public ConfigEntry setValue(Object value) {
		Json oldValue = this.getOldValue();
		
		Object newValue = value;
		if (oldValue.isArray()) {
			if (value instanceof JsonArray) {
				JsonArray json = (JsonArray)value;
				newValue = new Json[json.size()];
				for (int i = 0; i < json.size(); i++) {
					((Json[])newValue)[i] = json.child(i);
				}
			}
		} else if (oldValue.isObject()) {
			
		} else if (oldValue.isString()) {
			if (value instanceof JsonString) { newValue = ((JsonString) value).strValue();    }
			if (value instanceof JsonLong)   { newValue = ((JsonLong)   value).longValue();   }
			if (value instanceof JsonInt)    { newValue = ((JsonInt)    value).intValue();    }
			if (value instanceof JsonShort)  { newValue = ((JsonShort)  value).shortValue();  }
			if (value instanceof JsonByte)   { newValue = ((JsonByte)   value).byteValue();   }
			if (value instanceof JsonDouble) { newValue = ((JsonDouble) value).doubleValue(); }
			if (value instanceof JsonFloat)  { newValue = ((JsonFloat)  value).floatValue();  }
		}
		this.proxy.setValue (newValue);
		return this;
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		this.proxy.drawEntry (slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected);
	}
	
	@Override
	public boolean isValidValue() {
		return this.proxy.isValidValue();
	}
	
	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		return this.proxy.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
	}

	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		this.proxy.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
	}
	
	public void keyTyped(char eventChar, int eventKey){
		this.proxy.keyTyped(eventChar, eventKey);
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		this.proxy.mouseClicked(mouseX, mouseY, mouseEvent);
	}
	
	public void updateCursorCounter() {
		this.proxy.updateCursorCounter();
	}
	
	public void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY) {
		this.proxy.elementClicked(slotIndex, doubleClick, mouseX, mouseY);
	}

	public void setSlot(int slotIndex) {
		this.proxy.setSlot(slotIndex);
	}
	
	public void drawToolTip(int mouseX, int mouseY) {
		this.proxy.drawToolTip(mouseX, mouseY);
	}
}
