package com.gollum.core.client.gui.config;

import static net.minecraftforge.fml.client.config.GuiUtils.RESET_CHAR;
import static net.minecraftforge.fml.client.config.GuiUtils.UNDO_CHAR;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.common.config.ConfigProp;
import com.gollum.core.common.config.JsonConfigProp;
import com.gollum.core.common.mod.GollumMod;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiMessageDialog;
import net.minecraftforge.fml.client.config.GuiUnicodeGlyphButton;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public abstract class GuiConfig extends GuiScreen {
	
	protected ArrayList<ConfigElement> configElements = new ArrayList<ConfigElement>();
	protected GuiConfigEntries entryList;
	
	protected GuiButtonExt btDone;
	protected GuiButtonExt btUndo;
	protected GuiButtonExt btReset;
	
	private HoverChecker undoHoverChecker;
	private HoverChecker resetHoverChecker;
	
	protected GuiScreen parent;
	
	public GollumMod mod;
	private String title;
	public String titleLine2 = null;
	
	protected boolean needsRefresh = true;
	
	public GuiConfig(GuiScreen parent) {
		
		this.parent = parent;
		this.mod    = this.getMod();
		this.title  = this.mod.getModId();
		
	}
	
	protected abstract void initConfigElement ();
	
	public GollumMod getMod() {
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

		// Init List
		
		if (this.entryList == null) {
			this.initConfigElement();
		}
		
		if (this.needsRefresh) {
			this.entryList = new GuiConfigEntries(this, this.mc);
			this.needsRefresh = false;
		}
		
		// Init Button action
		
		int undoGlyphWidth  = this.mc.fontRenderer.getStringWidth(UNDO_CHAR)  * 2;
		int resetGlyphWidth = this.mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
		
		// Calculate size
		int doneWidth       = Math.max(this.mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
		int undoWidth       = this.mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
		int resetWidth      = this.mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
		int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
		
		this.buttonList.clear();
		
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
			" " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 
			2.0F
		));
			
		this.buttonList.add(this.btReset = new GuiUnicodeGlyphButton(
			2001,
			this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5,
			this.height - 29,
			resetWidth,
			20,
			" " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 
			2.0F
		));
		
		this.undoHoverChecker = new HoverChecker(this.btUndo, 800);
		this.resetHoverChecker = new HoverChecker(this.btReset, 800);
		
		this.entryList.initGui();
		
	}
	
	public int getTopEntryList() {
		return this.titleLine2 != null ? 33 : 23;
	}
	
	public boolean mustntDisplay () {
		return false;
	}
	
	public abstract void saveValue();
	
	public GuiScreen getParent () {
		if (this.parent instanceof GuiConfig) {
			if (((GuiConfig) this.parent).mustntDisplay()) {
				return ((GuiConfig) this.parent).getParent();
			}
		}
		return this.parent;
	}
	
	protected boolean displayRestart() {
		boolean mcRestart = this.entryList.requiresMcRestart();
		boolean wRestart  = this.entryList.requiresWorldRestart();
		
		ConfigChangedEvent event = new OnConfigChangedEvent(this.getMod().getModId(), "", wRestart, mcRestart);
		FMLCommonHandler.instance().bus().post(event);
		if (!event.getResult().equals(Result.DENY)) {
			this.saveValue ();
			FMLCommonHandler.instance().bus().post(new PostConfigChangedEvent(this.getMod().getModId(), "", wRestart, mcRestart));

			if (mcRestart) {
				this.mc.displayGuiScreen(new GuiMessageDialog(this.getParent(), "fml.configgui.gameRestartTitle", new TextComponentString(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
				return true;
			} else
			if (wRestart && this.mc.world != null) {
				this.mc.displayGuiScreen(new GuiMessageDialog(this.getParent(), ModGollumCoreLib.i18n.trans("config.worldRestartTitle"), new TextComponentString(ModGollumCoreLib.i18n.trans("config.worldRestartRequired")), "fml.configgui.confirmRestartMessage"));
				return true;
			}
		}
		return false;
	}
	
	public void displayParent() {
		this.saveValue ();
		if (this.parent instanceof GuiConfig) {
			if (((GuiConfig) this.parent).mustntDisplay()) {
				((GuiConfig) this.parent).displayParent();
				return;
			}
		}
		this.mc.displayGuiScreen(this.getParent());
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			this.displayParent();
		} else if (button.id == 2001) {
			this.setToDefault ();
		} else if (button.id == 2002) {
			this.undoChanges ();
		}
	}
	
	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		this.drawDefaultBackground();
		
		this.entryList.drawScreen(mouseX, mouseY, partialTicks);
		
		this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 8, 16777215);
		
		String title2 = this.titleLine2;
		if (this.titleLine2 != null) {
			int strWidth = mc.fontRenderer.getStringWidth(title2);
			int elipsisWidth = mc.fontRenderer.getStringWidth("...");
			if (strWidth > width - 6 && strWidth > elipsisWidth) {
				title2 = mc.fontRenderer.trimStringToWidth(title2, width - 6 - elipsisWidth).trim() + "...";
			}
			this.drawCenteredString(this.fontRenderer, title2, this.width / 2, 18, 16777215);
		}
		
		this.btDone.enabled  = this.isValidValues();
		this.btUndo.enabled  = this.isChanged() && this.undoIsVisible();
		this.btReset.enabled = !this.isDefault() && this.resetIsVisible();

		this.btUndo.visible  = this.undoIsVisible();
		this.btReset.visible = this.resetIsVisible();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if (this.undoHoverChecker.checkHover(mouseX, mouseY)) {
			this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.undoAll"), 300), mouseX, mouseY);
		}
		if (this.resetHoverChecker.checkHover(mouseX, mouseY)) {
			this.drawToolTip(this.mc.fontRenderer.listFormattedStringToWidth(I18n.format("fml.configgui.tooltip.resetAll"), 300), mouseX, mouseY);
		}
		
		this.entryList.drawScreenPost (mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void drawGradientRect(int p_73733_1_, int p_73733_2_, int p_73733_3_, int p_73733_4_, int p_73733_5_, int p_73733_6_) {
		super.drawGradientRect(p_73733_1_, p_73733_2_, p_73733_3_, p_73733_4_, p_73733_5_, p_73733_6_);
	}
	
	public boolean resetIsVisible() {
		return true;
	}
	
	public boolean undoIsVisible() {
		return true;
	}
	
	protected boolean isValidValues() {
		return this.entryList.isValidValues ();
	}
	
	protected boolean isChanged() {
		return this.entryList.isChanged ();
	}
	
	protected boolean isDefault() {
		return this.entryList.isDefault ();
	}
	
	protected void setToDefault() {
		this.entryList.setToDefault ();
	}
	
	protected void undoChanges() {
		this.entryList.undoChanges ();
	}
	
	public void drawToolTip(List stringList, int x, int y) {
		this.drawHoveringText(stringList, x, y);
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.entryList.handleMouseInput();
	}
	
	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int x, int y, int mouseEvent) throws IOException {
		if (mouseEvent != 0 || !this.entryList.mouseClicked(x, y, mouseEvent)) {
			this.entryList.mouseScreenClicked(x, y, mouseEvent);
			super.mouseClicked(x, y, mouseEvent);
		}
	}
	
	/**
	 * Called when the mouse is moved or a mouse button is released. Signature:
	 * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
	 * mouseUp
	 */
	@Override
	protected void mouseReleased(int x, int y, int mouseEvent) {
		if (mouseEvent != 0 || !this.entryList.mouseReleased(x, y, mouseEvent)) {
			super.mouseReleased(x, y, mouseEvent);
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char eventChar, int eventKey) {
		if (eventKey == Keyboard.KEY_ESCAPE) {
			this.mc.displayGuiScreen(this.parent);
		} else {
			this.entryList.keyTyped(eventChar, eventKey);
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.entryList.updateScreen();
	}

	public boolean isArray() {
		return false;
	}
	
	public boolean displayEntriesLabel() {
		return true;
	}
	
	public boolean canAdd() {
		return false;
	}

	public boolean canRemove() {
		return false;
	}

	
	public Object newValue() {
		return null;
	}
	
	public ConfigProp newConfigProp() {
		return new JsonConfigProp();
	}

	
}
