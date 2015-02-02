package mods.gollum.core.client.gui.config.entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.JsonConfigProp;
import mods.gollum.core.common.config.type.BuildingConfigType;
import mods.gollum.core.common.config.type.BuildingConfigType.Group;
import mods.gollum.core.common.config.type.BuildingConfigType.Group.Building;
import mods.gollum.core.common.config.type.BuildingConfigType.Group.Building.Dimention;
import mods.gollum.core.tools.registered.RegisteredObjects;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;

public class BuildingEntry extends ConfigEntry {
	
	private BuildingConfigType value;
	
	private int     currentGroup     = 0;
	private int     currentBuilding  = 0;
	private Integer currentDimention = null;

	private ConfigEntry group;
	private ConfigEntry globalSpawnRate;
	private ConfigEntry building;
	private ConfigEntry bDisabled;
	private ConfigEntry bDimentionList;
	private ConfigEntry dSpawnRate;
	private ConfigEntry dSpawnHeight;
	private ConfigEntry dBlocksSpawn;
	
	private boolean mutexChange = true;
	
	public BuildingEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		this.value = (BuildingConfigType) configElement.getValue();
		
		parent.setSlotHeight (175);
		
		this.init ();
	}
	
	private void init() {
		
		final BuildingEntry _this = this;
		
		Object[] valuesGO = this.value.lists.keySet().toArray();
		String[] valuesGS = new String[valuesGO.length];
		for (int i = 0; i < valuesGO.length; i++) {
			valuesGS[i] = (String)valuesGO[i];
		}
		
		this.group = this.createSubEntry(
			"group",
			this.getCurrentGroupName(),
			this.getCurrentDefaultGroupName(),
			0,
			new JsonConfigProp().type(ConfigProp.Type.LIST_INLINE).validValues(valuesGS)
		);
		this.group.btResetIsVisible = false;
		this.group.btUndoIsVisible  = false;
		this.group.formatedLabel    = false;
			
		this.group.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (type == Type.CHANGE) {
					if (_this.mutexChange()) {
						_this.saveEntry();
						_this.setGroup();
						_this.setBuilding();
						_this.mutexChange = true;
					}
				} 
			}
			
		});
		
		this.globalSpawnRate = this.createSubEntry(
			"globalSpawnRate",
			this.getCurrentGroup().globalSpawnRate,
			this.getCurrentDefaultGroup().globalSpawnRate,
			1,
			new JsonConfigProp().type(ConfigProp.Type.SLIDER).minValue("0").maxValue("10")
		);
		this.globalSpawnRate.btResetIsVisible = true;
		this.globalSpawnRate.btUndoIsVisible  = true;
		
		this.globalSpawnRate.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (_this.mutexChange()) {
					_this.saveEntry();
					_this.mutexChange = true;
				}
			}
			
		});
		
		Group group = this.getCurrentGroup();
		
		Object[] valuesBO = group.buildings.keySet().toArray();
		String[] valuesBS = new String[valuesBO.length];
		for (int i = 0; i < valuesBO.length; i++) {
			valuesBS[i] = (String)valuesBO[i];
		}
		
		this.building = this.createSubEntry(
			"building",
			this.getCurrentBuildingName(),
			this.getCurrentBuildingGroupName(),
			2,
			new JsonConfigProp().type(ConfigProp.Type.LIST_INLINE).validValues(valuesBS)
		);
		this.building.btResetIsVisible = false;
		this.building.btUndoIsVisible  = false;
		this.building.formatedLabel    = false;
				
		this.building.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (type == Type.CHANGE) {
					if (_this.mutexChange()) {
						_this.saveEntry();
						_this.setBuilding();
						_this.mutexChange = true;
					}
				} 
			}
			
		});
		
		Building buildingC = this.getCurrentBuilding();
		Building buildingD = this.getCurrentDefaultBuilding();
		
		this.bDisabled = this.createSubEntry(
			"disabled",
			buildingC.disabled,
			buildingD.disabled,
			3,
			new JsonConfigProp()
		);
		
		this.building.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (_this.mutexChange()) {
					_this.saveEntry();
					_this.mutexChange = true;
				}
			}
			
		});

		Set<Integer> dimentionsC = buildingC.dimentions.keySet();
		Set<Integer> dimentionsD = buildingD.dimentions.keySet();
		
		String[] dimCStr = new String[dimentionsC.size()];
		for (int i = 0; i < dimentionsC.size(); i++) {
			dimCStr[i] = dimentionsC.toArray()[i].toString();
		}
		String[] dimDStr = new String[dimentionsD.size()];
		for (int i = 0; i < dimentionsD.size(); i++) {
			dimDStr[i] = dimentionsD.toArray()[i].toString();
		}
		
		this.bDimentionList = this.createSubEntry(
			"dimention",
			dimCStr,
			dimDStr,
			4,
			new JsonConfigProp().entryClass(BuildingEntryTab.class.getCanonicalName())
		);
		
		Dimention dimentionC = this.getCurrentDimention();
		Dimention dimentionD = this.getCurrentDefaultDimention();

		dimentionC = dimentionC != null ? dimentionC : new Dimention();
		dimentionD = dimentionD != null ? dimentionD : new Dimention();
		
		this.dSpawnRate = this.createSubEntry(
			"spawnRate",
			dimentionC.spawnRate,
			dimentionD.spawnRate,
			5,
			new JsonConfigProp().type(ConfigProp.Type.SLIDER).minValue("0").maxValue("50")
		);
		this.dSpawnHeight = this.createSubEntry(
			"spawnHeight",
			dimentionC.spawnHeight,
			dimentionD.spawnHeight,
			6,
			new JsonConfigProp().type(ConfigProp.Type.SLIDER).minValue("1").maxValue("256")
		);

		ArrayList<String> blocksC = new ArrayList<String>();
		for (Block block : dimentionC.blocksSpawn) {
			String name = RegisteredObjects.instance().getRegisterName(block);
			if (name != null) {
				blocksC.add(name);
			}
		}
		ArrayList<String> blocksD = new ArrayList<String>();
		for (Block block : dimentionD.blocksSpawn) {
			String name = RegisteredObjects.instance().getRegisterName(block);
			if (name != null) {
				blocksD.add(name);
			}
		}
		
		this.dBlocksSpawn = this.createSubEntry(
			"blocksSpawn",
			blocksC.toArray(new String[0]),
			blocksD.toArray(new String[0]),
			7,
			new JsonConfigProp().type(ConfigProp.Type.BLOCK).newValue(RegisteredObjects.instance().getRegisterName(Blocks.grass))
		);
		
	}
	
	private boolean mutexChange () {
		boolean m = this.mutexChange;
		this.mutexChange = false;
		return m;
	}
	
	public void saveEntry () {

		Group    group    = this.getCurrentGroup();
		Building building = this.getCurrentBuilding();
		
		group.globalSpawnRate = (Integer) this.globalSpawnRate.getValue();
		building.disabled     = (Boolean) this.bDisabled.getValue();
		
	}

	public void setGroup () {
		
		this.setCurrentGroup((String) this.group.getValue());
		
		Group group = this.getCurrentGroup();
		Group groupC = this.getCurrentGroup();
		Group groupD = this.getCurrentDefaultGroup();
		Group groupO = this.getCurrentOldGroup();
			
		this.globalSpawnRate.setValue(groupC.globalSpawnRate);
		this.globalSpawnRate.configElement.setValue(groupO.globalSpawnRate);
		this.globalSpawnRate.configElement.setDefaultValue(groupD.globalSpawnRate);
		
		Object[] valuesBO = groupC.buildings.keySet().toArray();
		String[] valuesBS = new String[valuesBO.length];
		for (int i = 0; i < valuesBO.length; i++) {
			valuesBS[i] = (String)valuesBO[i];
		}
		
		((ListInlineEntry)this.building).values = valuesBS;
		this.currentBuilding = 0;
		
	}
	
	public void setBuilding () {
		
		this.setCurrentBuilding((String) this.building.getValue());
		
		Group groupC = this.getCurrentGroup();
		Group groupD = this.getCurrentDefaultGroup();
		Group groupO = this.getCurrentOldGroup();
		
		this.building.setValue(this.getCurrentBuildingName());
		this.building.configElement.setValue(groupO.globalSpawnRate);
		this.building.configElement.setDefaultValue(groupD.globalSpawnRate);
		
		Building buildingC = this.getCurrentBuilding();
		Building buildingD = this.getCurrentDefaultBuilding();
		Building buildingO = this.getCurrentOldBuilding();
		
		this.bDisabled.setValue(buildingC.disabled);
		this.bDisabled.configElement.setValue(buildingO.disabled);
		this.bDisabled.configElement.setDefaultValue(buildingD.disabled);
		
		this.setValue(this.getValue());
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth) {
		
		this.parent.controlWidth -= 7;
		
		this.parent.labelX -= 10;
		this.group.drawEntry(0, x, y     , listWidth, 22, tessellator, mouseX, mouseY, isSelected, false);
		this.parent.labelX += 10;
		
		this.drawRec(this.parent.labelX-10, y + 20, this.parent.scrollBarX - this.parent.labelX + 7, 200, 0x0EFFFFFF);
		
		this.globalSpawnRate.drawEntry(1, x, y + 24, listWidth, 22, tessellator, mouseX, mouseY, isSelected, false);
		this.building       .drawEntry(2, x, y + 46, listWidth, 22, tessellator, mouseX, mouseY, isSelected, false);
		
		this.drawRec(this.parent.labelX, y + 68, this.parent.scrollBarX - this.parent.labelX - 3, 152, 0x0EFFFFFF);
		
		this.parent.labelX += 10;
		
		this.bDisabled     .drawEntry(3, x, y + 72, listWidth, 22, tessellator, mouseX, mouseY, isSelected, false);
		this.bDimentionList.drawEntry(4, x, y + 92, listWidth, 22, tessellator, mouseX, mouseY, isSelected, false);
		
		this.drawRec(this.parent.labelX, y + 111, this.parent.scrollBarX - this.parent.labelX - 3, 109, 0x0EFFFFFF);
		
		this.parent.labelX += 10;
		
		this.dSpawnRate  .drawEntry(4, x, y + 112, listWidth, 22, tessellator, mouseX, mouseY, isSelected, false);
		this.dSpawnHeight.drawEntry(4, x, y + 132, listWidth, 22, tessellator, mouseX, mouseY, isSelected, false);
		this.dBlocksSpawn.drawEntry(4, x, y + 152, listWidth, 22, tessellator, mouseX, mouseY, isSelected);
		
		this.resetControlWidth();
		
		this.parent.controlWidth += 3;
	}
	
	@Override
	public int getLabelWidth() {
		if (!this.getLabelDisplay()) {
			return 0;
		}
		
		int[] sizes = new int[] {
			mc.fontRenderer.getStringWidth(this.tradIfExist("group"))-10,
			mc.fontRenderer.getStringWidth(this.tradIfExist("globalSpawnRate")),
			mc.fontRenderer.getStringWidth(this.tradIfExist("building"))+10,
			mc.fontRenderer.getStringWidth(this.tradIfExist("disabled"))+10,
			mc.fontRenderer.getStringWidth(this.tradIfExist("dimention"))+10,
		};
		
		int size = 0;
		for (int s : sizes) {
			size = Math.max(size, s);
		}
		
		return size;
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
	
	public void setCurrentBuilding (String name) {
		int i = 0;
		for (String building : this.getCurrentGroup().buildings.keySet()) {
			if (building.equals(name)) {
				this.currentBuilding = i;
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
		return this.getCurrentGroupName(((BuildingConfigType)this.configElement.getDefaultValue()).lists.keySet());
	}
	public String getCurrentOldGroupName () {
		return this.getCurrentGroupName(((BuildingConfigType)this.configElement.getValue()).lists.keySet());
	}
	private String getCurrentGroupName (Collection<String> collection) {
		int i = 0;
		for (String group : collection) {
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
	public Group getCurrentDefaultGroup () {
		return this.getCurrentGroup(((BuildingConfigType)this.configElement.getDefaultValue()).lists.values());
	}
	public Group getCurrentOldGroup () {
		return this.getCurrentGroup(((BuildingConfigType)this.configElement.getValue()).lists.values());
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
	
	public String getCurrentBuildingName () {
		return this.getCurrentBuildingName(this.getCurrentGroup().buildings.keySet());
	}
	public String getCurrentBuildingGroupName () {
		return this.getCurrentBuildingName(this.getCurrentDefaultGroup().buildings.keySet());
	}
	public String getCurrentOldBuildingName () {
		return this.getCurrentBuildingName(this.getCurrentOldGroup().buildings.keySet());
	}
	public String getCurrentBuildingName (Collection<String> collection) {
		int i = 0;
		for (String building : collection) {
			if (i == this.currentBuilding) {
				return building;
			}
			i++;
		}
		return "";
	}
	
	public Building getCurrentBuilding () {
		return this.getCurrentBuilding(this.getCurrentGroup().buildings.values());
	}
	public Building getCurrentDefaultBuilding () {
		return this.getCurrentBuilding(this.getCurrentDefaultGroup().buildings.values());
	}
	public Building getCurrentOldBuilding () {
		return this.getCurrentBuilding(this.getCurrentOldGroup().buildings.values());
	}
	private Building getCurrentBuilding (Collection<Building> collection) {
		int i = 0;
		for (Building building : collection) {
			if (i == this.currentBuilding) {
				return building;
			}
			i++;
		}
		return null;
	}
	
	public Dimention getCurrentDimention () {
		return this.getCurrentDimention(this.getCurrentBuilding().dimentions.entrySet());
	}
	public Dimention getCurrentDefaultDimention () {
		return this.getCurrentDimention(this.getCurrentDefaultBuilding().dimentions.entrySet());
	}
	public Dimention getCurrentOldDimention () {
		return this.getCurrentDimention(this.getCurrentOldBuilding().dimentions.entrySet());
	}
	private Dimention getCurrentDimention (Set<Entry<Integer, Dimention>> collection) {
		for (Entry<Integer, Dimention> entry : collection) {
			if (entry.getKey().equals(this.currentDimention)) {
				return entry.getValue();
			}
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
