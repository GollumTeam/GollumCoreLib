package mods.gollum.core.client.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mods.gollum.core.client.gui.config.entries.GuiEditArrayCustomEntries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiEditArray;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.config.HoverChecker;
import cpw.mods.fml.client.config.IConfigElement;


public class GuiEditCustomArray extends GuiScreen {
//	
//	private final Object[] beforeValues;
//	private Object[] currentValues;
//	public GuiConfig parent;
//	
//	public GuiEditCustomArray(GuiConfig parent, IConfigElement configElement, int slotIndex, Object[] currentValues, boolean enabled) {
//		super(parent, configElement, slotIndex, currentValues, enabled);
//		
//		this.parent        = parent;
//		this.beforeValues  = currentValues;
//		this.currentValues = currentValues;
//	}
//	
//	/**
//	 * Adds the buttons (and other controls) to the screen in question.
//	 */
//	@Override
//	public void initGui() {
//		
//		GuiEditArrayCustomEntries entryList = new GuiEditArrayCustomEntries(this, this.mc, this.configElement, this.beforeValues, this.currentValues);
//		
//		GuiButtonExt btnUndoChanges, btnDefault, btnDone;
//		
//		int undoGlyphWidth  = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
//		int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
//		int doneWidth       = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
//		int undoWidth       = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
//		int resetWidth      = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
//		int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
//		this.buttonList.add(btnDone        = new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
//		this.buttonList.add(btnDefault     = new GuiUnicodeGlyphButton(2001, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, this.height - 29, resetWidth, 20, " " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 2.0F));
//		this.buttonList.add(btnUndoChanges = new GuiUnicodeGlyphButton(2002, this.width / 2 - buttonWidthHalf + doneWidth + 5, this.height - 29, undoWidth, 20, " " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 2.0F));
//		
//		this.setEntryList(entryList);
//		
//		try {
//			
//			Field f = GuiEditArray.class.getDeclaredField("btnUndoChanges");
//			f.setAccessible(true);
//			f.set(this, btnUndoChanges);
//			
//			f = GuiEditArray.class.getDeclaredField("btnDefault");
//			f.setAccessible(true);
//			f.set(this, btnDefault);
//			
//			f = GuiEditArray.class.getDeclaredField("btnDone");
//			f.setAccessible(true);
//			f.set(this, btnDone);
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void setEntryList (GuiEditArrayEntries entryList) {
//		try {
//			Field f = GuiEditArray.class.getDeclaredField("entryList");
//			f.setAccessible(true);
//			f.set(this, entryList);
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	public GuiConfig parentScreen;
	protected IConfigElement configElement;
	private GuiEditArrayCustomEntries entryList;
	
	private GuiButtonExt btnUndoChanges, btnDefault, btnDone;
	
	protected String title;
	protected String titleLine2;
	protected String titleLine3;
	
	protected int slotIndex;
	
	private final Object[] beforeValues;
	private Object[]       currentValues;
	
	private HoverChecker tooltipHoverChecker;
	private List toolTip;
	protected boolean enabled;
	
	public GuiEditCustomArray(GuiConfig parent, IConfigElement configElement, int slotIndex, Object[] currentValues, boolean enabled) {
		
		this.mc            = Minecraft.getMinecraft();
		this.parentScreen  = parent;
		this.configElement = configElement;
		this.slotIndex     = slotIndex;
		this.beforeValues  = currentValues;
		this.currentValues = currentValues;
		this.toolTip       = new ArrayList();
		this.enabled       = enabled;
		String propName    = I18n.format(configElement.getLanguageKey());
		String comment;

		comment = I18n.format(configElement.getLanguageKey() + ".tooltip", "\n" + EnumChatFormatting.AQUA, configElement.getDefault(), configElement.getMinValue(), configElement.getMaxValue());

		if (!comment.equals(configElement.getLanguageKey() + ".tooltip")) {
			toolTip = mc.fontRenderer.listFormattedStringToWidth(EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + comment, 300);
		} else if (configElement.getComment() != null && !configElement.getComment().trim().isEmpty()) {
			toolTip = mc.fontRenderer.listFormattedStringToWidth(EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.YELLOW + configElement.getComment(), 300);
		} else {
			toolTip = mc.fontRenderer.listFormattedStringToWidth(EnumChatFormatting.GREEN + propName + "\n" + EnumChatFormatting.RED + "No tooltip defined.", 300);
		}
		
		if (parentScreen instanceof GuiConfig) {
			this.title = ((GuiConfig) parentScreen).title;
			if (((GuiConfig) parentScreen).titleLine2 != null) {
				this.titleLine2 = ((GuiConfig) parentScreen).titleLine2;
				this.titleLine3 = I18n.format(configElement.getLanguageKey());
			} else {
				this.titleLine2 = I18n.format(configElement.getLanguageKey());
			}
			this.tooltipHoverChecker = new HoverChecker(28, 37, 0, parentScreen.width, 800);
		} else {
			this.title = I18n.format(configElement.getLanguageKey());
			this.tooltipHoverChecker = new HoverChecker(8, 17, 0, parentScreen.width, 800);
		}
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		
		this.entryList = new GuiEditArrayCustomEntries(this, this.mc, this.configElement);
		
		int undoGlyphWidth  = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
		int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
		int doneWidth       = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
		int undoWidth       = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
		int resetWidth      = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
		int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
		this.buttonList.add(btnDone        = new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
		this.buttonList.add(btnDefault     = new GuiUnicodeGlyphButton(2001, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, this.height - 29, resetWidth, 20, " " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 2.0F));
		this.buttonList.add(btnUndoChanges = new GuiUnicodeGlyphButton(2002, this.width / 2 - buttonWidthHalf + doneWidth + 5, this.height - 29, undoWidth, 20, " " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 2.0F));
		
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 2000) {
			try {
				this.entryList.saveConfigElements();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			this.mc.displayGuiScreen(this.parentScreen);
		} else if (button.id == 2001) {
			// TODO
//			this.currentValues = configElement.getDefaults();
//			this.entryList = new GuiEditArrayCustomEntries(this, this.mc, this.configElement, this.beforeValues, this.currentValues);
		} else if (button.id == 2002) {
			// TODO
//			this.currentValues = Arrays.copyOf(beforeValues, beforeValues.length);
//			this.entryList = new GuiEditArrayCustomEntries(this, this.mc, this.configElement, this.beforeValues, this.currentValues);
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
	 * Called when the mouse is moved or a mouse button is released. Signature:
	 * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
	 * mouseUp
	 */
	@Override
	protected void mouseMovedOrUp(int x, int y, int mouseEvent) {
		if (mouseEvent != 0 || !this.entryList.func_148181_b(x, y, mouseEvent)) {
			super.mouseMovedOrUp(x, y, mouseEvent);
		}
	}
	
	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char eventChar, int eventKey) {
		if (eventKey == Keyboard.KEY_ESCAPE)
			this.mc.displayGuiScreen(parentScreen);
		else
			this.entryList.keyTyped(eventChar, eventKey);
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
	public void drawScreen(int x, int y, float par3) {
		
		this.drawDefaultBackground();
		this.entryList.drawScreen(x, y, par3);
		
		this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);

		if (this.titleLine2 != null) {
			this.drawCenteredString(this.fontRendererObj, this.titleLine2, this.width / 2, 18, 16777215);
		}
		
		if (this.titleLine3 != null) {
			this.drawCenteredString(this.fontRendererObj, this.titleLine3, this.width / 2, 28, 16777215);
		}
		
		this.btnDone       .enabled = this.entryList.isListSavable();
		this.btnDefault    .enabled = enabled && !this.entryList.isDefault();
		this.btnUndoChanges.enabled = enabled && this.entryList.isChanged();
		
		super.drawScreen(x, y, par3);
		
		this.entryList.drawScreenPost(x, y, par3);

		if (this.tooltipHoverChecker != null && this.tooltipHoverChecker.checkHover(x, y)) {
			drawToolTip(this.toolTip, x, y);
		}
	}
	
	public void drawToolTip(List stringList, int x, int y) {
		this.func_146283_a(stringList, x, y);
	}

}
