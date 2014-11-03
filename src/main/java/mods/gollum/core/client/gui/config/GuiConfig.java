package mods.gollum.core.client.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;

import java.lang.reflect.Field;
import java.util.List;

import mods.gollum.core.common.mod.GollumMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.config.HoverChecker;
import cpw.mods.fml.common.ModContainer;



public class GuiConfig extends GuiScreen {

	protected GuiButtonExt btDone;
	protected GuiButtonExt btUndo;
	protected GuiButtonExt btReset;
	
	private HoverChecker undoHoverChecker;
	private HoverChecker resetHoverChecker;
	
	protected GuiScreen parent;
	
	protected GollumMod mod;
	private String title;
	private String titleLine2 = null;
	

	public GuiConfig(GuiScreen parent) {
		
		this.parent = parent;
		this.mod    = this.getMod();
		this.title  = this.mod.getModId();
		
	}
	
	private GollumMod getMod() {
		if (this.parent instanceof GuiModList) {
			try {
				Field f = parent.getClass().getDeclaredField("selectedMod");
				f.setAccessible(true);
				ModContainer modContainer = (ModContainer)f.get(parent);
				return (GollumMod) modContainer.getMod();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (this.parent instanceof GuiConfig) {
			return ((GuiConfig) this.parent).mod;
		}
		return null;
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		
		Keyboard.enableRepeatEvents(true);
		
		int undoGlyphWidth  = mc.fontRenderer.getStringWidth(UNDO_CHAR)  * 2;
		int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
		
		// Calculate size
		int doneWidth       = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
		int undoWidth       = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
		int resetWidth      = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
		int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
		
		
		this.buttonList.add(this.btDone  = new GuiButtonExt(
			2000,
			this.width / 2 - buttonWidthHalf,
			this.height - 29, 
			doneWidth,
			20, 
			I18n.format("gui.done")
		));
		this.buttonList.add(this.btUndo = new GuiUnicodeGlyphButton(
			2002,
			this.width / 2 - buttonWidthHalf + doneWidth + 5,
			this.height - 29, 
			undoWidth,
			20,
			" " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 2.0F));
		
		this.buttonList.add(this.btReset = new GuiUnicodeGlyphButton(
			2001,
			this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5,
			this.height - 29,
			resetWidth,
			20,
			" " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 2.0F
		));
		
		this.undoHoverChecker = new HoverChecker(this.btUndo, 800);
		this.resetHoverChecker = new HoverChecker(this.btReset, 800);
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			this.mc.displayGuiScreen(this.parent);
		} else if (button.id == 2001) {
		} else if (button.id == 2002) {
		}
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		this.drawDefaultBackground();
		
		this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);
		
		String title2 = this.titleLine2;
		if (this.titleLine2 != null) {
			int strWidth = mc.fontRenderer.getStringWidth(title2);
			int elipsisWidth = mc.fontRenderer.getStringWidth("...");
			if (strWidth > width - 6 && strWidth > elipsisWidth) {
				title2 = mc.fontRenderer.trimStringToWidth(title2, width - 6 - elipsisWidth).trim() + "...";
			}
			this.drawCenteredString(this.fontRendererObj, title2, this.width / 2, 18, 16777215);
		}

		this.btUndo.enabled = true; // this.btUndo.enabled  = this.entryList.areAnyEntriesEnabled(this.chkApplyGlobally.isChecked()) && this.entryList.hasChangedEntry(this.chkApplyGlobally.isChecked());
		this.btReset.enabled = true; // this.btReset.enabled = this.entryList.areAnyEntriesEnabled(this.chkApplyGlobally.isChecked()) && !this.entryList.areAllEntriesDefault(this.chkApplyGlobally.isChecked());
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if (this.undoHoverChecker.checkHover(mouseX, mouseY)) {
			this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.undoAll"), 300), mouseX, mouseY);
		}
		if (this.resetHoverChecker.checkHover(mouseX, mouseY)) {
			this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.resetAll"), 300), mouseX, mouseY);
		}
	}
	
	public void drawToolTip(List stringList, int x, int y) {
		this.func_146283_a(stringList, x, y);
	}
	
}
