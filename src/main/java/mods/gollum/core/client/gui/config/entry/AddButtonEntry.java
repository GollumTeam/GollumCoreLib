package mods.gollum.core.client.gui.config.entry;

import static mods.gollum.core.ModGollumCoreLib.log;
import mods.gollum.core.client.gui.config.GuiEditCustomArray;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.config.GuiConfigEntries.IConfigEntry;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.client.config.IConfigElement;

public class AddButtonEntry implements IConfigEntry {
	
	protected GuiEditCustomArray parent;
	protected GuiButtonExt btnAdd;
	
	public AddButtonEntry(GuiEditCustomArray parent) {
		
		this.parent = parent;
		this.btnAdd = new GuiButtonExt(0, 0, 0, 18, 18, "+");
		this.btnAdd    .packedFGColour = GuiUtils.getColorCode('2', true);
		
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected) {
		
		this.btnAdd.xPosition = this.parent.entryList.scrollBarX - 44;
		this.btnAdd.yPosition = y;
		this.btnAdd.drawButton(this.parent.mc, mouseX, mouseY);
		
		
	}

	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		
		if (this.btnAdd.mousePressed(this.parent.mc, x, y)) {
			
			btnAdd.func_146113_a(this.parent.mc.getSoundHandler());
			this.parent.addNewEntry(index);
			
			return true;
		}
		
		return false;
	}

	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		this.btnAdd.mouseReleased(x, y);
	}

	@Override
	public IConfigElement getConfigElement() {
		return null;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public Object getCurrentValue() {
		return null;
	}

	@Override
	public Object[] getCurrentValues() {
		return null;
	}

	@Override
	public boolean enabled() {
		return true;
	}

	@Override
	public void keyTyped(char eventChar, int eventKey) {
	}

	@Override
	public void updateCursorCounter() {
	}

	@Override
	public void mouseClicked(int x, int y, int mouseEvent) {
	}

	@Override
	public boolean isDefault() {
		return true;
	}

	@Override
	public void setToDefault() {
	}

	@Override
	public void undoChanges() {
	}

	@Override
	public boolean isChanged() {
		return false;
	}

	@Override
	public boolean saveConfigElement() {
		return false;
	}

	@Override
	public void drawToolTip(int mouseX, int mouseY) {
	}

	@Override
	public int getLabelWidth() {
		return 0;
	}

	@Override
	public int getEntryRightBound() {
		return 0;
	}

	@Override
	public void onGuiClosed() {
	}

}
