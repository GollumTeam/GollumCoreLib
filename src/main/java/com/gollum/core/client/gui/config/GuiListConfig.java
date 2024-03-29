package com.gollum.core.client.gui.config;

import java.io.IOException;
import java.util.ArrayList;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.client.gui.config.element.ConfigElement;
import com.gollum.core.client.gui.config.element.ListElement;
import com.gollum.core.client.gui.config.entry.ListEntry;

import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;

public class GuiListConfig extends GuiConfig {
	
	public static class GuiButtonSlot extends GuiSlot {

		private GuiListConfig parent;
		private ArrayList<String> values;
		private ArrayList<GuiButtonExt> buttons = new ArrayList<GuiButtonExt>();
		private int isCliqued = 0;

		public GuiButtonSlot(GuiListConfig parent, ArrayList<String> values) {
			super(parent.mc, parent.entryList.controlX + 40, parent.height, parent.getTopEntryList(), parent.height - 32, 20);
			this.parent = parent;
			this.values = values;
			
			this.left += 30;
			this.width = this.right - this.left;
			
			this.init();
		}

		private void init() {
			int i = 0;
			this.buttons.clear();
			this.buttons.add(new GuiButtonExt(7000, 0, 0, this.parent.parentEntry.tradIfExist("all")));
			for (String value: this.values) {
				this.buttons.add(new GuiButtonExt(7000+i++, 0, 0, value));
			}
		}

		@Override
		protected int getScrollBarX() {
			return this.right;
		}
		
		@Override
		protected int getSize() {
			return this.buttons.size();
		}

		@Override
		protected void elementClicked(int slotIndex, boolean doubleClick, int mouseX, int mouseY) {
			if (this.buttons.get(slotIndex).mousePressed(this.parent.mc, mouseX, mouseY)) {
				this.buttons.get(slotIndex).playPressSound(this.parent.mc.getSoundHandler());
				this.isCliqued = slotIndex;
				if (slotIndex == 0) {
					this.parent.currentGroup = this.parent.parentEntry.tradIfExist("all");
				} else {
					this.parent.currentGroup = this.values.get(slotIndex-1);
				}
				this.parent.resetEntries();
			}
		}

		@Override
		protected boolean isSelected(int slotIndex) {
			return false;
		}

		protected boolean isCliqued(int slotIndex) {
			return this.isCliqued == slotIndex;
		}

		@Override
		protected void drawBackground() {
		}

		@Override
		protected void drawSlot(int slotIndex, int x, int y, int slotHeight , int mouseX, int mouseY, float partial) {
			GuiButtonExt button = this.buttons.get(slotIndex);
			button.x = this.left + 5;
			button.y = y;
			button.width = this.width - 10;
			button.height = slotHeight;
			button.enabled = !this.isCliqued(slotIndex);
			button.drawButton(this.parent.mc, mouseX, mouseY, partial);
		}
		
	}
	
	protected ListEntry parentEntry;
	protected GuiTextField search = null;
	protected String searchValue = "";
	public Object currentValue;
	public String currentGroup;
	
	GuiButtonSlot groupList = null;
	private ArrayList<String> groups = null;
	
	public GuiListConfig(ListEntry listEntry) {
		super(listEntry.parent.parent);
		this.parentEntry  = listEntry;
		this.titleLine2   = ((GuiConfig)this.getParent()).titleLine2 + " > " + listEntry.getLabel();
		this.currentValue = this.parentEntry.getValue();
		this.currentGroup = this.parentEntry.tradIfExist("all");
	}
	
	@Override
	public boolean displayEntriesLabel() {
		return false;
	}
	
	@Override
	protected void initConfigElement() {
		
		String[] values = this.parentEntry.configElement.getConfigProp().validValues();
		
		for (String value : values) {
			this.configElements.add(new ListElement(value, value));
		}
		
		this.filter();
 	}
	
	public void runSlotAction(int slotIndex, GuiButtonExt btAction, boolean doubleClick, int mouseX, int mouseY) {
	}
	
	protected void filter() {
		for (ConfigElement complement : new ArrayList<ConfigElement>(this.configElements)) {
			if (
				!this.searchValue.equals("") &&
				complement.getValue().toString().indexOf(searchValue) == -1 &&
				((ListElement)complement).label.indexOf(searchValue) == -1
			) {
				this.configElements.remove(complement);
			}
			if (!this.currentGroup.equals(this.parentEntry.tradIfExist("all"))) {
				if (!this.currentGroup.equals(((ListElement)complement).group)) {
					this.configElements.remove(complement);
				}
			}
		}
	}
	
	public ArrayList<String> getAllGroup () {
		
		if (this.groups == null) {
			this.groups = new ArrayList<String>();
			
			for (ConfigElement complement : new ArrayList<ConfigElement>(this.configElements)) {
				String group = ((ListElement)complement).group;
				if (!this.groups.contains(group)) {
					this.groups.add(group);
				}
			}
		}
		return this.groups;
	}

	public boolean showGroupList () {
		return this.getAllGroup().size() > 1;
	}

	@Override
	public void initGui() {
		
		super.initGui();
		
		this.search = new GuiTextField(100, this.mc.fontRenderer, 0, 0, 0, 16);
		this.groupList = new GuiButtonSlot(this, this.getAllGroup());
	}
	
	public int getTopEntryList() {
		return super.getTopEntryList() + (this.parentEntry.hasSearch() ? 20 : 0);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		if (this.showGroupList()) {
			this.entryList.controlX += 50;
			this.entryList.left = this.entryList.controlX - 5;
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if (this.showGroupList()) {
			this.entryList.controlX -= 50;
			this.groupList.drawScreen(mouseX, mouseY, partialTicks);
			this.btDone.drawButton(mc, mouseX, mouseY, partialTicks);
		}
		
		if (this.parentEntry.hasSearch()) {
			
			this.search.x = this.entryList.controlX;
			this.search.y = super.getTopEntryList();
			this.search.width = this.entryList.scrollBarX - this.entryList.controlX - 4;
			this.search.height = 16;
			this.search.drawTextBox();
			if (this.search.getText().equals("")) {
				this.drawString(this.fontRenderer, ModGollumCoreLib.i18n.trans("config.search"), this.search.x + 9, this.search.y + 4, 0x444444);
			}
			
			if (!this.searchValue.equals(this.search.getText().trim())) {
				this.searchValue = this.search.getText().trim();
				this.resetEntries();
			}
			
		}
		
	}
	
	public void resetEntries() {
		this.configElements = new ArrayList<ConfigElement>();
		this.entryList = null;
		this.needsRefresh = true;
		super.initGui();
	}
	
	@Override
	public void saveValue() {
		this.parentEntry.setValue(this.currentValue);
	}
	
	@Override
	protected boolean isChanged() {
		return !this.currentValue.equals(this.parentEntry.configElement.getValue());
	}
	
	@Override
	protected boolean isDefault() {
		return this.currentValue.equals(this.parentEntry.configElement.getDefaultValue());
	}
	
	@Override
	protected void setToDefault() {
		
		Object defaultValue = this.parentEntry.configElement.getDefaultValue();
		
		for (int i = 0; i < this.entryList.getSize(); i++) {
			if (this.entryList.getEntry(i).getValue().equals(defaultValue)) {
				this.entryList.setSlot(i);
				break;
			}
		}
	}
	
	@Override
	protected void undoChanges() {
		
		Object value = this.parentEntry.configElement.getValue();
		
		for (int i = 0; i < this.entryList.getSize(); i++) {
			if (this.entryList.getEntry(i).getValue().equals(value)) {
				this.entryList.setSlot(i);
				break;
			}
		}
	}
	
	 @Override
	public void keyTyped(char eventChar, int eventKey) {
		this.search.textboxKeyTyped(eventChar, eventKey);
		super.keyTyped(eventChar, eventKey);
	}
	 
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.search.updateCursorCounter();
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.groupList.handleMouseInput();
	}
	
	@Override
	protected void mouseClicked(int x, int y, int mouseEvent) throws IOException {
		super.mouseClicked(x, y, mouseEvent);
		this.search.mouseClicked(x, y, mouseEvent);
	}
}
