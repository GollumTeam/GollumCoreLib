package com.gollum.core.client.gui.config.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.gollum.core.client.gui.config.GuiConfigEntries;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.client.gui.config.entry.ConfigEntry.Event.Type;
import com.gollum.core.common.config.ConfigProp;
import com.gollum.core.common.config.JsonConfigProp;
import com.gollum.core.common.config.type.BuildingConfigType;
import com.gollum.core.common.config.type.BuildingConfigType.Group;
import com.gollum.core.common.config.type.BuildingConfigType.Group.Building;
import com.gollum.core.common.config.type.BuildingConfigType.Group.Building.Dimention;
import com.gollum.core.tools.registered.RegisteredObjects;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class BuildingEntry extends ConfigEntry {
	
	private BuildingConfigType value;
	
	private int     currentGroup     = 0;
	private int     currentBuilding  = 0;
	private Integer currentDimention = null;

	private ConfigEntry group;
	private ConfigEntry globalSpawnRate;
	private ConfigEntry building;
	private ConfigEntry bEnabled;
	private ConfigEntry bDimentionList;
	private ConfigEntry dSpawnRate;
	private ConfigEntry dSpawnMin;;
	private ConfigEntry dSpawnMax;
	private ConfigEntry dBlocksSpawn;
	private ConfigEntry dBiomes;

	private boolean mutexChangeEvent = true;
	private boolean mutexChange = true;
	
	public BuildingEntry(int index, Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		this.value = (BuildingConfigType) configElement.getValue();
		
		parent.setSlotHeight (220);
		
		this.init ();
	}
	
	private void init() {
		
		final BuildingEntry _this = this;
		
		///////////
		// GROUP //
		///////////
		
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
					if (_this.mutexChangeEvent()) {
						_this.saveEntry();
						_this.setGroup();
						_this.mutexChangeEvent = true;
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
				if (_this.mutexChangeEvent()) {
					_this.saveEntry();
					_this.mutexChangeEvent = true;
				}
			}
			
		});
		
		//////////////
		// BUILDING //
		//////////////
		
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
					if (_this.mutexChangeEvent()) {
						_this.saveEntry();
						_this.setBuilding();
						_this.mutexChangeEvent = true;
					}
				} 
			}
			
		});
		
		Building buildingC = this.getCurrentBuilding();
		Building buildingD = this.getCurrentDefaultBuilding();
		
		this.bEnabled = this.createSubEntry(
			"enabled",
			buildingC.enabled,
			buildingD.enabled,
			3,
			new JsonConfigProp()
		);
		
		this.bEnabled.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (_this.mutexChangeEvent()) {
					_this.saveEntry();
					_this.mutexChangeEvent = true;
				}
			}
			
		});

		///////////////
		// DIMENTION //
		///////////////
		
		Set<Integer> dimentionsC = buildingC.dimentions.keySet();
		Set<Integer> dimentionsD = buildingD.dimentions.keySet();
		

		String[] dimC = new String[buildingC.dimentions.size()];
		String[] dimD = new String[buildingD.dimentions.size()];
		for (int i = 0; i < dimC.length; i++) {
			dimC[i] = buildingC.dimentions.keySet().toArray()[i].toString();
		}
		for (int i = 0; i < dimD.length; i++) {
			dimD[i] = buildingD.dimentions.keySet().toArray()[i].toString();
		}
		
		this.bDimentionList = this.createSubEntry(
			"dimention",
			dimC,
			dimD,
			4,
			new JsonConfigProp().entryClass(BuildingEntryTab.class.getCanonicalName())
		);
		
		this.setCurrentDimention(((BuildingEntryTab)this.bDimentionList).getSelected());
		
		this.bDimentionList.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (type == Type.CHANGE) {
					if (_this.mutexChangeEvent()) {
						_this.saveEntry();
						_this.setDimention();
						_this.mutexChangeEvent = true;
					}
				} 
			}
			
		});
		
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
		this.dSpawnRate.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (_this.mutexChangeEvent()) {
					_this.saveEntry();
					_this.mutexChangeEvent = true;
				}
			}
			
		});
		
		this.dSpawnMin = this.createSubEntry(
			"spawnMin",
			dimentionC.spawnMin,
			dimentionD.spawnMin,
			6,
			new JsonConfigProp().type(ConfigProp.Type.SLIDER).minValue("1").maxValue("256")
		);
		this.dSpawnMin.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (_this.mutexChangeEvent()) {
					_this.saveEntry();
					_this.mutexChangeEvent = true;
				}
			}
			
		});
		
		this.dSpawnMax = this.createSubEntry(
			"spawnMax",
			dimentionC.spawnMax,
			dimentionD.spawnMax,
			6,
			new JsonConfigProp().type(ConfigProp.Type.SLIDER).minValue("1").maxValue("256")
		);
		this.dSpawnMax.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (_this.mutexChangeEvent()) {
					_this.saveEntry();
					_this.mutexChangeEvent = true;
				}
			}
			
		});
		
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
			new JsonConfigProp().type(ConfigProp.Type.BLOCK).newValue(RegisteredObjects.instance().getRegisterName(Blocks.GRASS))
		);
		this.dBlocksSpawn.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (_this.mutexChangeEvent()) {
					_this.saveEntry();
					_this.mutexChangeEvent = true;
				}
			}
			
		});
		
		ArrayList<String> biomesC = new ArrayList<String>();
		for (Biome biome : dimentionC.biomes) {
			biomesC.add(RegisteredObjects.instance().getRegisterName(biome));
		}
		ArrayList<String> biomesD = new ArrayList<String>();
		for (Biome biome : dimentionD.biomes) {
			biomesD.add(RegisteredObjects.instance().getRegisterName(biome));
		}
		
		this.dBiomes = this.createSubEntry(
			"biomesFilter",
			biomesC.toArray(new String[0]),
			biomesD.toArray(new String[0]),
			7,
			new JsonConfigProp().type(ConfigProp.Type.BIOME).newValue(RegisteredObjects.instance().getRegisterName(Biomes.OCEAN))
		);
		this.dBiomes.addEvent(new Event() {
			
			@Override
			public void call(Type type, Object... params) {
				if (_this.mutexChangeEvent()) {
					_this.saveEntry();
					_this.mutexChangeEvent = true;
				}
			}
			
		});
	}

	private boolean mutexChangeEvent () {
		boolean m = this.mutexChangeEvent;
		this.mutexChangeEvent = false;
		return m;
	}
	private boolean mutexChange () {
		boolean m = this.mutexChange;
		this.mutexChange = false;
		return m;
	}
	
	public void saveEntry () {
		
		if (this.mutexChange()) {
			
			Group    group      = this.getCurrentGroup();
			Building building   = this.getCurrentBuilding();
			Dimention dimentionC = this.getCurrentDimention();
			
			group.globalSpawnRate = (Integer) this.globalSpawnRate.getValue();
			building.enabled     = (Boolean) this.bEnabled.getValue();
			
			List<String> dimentionKeys = Arrays.asList((String[])this.bDimentionList.getValue());
			
			for (Entry<Integer, Dimention> entry : new TreeMap<Integer, Dimention> (building.dimentions).entrySet()) {
				if (!dimentionKeys.contains(entry.getKey().toString())) {
					Dimention dimention = building.dimentions.remove(entry.getKey());
				}
			}
			for (String key : dimentionKeys) {
				Integer keyInt = 0;
				try {
					keyInt = Integer.parseInt(key);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!building.dimentions.containsKey(keyInt)) {
					Dimention dimention = building.dimentions.put(keyInt, new Dimention());
				}
			}
			
			if (dimentionC != null) {
				dimentionC.spawnRate   = (Integer) this.dSpawnRate.getValue();
				dimentionC.spawnMin = (Integer) this.dSpawnMin.getValue();
				dimentionC.spawnMax = (Integer) this.dSpawnMax.getValue();

				ArrayList<Block> blocksSpawn = new ArrayList<Block>();
				for (String blockName : (String[])this.dBlocksSpawn.getValue()) {
					Block b = RegisteredObjects.instance().getBlock(blockName);
					if (b != null) {
						blocksSpawn.add(b);
					}
				}
				
				dimentionC.blocksSpawn = blocksSpawn;
				

				ArrayList<Biome> biomes = new ArrayList<Biome>();
				for (String biomeRName : (String[])this.dBiomes.getValue()) {
					Biome b = RegisteredObjects.instance().getBiome(biomeRName);
					if (b != null) {
						biomes.add(b);
					}
				}
				
				dimentionC.biomes = biomes;
			}
			
			this.fireEvent(Type.CHANGE);
			this.mutexChange = true;
		}
		
	}

	public void setGroup () {
		
		if (this.mutexChange()) {
			
			this.setCurrentGroup((String) this.group.getValue());
			
			Group groupC = this.getCurrentGroup();
			Group groupO = this.getCurrentOldGroup();
			Group groupD = this.getCurrentDefaultGroup();
				
			this.globalSpawnRate              .setValue       (groupC.globalSpawnRate);
			this.globalSpawnRate.configElement.setValue       (groupO.globalSpawnRate);
			this.globalSpawnRate.configElement.setDefaultValue(groupD.globalSpawnRate);
			
			Object[] valuesBO = groupC.buildings.keySet().toArray();
			String[] valuesBS = new String[valuesBO.length];
			for (int i = 0; i < valuesBO.length; i++) {
				valuesBS[i] = (String)valuesBO[i];
			}
			
			((ListInlineEntry)this.building).values = valuesBS;
			if (valuesBS.length != 0) {
				this.building.setValue(valuesBS[0]);
			} else {
				this.building.setValue(null);
			}
			
			this.currentBuilding  = 0;
			
			this.fireEvent(Type.CHANGE);

			this.mutexChange = true;
		}
		this.setBuilding ();
	}
	
	public void setBuilding () {

		if (this.mutexChange()) {
			
			this.setCurrentBuilding((String) this.building.getValue());
			
			Building buildingC = this.getCurrentBuilding();
			Building buildingO = this.getCurrentOldBuilding();
			Building buildingD = this.getCurrentDefaultBuilding();
			
			this.bEnabled              .setValue       (buildingC.enabled);
			this.bEnabled.configElement.setValue       (buildingO.enabled);
			this.bEnabled.configElement.setDefaultValue(buildingD.enabled);
			
			String[] dimC = new String[buildingC.dimentions.size()];
			String[] dimO = new String[buildingO.dimentions.size()];
			String[] dimD = new String[buildingD.dimentions.size()];
			
			for (int i = 0; i < dimC.length; i++) {
				dimC[i] = buildingC.dimentions.keySet().toArray()[i].toString();
			}
			for (int i = 0; i < dimO.length; i++) {
				dimO[i] = buildingO.dimentions.keySet().toArray()[i].toString();
			}
			for (int i = 0; i < dimD.length; i++) {
				dimD[i] = buildingD.dimentions.keySet().toArray()[i].toString();
			}
			
			this.bDimentionList              .setValue       (dimC);
			this.bDimentionList.configElement.setValue       (dimO);
			this.bDimentionList.configElement.setDefaultValue(dimD);
			((BuildingEntryTab)this.bDimentionList).setIndex0();
			
			this.fireEvent(Type.CHANGE);
			
			this.mutexChange = true;
		}
		
		this.setDimention ();
	}

	public void setDimention () {
		
		if (this.mutexChange()) {
			
			this.setCurrentDimention(((BuildingEntryTab)this.bDimentionList).getSelected());
			
			Dimention dimentionC = this.getCurrentDimention();
			Dimention dimentionO = this.getCurrentOldDimention();
			Dimention dimentionD = this.getCurrentDefaultDimention();
			
			dimentionC = dimentionC != null ? dimentionC : new Dimention();
			dimentionD = dimentionD != null ? dimentionD : new Dimention();
			dimentionO = dimentionO != null ? dimentionO : new Dimention();
	
			this.dSpawnRate              .setValue       (dimentionC.spawnRate);
			this.dSpawnRate.configElement.setValue       (dimentionO.spawnRate);
			this.dSpawnRate.configElement.setDefaultValue(dimentionD.spawnRate);
	
			this.dSpawnMin              .setValue       (dimentionC.spawnMin);
			this.dSpawnMin.configElement.setValue       (dimentionO.spawnMin);
			this.dSpawnMin.configElement.setDefaultValue(dimentionD.spawnMin);
	
			this.dSpawnMax              .setValue       (dimentionC.spawnMax);
			this.dSpawnMax.configElement.setValue       (dimentionO.spawnMax);
			this.dSpawnMax.configElement.setDefaultValue(dimentionD.spawnMax);
			
			
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
			ArrayList<String> blocksO = new ArrayList<String>();
			for (Block block : dimentionO.blocksSpawn) {
				String name = RegisteredObjects.instance().getRegisterName(block);
				if (name != null) {
					blocksO.add(name);
				}
			}
			
			this.dBlocksSpawn              .setValue       (blocksC.toArray(new String[0]));
			this.dBlocksSpawn.configElement.setValue       (blocksO.toArray(new String[0]));
			this.dBlocksSpawn.configElement.setDefaultValue(blocksD.toArray(new String[0]));
			
			
			ArrayList<String> biomesC = new ArrayList<String>();
			for (Biome biome: dimentionC.biomes) {
				blocksC.add(RegisteredObjects.instance().getRegisterName(biome));
			}
			ArrayList<String> biomesD = new ArrayList<String>();
			for (Biome biome : dimentionD.biomes) {
				biomesD.add(RegisteredObjects.instance().getRegisterName(biome));
			}
			ArrayList<String> biomesO = new ArrayList<String>();
			for (Biome biome : dimentionO.biomes) {
				biomesO.add(RegisteredObjects.instance().getRegisterName(biome));
			}
			
			this.dBiomes              .setValue       (biomesC.toArray(new String[0]));
			this.dBiomes.configElement.setValue       (biomesO.toArray(new String[0]));
			this.dBiomes.configElement.setDefaultValue(biomesD.toArray(new String[0]));
			
			this.fireEvent(Type.CHANGE);

			this.mutexChange = true;
		}
	}
	
	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth, float partial) {
		
		this.parent.controlWidth -= 7;
		
		this.parent.labelX -= 10;
		this.group.drawEntry(0, x, y     , listWidth, 22, mouseX, mouseY, isSelected, false, partial);
		this.parent.labelX += 10;
		
		this.drawRec(this.parent.labelX-10, y + 20, this.parent.scrollBarX - this.parent.labelX + 7, 200, 0x08FFFFFF);
		
		this.globalSpawnRate.drawEntry(1, x, y + 24, listWidth, 22, mouseX, mouseY, isSelected, false, partial);
		this.building       .drawEntry(2, x, y + 46, listWidth, 22, mouseX, mouseY, isSelected, false, partial);
		
		this.drawRec(this.parent.labelX, y + 68, this.parent.scrollBarX - this.parent.labelX - 3, 152, 0x08FFFFFF);
		
		this.parent.labelX += 10;
		
		this.bEnabled     .drawEntry(3, x, y + 72, listWidth, 22, mouseX, mouseY, isSelected, false, partial);
		this.bDimentionList.drawEntry(4, x, y + 92, listWidth, 22, mouseX, mouseY, isSelected, false, partial);
		
		this.drawRec(this.parent.labelX, y + 111, this.parent.scrollBarX - this.parent.labelX - 3, 109, 0x08FFFFFF);
		
		this.parent.labelX += 10;
		
		if (this.getCurrentDimention() != null) {
			this.dSpawnRate  .drawEntry(4, x, y + 112, listWidth, 22, mouseX, mouseY, isSelected, false, partial);
			this.dSpawnMin.drawEntry(4, x, y + 132, listWidth, 22, mouseX, mouseY, isSelected, false, partial);
			this.dSpawnMax.drawEntry(4, x, y + 152, listWidth, 22, mouseX, mouseY, isSelected, false, partial);
			this.dBlocksSpawn.drawEntry(4, x, y + 172, listWidth, 22, mouseX, mouseY, isSelected, partial);
			this.parent.labelX += 20;
			this.dBiomes.drawEntry(4, x, y + 192, listWidth, 22, mouseX, mouseY, isSelected, partial);
		}
		this.resetControlWidth();
		
		this.parent.controlWidth += 3;
	}
	
	@Override
	public int getLabelWidth() {
		if (!this.getLabelDisplay()) {
			return 0;
		}
		
		int[] sizes = new int[] {
			mc.fontRenderer.getStringWidth(this.tradIfExist("group"      ))-10,
			mc.fontRenderer.getStringWidth(this.tradIfExist("globalSpawnRate")),
			mc.fontRenderer.getStringWidth(this.tradIfExist("building"   ))+10,
			mc.fontRenderer.getStringWidth(this.tradIfExist("disabled"   ))+10,
			mc.fontRenderer.getStringWidth(this.tradIfExist("dimention"  ))+10,
			mc.fontRenderer.getStringWidth(this.tradIfExist("spawnRate"  ))+20,
			mc.fontRenderer.getStringWidth(this.tradIfExist("spawnMin"   ))+20,
			mc.fontRenderer.getStringWidth(this.tradIfExist("spawnMax"   ))+20,
			mc.fontRenderer.getStringWidth(this.tradIfExist("blocksSpawn"))+20,
			mc.fontRenderer.getStringWidth(this.tradIfExist("biomesFilter"))+20,
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
	private void setCurrentDimention(String dim) {
		this.currentDimention = null;
		try {
			if (dim != null && !dim.equals("")) {
				this.currentDimention = Integer.parseInt(dim);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		this.saveEntry();
		if (value instanceof BuildingConfigType) {
			this.currentGroup     = 0;
			this.currentBuilding  = 0;
			this.currentDimention = null;
			this.value = (BuildingConfigType) ((BuildingConfigType)value).clone();
		}
		this.setGroup();
		return super.setValue(value);
	}
}
