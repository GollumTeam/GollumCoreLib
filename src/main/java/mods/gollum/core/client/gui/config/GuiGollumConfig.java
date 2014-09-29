package mods.gollum.core.client.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.util.List;

import mods.gollum.core.client.gui.config.entry.AddButtonEntry;
import mods.gollum.core.client.gui.config.entry.ArrayEntry;
import mods.gollum.core.client.gui.config.entry.IGollumConfigEntry;
import mods.gollum.core.client.gui.config.properties.ValueProperty;
import mods.gollum.core.common.mod.GollumMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.HoverChecker;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.common.ModContainer;

public abstract class GuiGollumConfig extends GuiConfig {

	protected GollumMod mod;
	protected IGollumConfigEntry entry = null;
	
	protected GuiButtonExt btnDone;
	protected GuiButtonExt btnUndo;
	protected GuiButtonExt btnReset;
	protected GuiCheckBox  checkBox;
	
	protected HoverChecker undoHoverChecker     = null;
	protected HoverChecker resetHoverChecker    = null;
	protected HoverChecker checkBoxHoverChecker = null;
	
	public GuiGollumConfig(GuiConfig parent, List<IConfigElement> fields, IGollumConfigEntry entry) {
		this(parent, fields, parent.title);
		this.entry      = entry;
		this.mod        = this.getMod(parent);
		this.setTitle2();
	}
	
	private void setTitle2() {
		if (this.parentScreen instanceof GuiConfig) {
			
			this.titleLine2 = ((GuiConfig)this.parentScreen).titleLine2;
			if (!entry.getName().equals("")) {
				this.titleLine2 += " > "+entry.getName();
			}
		}
	}
	
	public GuiGollumConfig(GuiScreen parent, List<IConfigElement> configElements, String title) {
		super(parent, configElements, getModId (parent), false, false, title);
	}

	protected static GollumMod getMod(GuiScreen parent) {
		if (parent instanceof GuiModList) {
			try {
				Field f = parent.getClass().getDeclaredField("selectedMod");
				f.setAccessible(true);
				ModContainer modContainer = (ModContainer)f.get(parent);
				return (GollumMod) modContainer.getMod();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (parent instanceof GuiGollumConfig) {
			return getMod(((GuiGollumConfig) parent).parentScreen);
		}
		return null;
	}
	
	protected static String getModId(GuiScreen parent) {
		return getMod(parent).getModId();
	}
	
	protected static String getModName(GuiScreen parent) {
		return getMod(parent).getModName();
	}
	
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		
		int startBt          = this.buttonList.size();
		boolean needsRefresh = this.needsRefresh;
		
		super.initGui();
		
		if (this.isArray()) {
			this.entryList.labelX += 22;
			this.entryList.scrollBarX += 22;
			this.entryList.controlX += 22;
		}
		
		int undoGlyphWidth = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
		int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
		int doneWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
		int undoWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
		int resetWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
		int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth + 5) / 2;

		this.btnDone  = ((GuiButtonExt)this.buttonList.get(startBt  ));
		this.btnUndo  = ((GuiButtonExt)this.buttonList.get(startBt+2));
		this.btnReset = ((GuiButtonExt)this.buttonList.get(startBt+1));
		this.checkBox = ((GuiCheckBox) this.buttonList.get(startBt+3));
		
		this.btnDone.xPosition  = this.width / 2 - buttonWidthHalf;;
		this.btnUndo.xPosition  = this.width / 2 - buttonWidthHalf + doneWidth + 5;
		this.btnReset.xPosition = this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5;
		this.checkBox.visible   = false;
		this.checkBox.setIsChecked(true);
		
		try {
			Field f;
			f = GuiConfig.class.getDeclaredField("undoHoverChecker")    ; f.setAccessible(true);
			this.undoHoverChecker     = (HoverChecker) f.get(this);
			f = GuiConfig.class.getDeclaredField("resetHoverChecker")   ; f.setAccessible(true);
			this.resetHoverChecker    = (HoverChecker) f.get(this);
			f = GuiConfig.class.getDeclaredField("checkBoxHoverChecker"); f.setAccessible(true);
			this.checkBoxHoverChecker = (HoverChecker) f.get(this);
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		if (this.isArray()) {
			
			if (needsRefresh) {
				this.entryList.listEntries.add(new AddButtonEntry (this));
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			
			if (doneAction ()) {
				return;
			}
			
		} else {
			super.actionPerformed(button);
		}
	}
	
	protected boolean doneAction() {
		this.mc.displayGuiScreen(this.parentScreen);
		return true;
	}
	
	public void addNewEntry(int index) {
		log.debug ("More pressed");
	}
	
	public void removeEntry(int index) {
		log.debug ("Minus pressed");
	}
	
	public boolean isArray() {
		return true;
	}
}