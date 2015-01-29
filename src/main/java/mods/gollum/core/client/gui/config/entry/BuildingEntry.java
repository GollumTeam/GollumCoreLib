package mods.gollum.core.client.gui.config.entry;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.Collection;

import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.JsonConfigProp;
import mods.gollum.core.common.config.type.BuildingConfigType;
import mods.gollum.core.common.config.type.BuildingConfigType.Group;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

public class BuildingEntry extends ConfigEntry {
	
	private BuildingConfigType value;
	
	private int currentGroup = 0;

	private ConfigEntry group;
	private ConfigEntry globalSpawnRate;
	
	public BuildingEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		this.value = (BuildingConfigType) configElement.getValue();
		
		parent.setSlotHeight (200);
		
		Object[] valuesO = this.value.lists.keySet().toArray();
		String[] valuesS = new String[valuesO.length];
		for (int i = 0; i < valuesO.length; i++) {
			valuesS[i] = (String)valuesO[i];
		}
		
		this.group = this.createSubEntry(
			"group",
			this.getCurrentGroupName(),
			this.getCurrentDefaultGroupName(),
			0,
			new JsonConfigProp().type(ConfigProp.Type.LIST_INLINE).validValues(valuesS)
		);
		this.group.btResetIsVisible = false;
		this.group.btUndoIsVisible  = false;
		this.group.formatedLabel    = false;
		
		final BuildingEntry _this = this;
		
		this.group.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				
				if (type == Type.CHANGE) {
					
					Group oldGroup = _this.getCurrentGroup();
					oldGroup.globalSpawnRate = (Integer) _this.globalSpawnRate.getValue();
					
					_this.setCurrentGroup((String) _this.group.getValue());
					Group groupC = _this.getCurrentGroup();
					Group groupD = _this.getCurrentDefaultGroup();
					Group groupO = _this.getCurrentOldGroup();
					_this.globalSpawnRate.setValue(groupC.globalSpawnRate);
					_this.globalSpawnRate.configElement.setValue(groupO.globalSpawnRate);
					_this.globalSpawnRate.configElement.setDefaultValue(groupD.globalSpawnRate);
				} 
			}
			
		});
		
		this.init ();
	}
	
	private void init() {
		
		this.globalSpawnRate = this.createSubEntry(
			"globalSpawnRate",
			this.getCurrentGroup().globalSpawnRate,
			this.getCurrentDefaultGroup().globalSpawnRate,
			1,
			new JsonConfigProp().type(ConfigProp.Type.SLIDER).minValue("0").maxValue("10")
		);
		this.globalSpawnRate.btResetIsVisible = true;
		this.globalSpawnRate.btUndoIsVisible  = true;
		
		final BuildingEntry _this = this;
		
		this.globalSpawnRate.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				Group group = _this.getCurrentGroup();
				group.globalSpawnRate = (Integer) _this.globalSpawnRate.getValue();
			}
			
		});
		
	}
	
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		
		this.group          .drawEntry(0, x, y     , listWidth, 22, tessellator, mouseX, mouseY, isSelected);
		this.globalSpawnRate.drawEntry(1, x, y + 22, listWidth, 22, tessellator, mouseX, mouseY, isSelected);
	}
	
	public void setCurrentGroup (String name) {
		int i = 0;
		for (String group : this.value.lists.keySet()) {
			if (group.equals(name)) {
				this.currentGroup = i;
				return;
			}
			i++;
		}
		return;
	}
	
	public String getCurrentGroupName () {
		return this.getCurrentGroupName(this.value.lists.keySet());
	}
	public String getCurrentDefaultGroupName () {
		return this.getCurrentGroupName(((BuildingConfigType)this.configElement.getValue()).lists.keySet());
	}
	public String getCurrentOldGroupName () {
		return this.getCurrentGroupName(((BuildingConfigType)this.configElement.getValue()).lists.keySet());
	}
	
	public String getCurrentGroupName (Collection<String> collection) {
		int i = 0;
		for (String group : ((BuildingConfigType)this.configElement.getValue()).lists.keySet()) {
			if (i == this.currentGroup) {
				return group;
			}
			i++;
		}
		return null;
	}
	
	public Group getCurrentGroup () {
		return this.getCurrentGroup(this.value.lists.values());
	}
	public Group getCurrentOldGroup () {
		return this.getCurrentGroup(((BuildingConfigType)this.configElement.getValue()).lists.values());
	}
	public Group getCurrentDefaultGroup () {
		return this.getCurrentGroup(((BuildingConfigType)this.configElement.getDefaultValue()).lists.values());
	}
	
	private Group getCurrentGroup (Collection<Group> collection) {
		int i = 0;
		for (Group group : collection) {
			if (i == this.currentGroup) {
				return group;
			}
			i++;
		}
		return null;
	}
	
	@Override
	public Object getValue() {
		super.getValue();
		return this.value;
	}

	@Override
	public ConfigEntry setValue(Object value) {
		if (value instanceof BuildingConfigType) {
			this.value = (BuildingConfigType) ((BuildingConfigType)value).clone();
		}
		return super.setValue(value);
	}
}
