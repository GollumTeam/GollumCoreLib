package mods.gollum.core.client.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import static mods.gollum.core.ModGollumCoreLib.log;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import org.lwjgl.input.Keyboard;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.common.config.ConfigLoader;
import mods.gollum.core.common.config.ConfigLoader.ConfigLoad;
import mods.gollum.core.common.mod.GollumMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.config.HoverChecker;
import cpw.mods.fml.common.ModContainer;

public class GuiConfigMod extends GuiScreen {
	
	GollumMod mod;
	GuiScreen parentScreen;

	public GuiConfigEntries entryList;
	
	private GuiUnicodeGlyphButton btnUndoAll;
	private GuiUnicodeGlyphButton btnDefaultAll;
	
	private HoverChecker undoHoverChecker;
	private HoverChecker resetHoverChecker;
	
	public GuiConfigMod(GuiScreen parent) {
		
		this.parentScreen = parent;
		
		if (parent instanceof GuiModList) {
			try {
				Field f = parent.getClass().getDeclaredField("selectedMod");
				f.setAccessible(true);
				ModContainer modContainer = (ModContainer)f.get(parent);
				mod = (GollumMod) modContainer.getMod();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		log.debug ("Config mod : " + mod.getModId());
		
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		super.initGui();
		
		Keyboard.enableRepeatEvents(true);
		
		int undoGlyphWidth  = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
		int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
		int doneWidth       = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
		int undoWidth       = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
		int resetWidth      = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
		int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth + 5) / 2;
		
		this.buttonList.add(new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
		
		this.buttonList.add(btnDefaultAll = new GuiUnicodeGlyphButton(
			2001, 
			this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5,
			this.height - 29, 
			resetWidth,
			20,
			" " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 2.0F)
		);
		
		this.buttonList.add(btnUndoAll = new GuiUnicodeGlyphButton(
			2002, 
			this.width / 2 - buttonWidthHalf + doneWidth + 5,
			this.height - 29, 
			undoWidth, 
			20, 
			" " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 2.0F)
		);
		
		this.undoHoverChecker = new HoverChecker(this.btnUndoAll, 800);
		this.resetHoverChecker = new HoverChecker(this.btnDefaultAll, 800);
		
		this.entryList = new GuiConfigEntries(this, mc, ConfigLoader.configLoaded.get(this.mod));
		this.entryList.initGui();
	}

	private void addEntries(ConfigLoad configLoad) {
		
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}
	
	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.entryList.updateScreen();
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		this.drawCenteredString(this.fontRendererObj, this.mod.getModName(), this.width / 2, 8, 16777215);
		
		
		this.entryList.drawScreen(mouseX, mouseY, partialTicks);

		this.btnDefaultAll.enabled = !this.entryList.isDefault ();
		this.btnUndoAll.enabled = this.entryList.isChanged ();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if (this.undoHoverChecker.checkHover(mouseX, mouseY)) {
			this.drawToolTip(
				this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.undoAll"), 300), 
				mouseX, mouseY
			);
		}
		if (this.resetHoverChecker.checkHover(mouseX, mouseY)) {
			this.drawToolTip(
				this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.resetAll"), 300), 
				mouseX, mouseY
			);
		}
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int x, int y, int mouseEvent) {
		if (mouseEvent != 0 || !this.entryList.func_148179_a(x, y, mouseEvent)) {
			this.entryList.mouseClicked(x, y, mouseEvent);
			super.mouseClicked(x, y, mouseEvent);
		}
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char eventChar, int eventKey) {
		if (eventKey == Keyboard.KEY_ESCAPE) {
			this.mc.displayGuiScreen(parentScreen);
		} else { // TODO
			this.entryList.keyTyped(eventChar, eventKey);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			
			this.mc.displayGuiScreen(this.parentScreen);
			
		} else
		if (button.id == 2001) {
			
		} else
		if (button.id == 2002) {
			
		}
	}
	
	public void drawToolTip(List stringList, int x, int y) {
		this.func_146283_a(stringList, x, y);
	}
}