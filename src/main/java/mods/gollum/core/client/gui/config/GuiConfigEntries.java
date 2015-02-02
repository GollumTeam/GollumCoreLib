package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.client.gui.config.element.TypedValueElement;
import mods.gollum.core.client.gui.config.entry.AddButtonEntry;
import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.utils.reflection.Reflection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

public class GuiConfigEntries extends GuiListExtended {

	protected Minecraft mc;
	public GuiConfig parent;
	
	private ArrayList<ConfigEntry> listEntries = new ArrayList<ConfigEntry>();
	
	public int labelX;
	public int controlX;
	public int resetX;
	public int scrollBarX;
	public int controlWidth;
	public int selected = -1;
	
	public GuiConfigEntries(GuiConfig parent, Minecraft mc) {
		super(mc, parent.width, parent.height, parent.titleLine2 != null ? 33 : 23, parent.height - 32, 20);

		this.mc = mc;
		this.parent = parent;
		
		this.setShowSelectionBox(false);
		
		for (ConfigElement configElement : this.parent.configElements) {
			if (configElement != null) {
				ConfigProp anno = configElement.getConfigProp();
				ConfigEntry configEntry = this.newInstanceOfEntryConfig(this.listEntries.size(), configElement, anno);
				if (configEntry != null) {
					this.listEntries.add(configEntry);
				}
			} else {
				log.severe("Can create config entry because ConfigElement is null");
			}
		}
		this.listEntries.add(new AddButtonEntry(this.listEntries.size(), this.mc, this));
	}
	
	public ConfigEntry newInstanceOfEntryConfig(int index, ConfigElement configElement, ConfigProp anno) {
		ConfigEntry configEntry = null;
		
		if (anno.show() && (!anno.dev() || ModGollumCoreLib.config.devTools)) {
			try {
				if (configElement.getEntryClass() != null) {
					configEntry = configElement.getEntryClass().getConstructor(int.class, Minecraft.class, GuiConfigEntries.class, ConfigElement.class).newInstance(index, this.mc, this, configElement);
					log.debug("Create config entry : "+configElement.getName()+" : "+configElement.getEntryClass().getName());
				} else {
					log.warning("ConfigElement "+configElement.getName()+" hasn't class entry");
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.severe("Can create config entry : "+configElement.getName()+" : "+configElement.getEntryClass().getName());
			}
		}
		return configEntry;
	}
	
	@Override
	protected boolean isSelected(int index) {
		return this.selected == index;
	}

	@Override
	protected void drawBackground() {}

	@Override
	protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_) {
		this.getListEntry(p_148126_1_).drawEntry(p_148126_1_, p_148126_2_, p_148126_3_, this.getListWidth(), p_148126_4_, p_148126_5_, p_148126_6_, p_148126_7_, this.func_148124_c(p_148126_6_, p_148126_7_) == p_148126_1_);
	}
	
	@Override
	protected int getSize() {
		return this.listEntries.size() - (this.parent.canAdd() ? 0 : 1);
	}

	public IGuiListEntry getListEntry(int index) {
		return (IGuiListEntry) this.listEntries.get(index);
	}
	
	public ConfigEntry getEntry(int index) {
		return this.listEntries.get(index);
	}
	
	public void drawScreenPost(int mouseX, int mouseY, float partialTicks) {
		for (ConfigEntry entry : this.listEntries) {
			entry.drawToolTip(mouseX, mouseY);
		}
	}
	
	@Override
	public int getScrollBarX() {
		return this.scrollBarX;
	}
	
	/**
	 * Gets the width of the list
	 */
	@Override
	public int getListWidth() {
		return this.width;
	}
	
	public int getMaxLabelSizeEntry () {
		int maxSizeEntry = 0;
		for (ConfigEntry entry : this.listEntries) {
			
			int size = entry.getLabelWidth();
			
			if (maxSizeEntry < size) {
				maxSizeEntry = size;
			}
		}
		return maxSizeEntry;
	}
	
	public void initGui() {
		
		this.width = this.parent.width;
		this.height = this.parent.height;
		
		this.top    = this.parent.getTopEntryList ();
		this.bottom = this.parent.height - 32;
		this.left   = 0;
		this.right  = width;
		float ratio = 1.78F;
		if (this.parent.displayEntriesLabel()) {
			ratio = 2.0F;
		}
		
		float maxLabel = (float)this.getMaxLabelSizeEntry();
		
		float viewWidth = (float)maxLabel + 8.0F + ((float)this.width / ratio);
		
		this.labelX   = (int)( ((float)this.width / 2.0F) - (viewWidth / ratio)         );
		this.controlX = (int)( (float)this.labelX + maxLabel + 8.0F                      );
		this.resetX   = (int)( ((float)this.width / ratio) + (viewWidth / ratio) - 45.0F );
		this.scrollBarX   = this.resetX + 45;
		this.controlWidth = this.resetX - this.controlX - 5;
		if (this.parent.canAdd ()) {
			this.controlWidth -= 22;
		}
		if (this.parent.canRemove ()) {
			this.controlWidth -= 22;
		}
	}
	
	public boolean isChanged() {
		boolean changed = false;
		
		for(ConfigEntry entry : this.listEntries) {
			if (entry.enabled() && entry.isChanged()) {
				changed = true;
				break;
			}
		}
		
		return changed;
	}

	public boolean isDefault() {
		boolean isDefault = true;
		
		for(ConfigEntry entry : this.listEntries) {
			if (entry.enabled() && !entry.isDefault()) {
				isDefault = false;
				break;
			}
		}
		
		return isDefault;
	}

	public boolean isValidValues() {
		boolean isValid = true;
		
		for(ConfigEntry entry : this.listEntries) {
			if (entry.enabled() && !entry.isValidValue()) {
				isValid = false;
				break;
			}
		}
		
		return isValid;
	}
	
	public LinkedHashMap<String, Object> getValues() {
		LinkedHashMap<String, Object> values = new LinkedHashMap<String, Object>();
		for(ConfigEntry entry : this.listEntries) {
			if (!(entry instanceof AddButtonEntry)) {
				if (entry.enabled() && entry.isValidValue()) {
					values.put(entry.getName(), entry.getValue());
				} else {
					values.put(entry.getName(), entry.configElement.getValue());
				}
			}
		}
		return values;
	}
	
	public ArrayList<Object> getValuesByIndex() {
		ArrayList<Object> values = new ArrayList<Object>();
		for(ConfigEntry entry : this.listEntries) {
			if (!(entry instanceof AddButtonEntry)) {
				if (entry.enabled() && entry.isValidValue()) {
					values.add(entry.getValue());
				} else {
					values.add(entry.configElement.getValue());
				}
			}
		}
		return values;
	}
	
	/**
	 * This method is a pass-through for IConfigEntry objects that contain
	 * GuiTextField elements. Called from the parent GuiConfig screen.
	 */
	public void updateScreen() {
		for (ConfigEntry entry : this.listEntries) {
			entry.updateCursorCounter();
		}
	}
	
	public boolean requiresMcRestart() {
		for (ConfigEntry entry : this.listEntries) {
			if (entry.isChanged() && entry.requiresMcRestart()) {
				return true;
			}
		}
		
		return false;
	}
	
	public void setSlot (int slotIndex) {
		this.selected = slotIndex;
		this.listEntries.get(slotIndex).setSlot(slotIndex);
	}
	
	public GuiConfigEntries setSlotHeight(int height) {
		try {
			
			try {
				Reflection.setFinalField(GuiSlot.class.getDeclaredField("slotHeight"), this, height);
			} catch (Exception e) {
				e.printStackTrace();
				Reflection.setFinalField(GuiSlot.class.getDeclaredField("field_148149_f"), this, height);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	/////////////
	// Actions //
	/////////////

	public void add(int index) {
		if (this.parent.canAdd()) {
			Object value = this.parent.newValue();
			Object defaultValue = this.parent.newValue();
			ConfigProp prop = this.parent.newConfigProp();
			TypedValueElement configElement = new TypedValueElement(value.getClass(), "", value, defaultValue, prop);
			
			if (value != null && defaultValue != null) {
				log.debug ("add : "+index);
				
				ConfigEntry configEntry = this.newInstanceOfEntryConfig(index, configElement, prop);
				if (configEntry != null) {
					this.listEntries.add(index, configEntry);
					for (int i = index; i < this.listEntries.size(); i++) {
						this.listEntries.get(i).index++;
					}
				}
			}
			this.initGui();
		}
	}
	
	public void remove(int index) {
		if (this.parent.canRemove()) {
			log.debug ("remove : "+index);
			this.listEntries.remove(index);
			this.initGui();
			for (int i = index; i < this.listEntries.size(); i++) {
				this.listEntries.get(i).index--;
			}
		}
	}

	public void setToDefault() {
		for(ConfigEntry entry : this.listEntries) {
			if (entry.enabled()) {
				entry.setToDefault();
			}
		}
	}

	public void undoChanges() {
		for(ConfigEntry entry : this.listEntries) {
			if (entry.enabled()) {
				entry.undoChanges();
			}
		}
	}
	
	/**
	 * This method is a pass-through for IConfigEntry objects that require keystrokes. Called from the parent GuiConfig screen.
	 */
	public void keyTyped(char eventChar, int eventKey) {
		for (ConfigEntry entry : this.listEntries) {
			entry.keyTyped(eventChar, eventKey);
		}
	}
	
	/**
	 * This method is a pass-through for IConfigEntry objects that contain GuiTextField elements. Called from the parent GuiConfig
	 * screen.
	 */
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		for (ConfigEntry entry : this.listEntries){
			entry.mouseClicked(mouseX, mouseY, mouseEvent);
		}
	}
	
	@Override
	protected void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY) {
		this.setSlot(slotIndex);
		this.listEntries.get(slotIndex).elementClicked(slotIndex, doubleClick, mouseX, mouseY);
	}

	
}
