package mods.gollum.core.client.gui.config;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static mods.gollum.core.ModGollumCoreLib.log;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.client.gui.config.entry.ConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;

public class GuiConfigEntries extends GuiSlot {

	protected Minecraft mc;
	protected GuiConfig parent;
		
	private ArrayList<ConfigEntry> listEntries = new ArrayList<ConfigEntry>();
	
	public int labelX;
	public int controlX;
	public int resetX;
	public int scrollBarX;
	public int controlWidth;
	
	public GuiConfigEntries(GuiConfig parent, Minecraft mc) {
		super(mc, parent.width, parent.height, parent.titleLine2 != null ? 33 : 23, parent.height - 32, 20);

		this.mc = mc;
		this.parent = parent;
		
		this.setShowSelectionBox(false);
		
		for (ConfigElement configElement : this.parent.configElements) {
			if (configElement != null) {
				if (configElement.getConfigProp().show()) {
					try {
						ConfigEntry configEntry = configElement.getEntryClass().getConstructor(Minecraft.class, GuiConfigEntries.class, ConfigElement.class).newInstance(this.mc, this, configElement);
						log.debug("Create config entry : "+configElement.getName()+" : "+configElement.getEntryClass().getName());
						
						listEntries.add(configEntry);
						
					} catch (Exception e) {
						e.printStackTrace();
						log.severe("Can create config entry : "+configElement.getName()+" : "+configElement.getEntryClass().getName());
					}
				}
			} else {
				log.severe("Can create config entry because ConfigElement is null");
			}
		}
		
	}

	@Override
	protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_) {}

	@Override
	protected boolean isSelected(int p_148131_1_) {
		return false;
	}

	@Override
	protected void drawBackground() {}

	@Override
	protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_) {
		this.getListEntry(p_148126_1_).drawEntry(p_148126_1_, p_148126_2_, p_148126_3_, this.getListWidth(), p_148126_4_, p_148126_5_, p_148126_6_, p_148126_7_, this.func_148124_c(p_148126_6_, p_148126_7_) == p_148126_1_);
	}
	
	@Override
	protected int getSize() {
		return this.listEntries.size();
	}
	
	public ConfigEntry getListEntry(int index) {
		return this.listEntries.get(index);
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
	
	public int getMaxValueSizeEntry () {
		int maxSizeEntry = 0;
		for (ConfigEntry entry : this.listEntries) {
			
			int size = entry.getValueWidth();
			
			if (maxSizeEntry < size) {
				maxSizeEntry = size;
			}
		}
		return maxSizeEntry;
	}
	
	public void initGui() {
		
		this.width = this.parent.width;
		this.height = this.parent.height;
		
		this.top    = this.parent.titleLine2 != null ? 33 : 23;
		this.bottom = this.parent.height - 32;
		this.left   = 0;
		this.right  = width;
		
		int maxLabel = this.getMaxLabelSizeEntry();
		int maxValue = this.getMaxValueSizeEntry();
		
		int viewWidth = maxLabel + 8 + (width / 2);

//		this.labelX = (this.width / 2) - (viewWidth / 2);
		this.labelX = (this.width / 2) - (viewWidth / 2);
		this.controlX = this.labelX + maxLabel + 8;
		this.resetX = (this.width / 2) + (viewWidth / 2) - 45;
		this.scrollBarX = this.resetX + 45;
		this.controlWidth = this.resetX - this.controlX - 5;
	}
	
}
