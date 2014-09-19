package mods.gollum.core.client.gui.config.entries;

import static cpw.mods.fml.client.config.GuiUtils.RESET_CHAR;
import static cpw.mods.fml.client.config.GuiUtils.UNDO_CHAR;
import mods.gollum.core.client.gui.config.GuiConfigEntries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.client.config.GuiButtonExt;


public abstract class GuiConfigEntryString implements IGuiListEntry {
		
		public GuiConfigEntries parent;
		private Minecraft mc;
		public String label;
		public String originalValue;
		public String defaultValue;
		public String match;
		public Object[] params;
		
		private boolean isInit = false;
		
		protected final GuiButtonExt btnUndoChanges;
		protected final GuiButtonExt btnDefault;
		private GuiTextField textFieldValue;
		
		public GuiConfigEntryString (GuiConfigEntries parent, Minecraft mc, String label, String value, String defaultValue, String match, Object[] params) {
			this.parent        = parent;
			this.mc            = mc;
			this.label         = label;
			this.originalValue = value;
			this.defaultValue  = defaultValue;
			this.match         = match;
			this.params        = params;
			
			this.btnUndoChanges = new GuiButtonExt(0, 0, 0, 18, 18, UNDO_CHAR);
			this.btnDefault     = new GuiButtonExt(0, 0, 0, 18, 18, RESET_CHAR);
			
			this.textFieldValue = new GuiTextField(this.mc.fontRenderer, this.parent.controlX + 1, 0, this.parent.controlWidth - 3, 16);
			this.textFieldValue.setMaxStringLength(10000);
			this.textFieldValue.setText(value);
			
			
		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
			
			EnumChatFormatting color = EnumChatFormatting.GRAY;
			color = (isChanged())     ? EnumChatFormatting.WHITE : color;
			color = (!isValidValue()) ? EnumChatFormatting.RED   : color;
			
			this.mc.fontRenderer.drawString(color+this.label, this.parent.labelX, y + slotHeight / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2, 0x000000);
			
			this.btnUndoChanges.xPosition = this.parent.scrollBarX - 44;
			this.btnUndoChanges.yPosition = y;
			this.btnUndoChanges.enabled = this.isChanged ();
			this.btnUndoChanges.drawButton(this.mc, mouseX, mouseY);

			this.btnDefault.xPosition = this.parent.scrollBarX - 22;
			this.btnDefault.yPosition = y;
			this.btnDefault.enabled = !this.isDefault();
			this.btnDefault.drawButton(this.mc, mouseX, mouseY);
			
			this.textFieldValue.xPosition = this.parent.controlX + 2;
			this.textFieldValue.yPosition = y + 1;
			this.textFieldValue.width = this.parent.controlWidth - 4;
			this.textFieldValue.drawTextBox();
			
			// Fixe display text 
			if (!isInit) { this.textFieldValue.setCursorPosition(this.textFieldValue.getCursorPosition()); isInit = true; }
			this.textFieldValue.drawTextBox();
		}
		
		public void updateCursorCounter() {
			this.textFieldValue.updateCursorCounter();
		}
		
		public void mouseClicked(int x, int y, int mouseEvent) {
			this.textFieldValue.mouseClicked(x, y, mouseEvent);
		}
		
		public void keyTyped(char eventChar, int eventKey) {
			this.textFieldValue.textboxKeyTyped(eventChar, eventKey);
			this.onChange();
		}
		
		public int getEntryRightBound() {
			return this.parent.resetX + 40;
		}
		
		public String getValue () {
			return this.textFieldValue.getText().trim();
		}
		
		public boolean isChanged() {
			return !this.getValue().equals (this.originalValue);
		}
		
		public boolean isDefault() {
			return this.getValue().equals (this.defaultValue);
		}
		
		public boolean isValidValue() {
			return this.getValue().matches(this.match);
		}
		
		@Override
		public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
			if (this.btnDefault.mousePressed(this.mc, x, y)) {
				this.btnDefault.func_146113_a(this.mc.getSoundHandler());
				this.setToDefault();
				return true;
			} else if (this.btnUndoChanges.mousePressed(this.mc, x, y)) {
				this.btnUndoChanges.func_146113_a(this.mc.getSoundHandler());
				this.undoChanges();
				return true;
			}
			return false;
		}
		
		public void undoChanges() {
			this.textFieldValue.setText(this.originalValue);
		}

		public void setToDefault() {
			this.textFieldValue.setText(this.defaultValue);
		}

		@Override
		public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {
			// TODO Auto-generated method stub
			
		}
		
		protected abstract void onChange ();
		
	}