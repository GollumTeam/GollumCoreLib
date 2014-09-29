package mods.gollum.core.client.gui.config.entry.logic;

import static mods.gollum.core.ModGollumCoreLib.log;

import java.awt.Color;
import java.lang.reflect.Field;

import mods.gollum.core.client.gui.config.GuiGollumConfig;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.ListEntryBase;
import cpw.mods.fml.client.config.GuiUtils;

public class GuiArrayButtonEntryLogic {
	
	private Minecraft mc;
	public GuiGollumConfig parent;
	
	protected ListEntryBase entry;

	protected GuiButtonExt btnAdd;
	protected GuiButtonExt btnRemove;
	
	public GuiArrayButtonEntryLogic(ListEntryBase entry) {
		
		this.mc = Minecraft.getMinecraft();
		
		this.entry      = entry;
		this.btnAdd     = new GuiButtonExt(0, 0, 0, 18, 18, "+");
		this.btnRemove  = new GuiButtonExt(0, 0, 0, 18, 18, "x");
		
		this.btnAdd    .packedFGColour = GuiUtils.getColorCode('2', true);
		this.btnRemove .packedFGColour = GuiUtils.getColorCode('c', true);
		
	}
	
	protected GuiGollumConfig getOwningScreen () {
		if (this.parent == null) {
			try {
				Field f = ListEntryBase.class.getDeclaredField("owningScreen");
				f.setAccessible(true);
				this.parent = (GuiGollumConfig) f.get(this.entry);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return this.parent;
	}
	
	protected GuiConfigEntries getOwningEntryList () {
		return this.getOwningScreen().entryList;
	}
	
	
	public void drawEntryBefore () {
		
		if (this.getOwningScreen().isArray()) {
			this.getOwningScreen().entryList.labelX -= 44;
			this.getOwningScreen().entryList.controlX -= 44;
			this.getOwningEntryList().scrollBarX -= 44;
		}
	}
	
	public void drawEntryAfter(int x, int y,int mouseX, int mouseY) {
		
		if (this.getOwningScreen().isArray()) {
			
			this.getOwningScreen().entryList.labelX += 44;
			this.getOwningEntryList().scrollBarX += 44;
			this.getOwningScreen().entryList.controlX += 44;
			
			this.btnAdd.xPosition = this.getOwningEntryList().scrollBarX - 44;
			this.btnAdd.yPosition = y;
			this.btnAdd.drawButton(this.mc, mouseX, mouseY);
			
			this.btnRemove.xPosition = this.getOwningEntryList().scrollBarX - 22;
			this.btnRemove.yPosition = y;
			this.btnRemove.drawButton(this.mc, mouseX, mouseY);
			
		}
	}

	public boolean mousePressed(int index, int x, int y) {
		
		if (this.getOwningScreen().isArray()) {
			
			if (this.btnAdd.mousePressed(this.mc, x, y)) {
				
				btnAdd.func_146113_a(this.mc.getSoundHandler());
				this.getOwningScreen().addNewEntry(index);
				
				return true;
			} else 
			if (this.btnRemove.mousePressed(this.mc, x, y)) {
				
				btnRemove.func_146113_a(this.mc.getSoundHandler());
				this.getOwningScreen().removeEntry(index);
				
				return true;
			}
			
		}
		
		return false;
	}
	
	public void mouseReleased(int x, int y) {
		this.btnAdd.mouseReleased(x, y);
		this.btnRemove.mouseReleased(x, y);
	}
	
}
