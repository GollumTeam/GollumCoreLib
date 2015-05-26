package com.gollum.core.common.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.stats.StatBasic;
import net.minecraftforge.common.AchievementPage;

public class StatsPage {
	
	protected static TreeMap<String, StatsPage> statsPages = new TreeMap<String, StatsPage>();
	
	protected String name;
	protected ArrayList<StatBasic> statsBasics;
	
	public StatsPage(String name, StatBasic... statsBasics) {
		this.name        = name;
		this.statsBasics =  new ArrayList<StatBasic>(Arrays.asList(statsBasics));
	}
	
	public static void registerStatsPage(StatsPage page) {
		if (getStatsPage(page.getName()) != null) {
			throw new RuntimeException("Duplicate stats page name \"" + page.getName() + "\"!");
		}
		statsPages.put(page.getName(), page);
	}
	
	public static StatsPage getStatsPage(String name) {
		if (statsPages.containsKey(name)) {
			return statsPages.get(name);
		}
		return null;
	}
	
	public static Collection<StatsPage> getStatsPages() {
		return statsPages.values();
	}
	
	public static boolean inPages(StatBasic stat) {
		for (StatsPage page : getStatsPages()) {
			if (page.inPage(stat)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<StatBasic> getStats () {
		return this.statsBasics;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean inPage(StatBasic stat) {
		return this.statsBasics.contains(stat);
	}
	
}
