package mods.gollum.core.client.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import static mods.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import mods.gollum.core.client.gui.config.entry.GollumCategoryEntry;
import mods.gollum.core.client.gui.config.entry.IGollumConfigEntry;
import mods.gollum.core.client.gui.config.properties.FieldProperty;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.mod.GollumMod;
import mods.gollum.core.tools.simplejson.Json;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiMessageDialog;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.config.HoverChecker;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.ModContainer;

public abstract class GuiGollumConfig extends GuiConfig {
	
	protected IGollumConfigEntry entry = null;
	
	public GuiGollumConfig(GuiConfig parent, List<IConfigElement> fields, IGollumConfigEntry entry) {
		this(parent, fields, parent.title);
		
		this.titleLine2 = parent.titleLine2 + " > "+entry.getName();
		this.entry      = entry;
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
		
		int startBt = this.buttonList.size();
		
		super.initGui();
		
		int undoGlyphWidth = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
		int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
		int doneWidth = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
		int undoWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
		int resetWidth = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
		int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth + 5) / 2;
		
		((GuiButtonExt)this.buttonList.get(startBt  )).xPosition = this.width / 2 - buttonWidthHalf;;
		((GuiButtonExt)this.buttonList.get(startBt+2)).xPosition = this.width / 2 - buttonWidthHalf + doneWidth + 5;
		((GuiButtonExt)this.buttonList.get(startBt+1)).xPosition = this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5;
		((GuiCheckBox )this.buttonList.get(startBt+3)).visible   = false;
		((GuiCheckBox )this.buttonList.get(startBt+3)).setIsChecked(true);
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
}