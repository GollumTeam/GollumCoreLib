package com.gollum.core.client.gui.achievement;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gollum.core.common.stats.StatsPage;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GollumGuiStats extends GuiStats {
	
	private StatisticsManager stats;
	
	protected GuiButton buttonMod;
	protected GuiButton buttonModPrev;
	protected GuiButton buttonModNext;
	protected String    currentButton;
	protected int       currentIndex = 0;
	protected boolean   isCustomStat = false;
	
	protected GuiSlot slotGeneral;
	protected HashMap<String, GuiSlot> customSlots = new HashMap<String, GuiSlot>();
	
	public GollumGuiStats(GuiScreen parent, StatisticsManager stats) {
		super(parent, stats);
		this.stats = stats;
	}
	
	@Override
	public void initLists() {
		
		super.initLists ();
		
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
			for (Field f: GuiStats.class.getDeclaredFields()) {
				if (f.getType() == GuiSlot.class) {
					f.setAccessible(true);
					f.set(this, slot);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		
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
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_) {}
		
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
		protected void drawSlot(int slot, int x, int y, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
			StatBase statbase = StatsPage.getStatsPage(this.name).getStats().get(slot);
			GollumGuiStats.this.drawString(GollumGuiStats.this.fontRenderer, statbase.getStatName().getUnformattedText(), x + 2, y + 1, slot % 2 == 0 ? 16777215 : 9474192);
			String s = statbase.format(GollumGuiStats.this.stats.readStat(statbase));
			GollumGuiStats.this.drawString(GollumGuiStats.this.fontRenderer, s, x + 2 + 213 - GollumGuiStats.this.fontRenderer.getStringWidth(s), y + 1, slot % 2 == 0 ? 16777215 : 9474192);
		}
	}
	
	@SideOnly(Side.CLIENT)
	class StatsGeneralCustom extends GuiSlot {
		
		private List<StatBase> generalStats;
		
		public StatsGeneralCustom() {
			super(GollumGuiStats.this.mc, GollumGuiStats.this.width, GollumGuiStats.this.height, 32, GollumGuiStats.this.height - 64, 10);
			this.setShowSelectionBox(false);
			
			this.generalStats = new ArrayList<StatBase>();
			for (Object stat : StatList.BASIC_STATS) {
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
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_) {}
		
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
		protected void drawSlot(int slot, int x, int y, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
			StatBase statbase = this.generalStats.get(slot);
			GollumGuiStats.this.drawString(GollumGuiStats.this.fontRenderer, statbase.getStatName().getUnformattedText(), x + 2, y + 1, slot % 2 == 0 ? 16777215 : 9474192);
			String s = statbase.format(GollumGuiStats.this.stats.readStat(statbase));
			GollumGuiStats.this.drawString(GollumGuiStats.this.fontRenderer, s, x + 2 + 213 - GollumGuiStats.this.fontRenderer.getStringWidth(s), y + 1, slot % 2 == 0 ? 16777215 : 9474192);
		}
	}
}
