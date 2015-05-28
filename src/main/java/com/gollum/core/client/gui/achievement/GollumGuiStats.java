package com.gollum.core.client.gui.achievement;

import static com.gollum.core.ModGollumCoreLib.i18n;
import static com.gollum.core.ModGollumCoreLib.log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
	protected String    currentButton;
	protected int       currentIndex = 0;
	protected boolean   isCustomStat = false;
	
	protected GuiSlot slotGeneral;
	protected HashMap<String, GuiSlot> customSlots = new HashMap<String, GuiSlot>();
	
	public GollumGuiStats(GuiScreen parent, StatFileWriter statFileWriter) {
		super(parent, statFileWriter);
		this.statFileWriter = statFileWriter;
	}
	
	@Override
	public void initGui () {
		
		super.initGui ();
		
		this.init ();
		
		StatsPage page = null;
		for (StatsPage p : StatsPage.getStatsPages()) {
			page = p;
			break;
		}
		
		if (page != null) {
			this.currentIndex  = 0;
			this.currentButton = page.getName();
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
			GuiSlot slot = new StatsCustom(p.getName());
			slot.registerScrollButtons(1, 1);
			this.customSlots.put(p.getName(), slot);
		}
		this.slotGeneral = new StatsGeneralCustom();
		this.slotGeneral.registerScrollButtons(1, 1);
		
		this.setCurentSlot(this.slotGeneral);
	}
	
	protected void setCurentSlot (GuiSlot slot) {
		try {
			Field f = GuiStats.class.getDeclaredField("field_146545_u");
			f.setAccessible(true);
			f.set(this, slot);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		
		
		if (button.id == 10) {
			
			this.setCurentSlot(this.customSlots.get(this.currentButton));
			this.isCustomStat = true;
			
		} else if (button.id == 11) {
			
			this.currentIndex = (this.currentIndex + StatsPage.getStatsPages().size()-1) % StatsPage.getStatsPages().size();
			this.currentButton = StatsPage.getStatsPages().get(this.currentIndex).getName();
			this.buttonMod.displayString = this.currentButton;
			if (this.isCustomStat) {
				this.setCurentSlot(this.customSlots.get(this.currentButton));
			} else {
				this.isCustomStat = false;
			}
			
		} else if (button.id == 12) {
			
			this.currentIndex = (this.currentIndex + 1) % StatsPage.getStatsPages().size();
			this.currentButton = StatsPage.getStatsPages().get(this.currentIndex).getName();
			this.buttonMod.displayString = this.currentButton;
			if (this.isCustomStat) {
				this.setCurentSlot(this.customSlots.get(this.currentButton));
			} else {
				this.isCustomStat = false;
			}
			
		} else if (button.id == 1) {
			
			this.setCurentSlot (this.slotGeneral);
			this.isCustomStat = false;
			
		} else {
			
			this.isCustomStat = false;
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
		
		@Override
		protected int getSize() {
			return StatsPage.getStatsPage(this.name).getStats().size();
		}
		
		@Override
		protected void elementClicked(int i, boolean flag) {}
		
		@Override
		protected boolean isSelected(int p_148131_1_) {
			return false;
		}
		
		@Override
		protected int getContentHeight() {
			return this.getSize() * 10;
		}
		
		@Override
		protected void drawBackground() {
			GollumGuiStats.this.drawDefaultBackground();
		}
		
		@Override
		protected void drawSlot(int slot, int x, int y, int p_148126_4_, Tessellator tessellator) {
//			StatBase statbase = StatsPage.getStatsPage(this.name).getStats().get(slot);
//			GollumGuiStats.this.drawString(GollumGuiStats.this.fontRenderer, statbase.func_150951_e().getUnformattedText(), x + 2, y + 1, slot % 2 == 0 ? 16777215 : 9474192);
//			String s = statbase.func_75968_a(GollumGuiStats.this.statFileWriter.writeStat(statbase));
//			GollumGuiStats.this.drawString(GollumGuiStats.this.fontRenderer, s, x + 2 + 213 - GollumGuiStats.this.fontRenderer.getStringWidth(s), y + 1, slot % 2 == 0 ? 16777215 : 9474192);
		}
	}
	
	@SideOnly(Side.CLIENT)
	class StatsGeneralCustom extends GuiSlot {
		
		private List<StatBase> generalStats;
		
		public StatsGeneralCustom() {
			super(GollumGuiStats.this.mc, GollumGuiStats.this.width, GollumGuiStats.this.height, 32, GollumGuiStats.this.height - 64, 10);
			this.setShowSelectionBox(false);
			
			this.generalStats = new ArrayList<StatBase>();
			for (Object stat : StatList.generalStats) {
				if (stat instanceof StatBase) {
					if (!StatsPage.inPages((StatBase) stat)) {
						this.generalStats.add((StatBase) stat);
					}
				}
			}
		}
		
		@Override
		protected int getSize() {
			return this.generalStats.size();
		}
		
		@Override
		protected void elementClicked(int i, boolean flag) {}
		
		@Override
		protected boolean isSelected(int p_148131_1_) {
			return false;
		}
		
		@Override
		protected int getContentHeight() {
			return this.getSize() * 10;
		}
		
		protected void drawBackground() {
			GollumGuiStats.this.drawDefaultBackground();
		}
		
		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5) {
//			StatBase statbase = this.generalStats.get(p_148126_1_);
//			GollumGuiStats.this.drawString(GollumGuiStats.this.fontRenderer, statbase.func_150951_e().getUnformattedText(), p_148126_2_ + 2, p_148126_3_ + 1, p_148126_1_ % 2 == 0 ? 16777215 : 9474192);
//			String s = statbase.func_75968_a(GollumGuiStats.this.statFileWriter.writeStat(statbase));
//			GollumGuiStats.this.drawString(GollumGuiStats.this.fontRenderer, s, p_148126_2_ + 2 + 213 - GollumGuiStats.this.fontRenderer.getStringWidth(s), p_148126_3_ + 1, p_148126_1_ % 2 == 0 ? 16777215 : 9474192);
		}
	}
}
