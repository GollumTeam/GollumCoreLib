package mods.gollum.core.client.gui.config.entry;

import java.util.Map.Entry;
import java.util.TreeMap;

import mods.gollum.core.client.gui.config.GuiConfigEntries;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.client.gui.config.entry.ConfigEntry.Event;
import mods.gollum.core.common.config.ConfigProp;
import mods.gollum.core.common.config.JsonConfigProp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUtils;

public class BuildingEntryTab extends ConfigEntry {

	private String selected = "";
	public int index = 0;
	private TreeMap<String, GuiButtonExt> values = new TreeMap<String, GuiButtonExt>();
	
	private GuiButtonExt btnPrev;
	private GuiButtonExt btnNext;

	private GuiButtonExt btnAdd;
	private GuiButtonExt btnRemove;
	
	private boolean modalEnabled = false;
	private GuiButtonExt btnAddDone;
	private ConfigEntry addEntry;
	

	public BuildingEntryTab(int index , Minecraft mc, GuiConfigEntries parent, ConfigElement configElement) {
		super(index, mc, parent, configElement);
		
		this.setValue (configElement.getValue());
		this.setIndex0();
		
		this.init ();
	}
	
	private void init() {
		
		this.btUndoIsVisible  = false;
		this.btResetIsVisible = false;
		
		this.btnPrev   = new GuiButtonExt(0, 0, 0, 10, 18, "<");
		this.btnNext   = new GuiButtonExt(0, 0, 0, 10, 18, ">");
		this.btnAdd    = new GuiButtonExt(0, 0, 0, 18, 18, "+");
		this.btnRemove = new GuiButtonExt(0, 0, 0, 18, 18, "x");
		
		this.btnAdd   .packedFGColour = GuiUtils.getColorCode('2', true);
		this.btnRemove.packedFGColour = GuiUtils.getColorCode('c', true);
		
		this.btnAddDone = new GuiButtonExt(0, 0, 0, mc.fontRenderer.getStringWidth(I18n.format("gui.done")) + 10, 18, I18n.format("gui.done"));
		this.addEntry   = this.createSubEntry("", 0, 0, 0);
		
		this.addEntry.btUndoIsVisible  = false;
		this.addEntry.btResetIsVisible = false;
	}

	@Override
	public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tessellator, int mouseX, int mouseY, boolean isSelected, boolean resetControlWidth) {
		
		super.drawEntry(slotIndex, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected, resetControlWidth);
		
		if (this.modalEnabled) {
			
			this.btnAddDone.xPosition = this.parent.controlX + this.parent.controlWidth - this.btnAddDone.width;
			this.btnAddDone.yPosition = y;
			this.btnAddDone.drawButton(this.mc, mouseX, mouseY);
			
			this.parent.controlWidth -= this.btnAddDone.width;
			this.addEntry.drawEntry(0, x, y, listWidth, slotHeight, tessellator, mouseX, mouseY, isSelected, false);
			this.parent.controlWidth += this.btnAddDone.width;
			
		} else {
			
			this.btnPrev.xPosition = this.parent.controlX;
			this.btnPrev.yPosition = y;
			this.btnPrev.drawButton(this.mc, mouseX, mouseY);
			
			int btX = 10;
			for (Entry<String, GuiButtonExt> entry : this.values.entrySet()){
				
				GuiButtonExt bt = entry.getValue();
				
				bt.enabled = !bt.displayString.equals(this.selected);
				
				bt.enabled = !bt.enabled;
				bt.xPosition = this.parent.controlX + btX;
				bt.yPosition = y;
				bt.drawButton(mc, mouseX, mouseY);
				bt.enabled = !bt.enabled;
				
				if (!bt.enabled) {
					this.drawRec(bt.xPosition+1, y+15, 1, 3, 0xFF7E7E7E);
					this.drawRec(bt.xPosition+2, y+15, bt.width-3, 3, 0xFF525252);
					this.drawRec(bt.xPosition+bt.width-2, y+15, 1, 3, 0xFF3F3F3F);
				}
				
				btX += bt.width - 1;
			}
			
			this.btnNext.xPosition = this.parent.controlX + this.parent.controlWidth - 48;
			this.btnNext.yPosition = y;
			this.btnNext.drawButton(this.mc, mouseX, mouseY);
			
			this.btnAdd.xPosition = this.parent.controlX + this.parent.controlWidth - 18;
			this.btnAdd.yPosition = y;
			this.btnAdd.drawButton(this.mc, mouseX, mouseY);
			
			this.btnRemove.xPosition = this.parent.controlX + this.parent.controlWidth - 37;
			this.btnRemove.yPosition = y;
			this.btnRemove.drawButton(this.mc, mouseX, mouseY);
			
		}
		
	}
	
	@Override
	 public Object getValue() {
		super.getValue();
		return this.values.keySet().toArray(new String[0]);
	}
	
	@Override
	public ConfigEntry setValue(Object values) {
		this.values.clear();
		for (String value : (String[])values) {
			this.addValues(value);
		}
		return super.setValue(values);
	}
	
	public ConfigEntry addValues(String value) {
		
		this.values.put(value, new GuiButtonExt(0, parent.controlX, 0, mc.fontRenderer.getStringWidth(value)+10, 18, value));
		this.select (value);
		
		return this;
	}

	private void prev() {
		this.index--;
		if (this.index < 0) {
			this.index = 0;
		}
	}
	
	private void next() {
		this.index++;
	}
	
	private void remove() {
		if (this.values.containsKey(this.selected)) {
			this.values.remove(this.selected);
		}
		this.setIndex0 ();
	}
	
	public void setIndex0 () {
		if (this.values.size() > 0) {
			this.select (this.values.firstKey());
			return;
		}
		this.select (null);
	}
	
	public void select (String value) {
		this.selected = value;
		this.fireEvent(Event.Type.CHANGE);
	}
	
	public String getSelected () {
		return this.selected;
	}
	
	/////////////
	// ACTIONS //
	/////////////
	
	/**
	 * Returns true if the mouse has been pressed on this control.
	 */
	@Override
	public boolean mousePressed(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {
		
		if (this.modalEnabled) {
			if (this.btnAddDone.mousePressed(this.mc, x, y)) {
				btnAddDone.func_146113_a(mc.getSoundHandler());
				this.modalEnabled = false;
				
				String tab = this.addEntry.getValue().toString().trim();
				this.addValues(tab);
				
				return true;
			}
			if (this.addEntry.mousePressed(index, x, y, mouseEvent, relativeX, relativeY)) {
				return true;
			}
		} else {
			if (this.btnNext.mousePressed(this.mc, x, y)) {
				btnNext.func_146113_a(mc.getSoundHandler());
				this.next();
				return true;
			}
			if (this.btnPrev.mousePressed(this.mc, x, y)) {
				btnPrev.func_146113_a(mc.getSoundHandler());
				this.prev();
				return true;
			}
			if (this.btnAdd.mousePressed(this.mc, x, y)) {
				btnAdd.func_146113_a(mc.getSoundHandler());
				this.modalEnabled = true;
				return true;
			}
			if (this.btnRemove.mousePressed(this.mc, x, y)) {
				btnRemove.func_146113_a(mc.getSoundHandler());
				this.remove();
				return true;
			}
			for (Entry<String, GuiButtonExt> entry : this.values.entrySet()){
				
				GuiButtonExt bt = entry.getValue();
				
				if (bt.mousePressed(this.mc, x, y)) {
					bt.func_146113_a(mc.getSoundHandler());
					this.select (bt.displayString);
					return true;
				}
			}
		}
		
		
		return super.mousePressed(index, x, y, mouseEvent, relativeX, relativeY);
	}
	
	/**
	 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
	 */
	@Override
	public void mouseReleased(int index, int x, int y, int mouseEvent, int relativeX, int relativeY) {

		if (this.modalEnabled) {
			this.btnAddDone.mouseReleased(x, y);
			this.addEntry.mouseReleased(index, x, y, mouseEvent, relativeX, relativeY);
		} else {
			this.btnNext.mouseReleased(x, y);
			this.btnPrev.mouseReleased(x, y);
		}
	}
	
}
