package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.util.ArrayList;

import mods.gollum.core.client.gui.config.entries.GuiConfigEntryNumber;
import mods.gollum.core.client.gui.config.entries.GuiConfigEntryString;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.config.ConfigProp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class GuiConfigEntries extends GuiListExtended {
	
	private ArrayList<GuiConfigEntryString> entries;
	private GuiGollumConfig parent;
	private Minecraft mc;
	private ConfigLoad configLoad;

	//////////////
	// Position //
	//////////////
	
	public int maxEntryRightBound = 0;
	public int maxLabelTextWidth = 0;
	public int labelX;
	public int controlX;
	public int resetX;
	public int controlWidth;
	public int scrollBarX;
	
	public GuiConfigEntries(GuiGollumConfig parent, Minecraft mc, ConfigLoad configLoad) {
		
		super(mc, parent.width, parent.height, 23, parent.height - 32, 20);
		
		this.parent     = parent;
		this.mc         = mc;
		this.configLoad = configLoad;
		
		this.entries = new ArrayList<GuiConfigEntryString>();
		
		
		if (configLoad != null) {
			for (Field f : configLoad.config.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				
				ConfigProp anno = f.getAnnotation(ConfigProp.class);
				if (anno != null) {
					
					// TODO généraliser aux autres champs
					if (
						f.getType().isAssignableFrom(Long.TYPE) ||
						f.getType().isAssignableFrom(Integer.TYPE)
					) {
						
						String label = this.getLabel (f.getName());
						
						try {
							
							entries.add(new GuiConfigEntryNumber (
								this,
								this.mc,
								label,
								f.get(configLoad.config).toString(),
								f.get(configLoad.configDefault).toString(),
								new Object[] {configLoad, f}
							) {
								@Override
								protected void onChange() {
									log.debug ("Change entity : " + this.getValue());
									try {
										
										ConfigLoad configLoad = (ConfigLoad) this.params[0];
										Field      f          = (Field)      this.params[1];

										if(f.getType().isAssignableFrom(Long.TYPE)   ) f.set(configLoad.config, Long   .parseLong(this.getValue()));
										if(f.getType().isAssignableFrom(Integer.TYPE)) f.set(configLoad.config, Integer.parseInt (this.getValue()));
										
									} catch (Exception e) {
									}
								}
							});
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		}
		
		this.initGui();
	}
	
	protected void initGui() {
		this.width  = parent.width;
		this.height = parent.height;
		
		this.maxLabelTextWidth = 0;
		for (GuiConfigEntryString entry : this.entries) {
			this.maxLabelTextWidth = Math.max (this.maxLabelTextWidth, this.mc.fontRenderer.getStringWidth(entry.label));
		}
		
		this.top = 23;
		this.bottom = parent.height - 32;
		this.left = 0;
		this.right = width;

		int viewWidth = this.maxLabelTextWidth + 8 + (this.width / 2);
		this.labelX   = (this.width / 2) - (viewWidth / 2);
		this.controlX = this.labelX + this.maxLabelTextWidth + 8;
		this.resetX   = (this.width / 2) + (viewWidth / 2) - 45;
		
		this.maxEntryRightBound = 0;
		for (GuiConfigEntryString entry : this.entries) {
			this.maxEntryRightBound = Math.max(this.maxEntryRightBound, entry.getEntryRightBound());
		}
		
		scrollBarX = this.maxEntryRightBound + 5;
		controlWidth = maxEntryRightBound - controlX - 45;

	}
	
	public String getLabel (String name) {
		return this.configLoad.mod.i18n().trans("config."+name);
	}
	
	@Override
	public IGuiListEntry getListEntry(int i) {
		return this.entries.get(i);
	}

	@Override
	protected int getSize() {
		return this.entries.size();
	}

	public void updateScreen() {
		for (GuiConfigEntryString entry : this.entries) {
			entry.updateCursorCounter();
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseEvent) {
		for (GuiConfigEntryString entry : this.entries) {
			entry.mouseClicked(mouseX, mouseY, mouseEvent);
		}
	}
	
	public void keyTyped(char eventChar, int eventKey) {
		 for (GuiConfigEntryString entry : this.entries) {
			 entry.keyTyped(eventChar, eventKey);
		}
	}
	
	@Override
	public int getScrollBarX() {
		return scrollBarX;
	}
	
	@Override
	public int getListWidth() {
		return parent.width;
	}


	public boolean isDefault() {
		for (GuiConfigEntryString entry : this.entries) {
			if (!entry.isDefault()) {
				return false;
			}
		}
		return true;
	}

	public boolean isChanged() {
		for (GuiConfigEntryString entry : this.entries) {
			if (entry.isChanged()) {
				return true;
			}
		}
		return false;
	}

	public void setAllToDefault() {
		for (GuiConfigEntryString entry : this.entries) {
			entry.setToDefault();
		}
	}

	public void undoAllChanges() {
		for (GuiConfigEntryString entry : this.entries) {
			entry.undoChanges();
		}
	}
	
	public void saveConfigElements() {
		log.debug ("Save config");
		if (this.configLoad != null) {
			new ConfigLoader (this.configLoad.config, false).writeConfig();
		} else {
			log.warning ("No config init");
		}
	}
	
	
	
}
