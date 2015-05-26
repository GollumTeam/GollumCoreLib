package com.gollum.core.client.gui.achievement;

import static com.gollum.core.ModGollumCoreLib.i18n;
import static com.gollum.core.ModGollumCoreLib.log;

import java.util.Iterator;

import scala.collection.mutable.HashMap;

import com.gollum.core.common.stats.StatsPage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.stats.StatList;

public class GollumGuiStats extends GuiStats {
	
	private StatFileWriter statFileWriter;
	
	protected GuiButton buttonMod;
	protected GuiButton buttonModPrev;
	protected GuiButton buttonModNext;
	protected String    currenTButton;
	
	protected HashMap<String, GuiSlot> slots = new HashMap<String, GuiSlot>();
	
	public GollumGuiStats(GuiScreen parent, StatFileWriter statFileWriter) {
		super(parent, statFileWriter);
		this.statFileWriter = statFileWriter;
	}
	
	public void func_146541_h() {
		
		super.func_146541_h ();

		this.init ();
		
		StatsPage page = null;
		for (StatsPage p : StatsPage.getStatsPages()) {
			page = p;
			break;
		}
		
		if (page != null) {
			this.currenTButton = page.getName();
			this.buttonMod     = new GuiButton(10, this.width / 2 - 160, this.height - 28, 120, 20, page.getName());
			this.buttonModPrev = new GuiButton(11, this.width / 2 - 172, this.height - 28, 12, 20, "<");
			this.buttonModNext = new GuiButton(12, this.width / 2 - 40, this.height - 28, 12, 20, ">");
			
			this.buttonList.add(this.buttonMod);
			this.buttonList.add(this.buttonModPrev);
			this.buttonList.add(this.buttonModNext);
		}
		
	}
	
	protected void init() {
		
		for (StatsPage p : StatsPage.getStatsPages()) {
			
		}
		
        this.field_146550_h = new GuiStats.StatsGeneral();
        this.field_146550_h.registerScrollButtons(1, 1);
	}

	protected void actionPerformed(GuiButton button) {
		
		if (button.id == 10) {
//			 this.field_146545_u = this.field_146550_h;
			log.debug("COOOOOOLLLLL");
			
		} else if (button.id == 11) {
			log.debug("Prev");
		} else if (button.id == 12) {
			log.debug("Next");
		} else {
			super.actionPerformed(button);
		}
	}
	
	@SideOnly(Side.CLIENT)
	class StatsCustom extends GuiSlot {
		
		protected String name;
		
		public StatsCustom(String name) {
			super(GollumGuiStats.this.mc, GollumGuiStats.this.width, GollumGuiStats.this.height, 32, GollumGuiStats.this.height - 64, 10);
			this.name = name;
			this.setShowSelectionBox(false);
		}
		
		protected int getSize() {
			return StatList.generalStats.size();
		}
		
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_) {}
		
		protected boolean isSelected(int p_148131_1_) {
			return false;
		}
		
		protected int getContentHeight() {
			return this.getSize() * 10;
		}
		
		protected void drawBackground() {
			GollumGuiStats.this.drawDefaultBackground();
		}
		
		protected void drawSlot(int slot, int x, int y, int p_148126_4_, Tessellator tessellator, int p_148126_6_, int p_148126_7_) {
			StatBase statbase = (StatBase)StatList.generalStats.get(slot);
			GollumGuiStats.this.drawString(GollumGuiStats.this.fontRendererObj, statbase.func_150951_e().getUnformattedText(), x + 2, y + 1, slot % 2 == 0 ? 16777215 : 9474192);
			String s = statbase.func_75968_a(GollumGuiStats.this.statFileWriter.writeStat(statbase));
			GollumGuiStats.this.drawString(GollumGuiStats.this.fontRendererObj, s, x + 2 + 213 - GollumGuiStats.this.fontRendererObj.getStringWidth(s), y + 1, slot % 2 == 0 ? 16777215 : 9474192);
		}
	}
}
