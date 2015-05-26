package com.gollum.core.common.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.stats.StatBase;
import net.minecraftforge.common.AchievementPage;

public class StatsPage {
	
	protected static TreeMap<String, StatsPage> statsPages = new TreeMap<String, StatsPage>();
	
	protected String name;
	protected ArrayList<StatBase> statsBases;
	
	public StatsPage(String name, StatBase... statsBases) {
		this.name        = name;
		this.statsBases =  new ArrayList<StatBase>(Arrays.asList(statsBases));
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
	
	public static boolean inPages(StatBase stat) {
		for (StatsPage page : getStatsPages()) {
			if (page.inPage(stat)) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<StatBase> getStats () {
		return this.statsBases;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean inPage(StatBase stat) {
		return this.statsBases.contains(stat);
	}
	
}
