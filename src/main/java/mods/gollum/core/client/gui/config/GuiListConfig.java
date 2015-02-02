package mods.gollum.core.client.gui.config;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.client.gui.config.element.ConfigElement;
import mods.gollum.core.client.gui.config.element.ListElement;
import mods.gollum.core.client.gui.config.entry.ListEntry;
import net.minecraft.client.gui.GuiTextField;

public class GuiListConfig extends GuiConfig {
	
	protected ListEntry parentEntry;
	protected GuiTextField search = null;
	public String currentValue;
	
	public GuiListConfig(ListEntry listEntry) {
		super(listEntry.parent.parent);
		this.parentEntry  = listEntry;
		this.titleLine2   = ((GuiConfig)this.getParent()).titleLine2 + " > " + listEntry.getLabel();
		this.currentValue = this.parentEntry.getValue().toString();
	}
	
	@Override
	public boolean displayEntriesLabel() {
		return false;
	}
	
	@Override
	protected void initConfigElement() {
		
		String search = "";
		
		if (this.search != null) {
			search = this.search.getText().trim();
		}
		
		this.configElements = new ArrayList<ConfigElement>();
		String[] values = this.parentEntry.configElement.getConfigProp().validValues();
		
		for (String value : values) {
			if (search.equals("") || value.indexOf(search) != -1) {
				this.configElements.add(new ListElement(value, value));
			}
		}
 	}
	
	@Override
	public void initGui() {
		
		super.initGui();
		
		this.search = new GuiTextField(this.mc.fontRenderer, 0, 0, 0, 16);
		
	}
	
	public int getTopEntryList() {
		return super.getTopEntryList() + 20;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if (this.parentEntry.hasSearch()) {
			
			this.search.xPosition = this.entryList.controlX;
			this.search.yPosition = super.getTopEntryList();
			this.search.width = this.entryList.scrollBarX - this.entryList.controlX - 4;
			this.search.height = 16;
			this.search.drawTextBox();
			if (this.search.getText().equals("")) {
				this.drawString(this.fontRendererObj, ModGollumCoreLib.i18n.trans("config.search"), this.search.xPosition + 4, this.search.yPosition + 4, 0x444444);
			}
		}
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
	protected void mouseClicked(int x, int y, int mouseEvent) {
		super.mouseClicked(x, y, mouseEvent);
		this.search.mouseClicked(x, y, mouseEvent);
	}
	
}
