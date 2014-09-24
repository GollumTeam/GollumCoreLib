package mods.gollum.core.client.gui.config;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;

import java.lang.reflect.Field;
import java.util.ArrayList;

import mods.gollum.core.client.gui.config.entries.GuiEditArrayCustomEntries;
import mods.gollum.core.client.gui.config.entries.GuiEditArrayEntriesProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiEditArray;
import cpw.mods.fml.client.config.GuiEditArrayEntries;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.config.HoverChecker;
import cpw.mods.fml.client.config.IConfigElement;

public class GuiEditCustomArray extends GuiEditArray {

	private final Object[] beforeValues;
	private Object[] currentValues;
	public GuiConfig parent;
	
	public GuiEditCustomArray(GuiConfig parent, IConfigElement configElement, int slotIndex, Object[] currentValues, boolean enabled) {
		super(parent, configElement, slotIndex, currentValues, enabled);
		
		this.parent        = parent;
		this.beforeValues  = currentValues;
		this.currentValues = currentValues;
	}
	
	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		
		GuiEditArrayCustomEntries entryList = new GuiEditArrayCustomEntries(this, this.mc, this.configElement, this.beforeValues, this.currentValues);
		
		GuiButtonExt btnUndoChanges, btnDefault, btnDone;
		
		int undoGlyphWidth  = mc.fontRenderer.getStringWidth(UNDO_CHAR) * 2;
		int resetGlyphWidth = mc.fontRenderer.getStringWidth(RESET_CHAR) * 2;
		int doneWidth       = Math.max(mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 20, 100);
		int undoWidth       = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.undoChanges")) + undoGlyphWidth + 20;
		int resetWidth      = mc.fontRenderer.getStringWidth(" " + I18n.format("fml.configgui.tooltip.resetToDefault")) + resetGlyphWidth + 20;
		int buttonWidthHalf = (doneWidth + 5 + undoWidth + 5 + resetWidth) / 2;
		this.buttonList.add(btnDone        = new GuiButtonExt(2000, this.width / 2 - buttonWidthHalf, this.height - 29, doneWidth, 20, I18n.format("gui.done")));
		this.buttonList.add(btnDefault     = new GuiUnicodeGlyphButton(2001, this.width / 2 - buttonWidthHalf + doneWidth + 5 + undoWidth + 5, this.height - 29, resetWidth, 20, " " + I18n.format("fml.configgui.tooltip.resetToDefault"), RESET_CHAR, 2.0F));
		this.buttonList.add(btnUndoChanges = new GuiUnicodeGlyphButton(2002, this.width / 2 - buttonWidthHalf + doneWidth + 5, this.height - 29, undoWidth, 20, " " + I18n.format("fml.configgui.tooltip.undoChanges"), UNDO_CHAR, 2.0F));
		

		try {
			Field f = GuiEditArray.class.getDeclaredField("entryList");
			f.setAccessible(true);
			f.set(this, new GuiEditArrayEntriesProxy(entryList));
			
			f = GuiEditArray.class.getDeclaredField("btnUndoChanges");
			f.setAccessible(true);
			f.set(this, btnUndoChanges);
			
			f = GuiEditArray.class.getDeclaredField("btnDefault");
			f.setAccessible(true);
			f.set(this, btnDefault);
			
			f = GuiEditArray.class.getDeclaredField("btnDone");
			f.setAccessible(true);
			f.set(this, btnDone);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
