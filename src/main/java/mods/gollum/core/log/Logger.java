package mods.gollum.core.log;

import io.netty.util.internal.StringUtil;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Date;

import mods.gollum.core.mod.ModMetaInfos;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Logger {

	public static final int LEVEL_DEBUG   = 0;
	public static final int LEVEL_INFO    = 1;
	public static final int LEVEL_WARNING = 2;
	public static final int LEVEL_SEVERE  = 3;
	public static final int LEVEL_NONE    = 99;

//	private org.apache.logging.log4j.Logger log;
	private String modid;
	private static int level = LEVEL_INFO;
	
	public Logger(Object mod) {
		this.modid = new ModMetaInfos(mod).getModid();
	}
	
	/**
	 * Change le niveau d'affichage des logs
	 * @param levelDisplay
	 */
	public static void setLevelDisplay (int levelDisplay) {
		Logger.level = levelDisplay;
	}
	
	/**
	 * Change le niveau d'affichage des logs
	 * @param levelDisplay
	 */
	public static void setLevelDisplay (String levelDisplay) {
		Logger.level = LEVEL_INFO;
		if (levelDisplay.equals("DEBUG"))   Logger.level = LEVEL_DEBUG;
		if (levelDisplay.equals("WARNING")) Logger.level = LEVEL_WARNING;
		if (levelDisplay.equals("SEVERE"))  Logger.level = LEVEL_SEVERE;
		if (levelDisplay.equals("NONE"))    Logger.level = LEVEL_NONE;
	}
	
	private String levelStr (int level) {
		switch (level) {
			case LEVEL_DEBUG: return "DEBUG";
			case LEVEL_INFO: return "INFO";
			case LEVEL_WARNING: return "WARNING";
			default: return "SEVERE";
		}
	}
	
	private String format (String msg, int level) {
		Calendar calendar = Calendar.getInstance();
		String h =  String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		String m =  String.format("%02d", calendar.get(Calendar.MINUTE));
		String s =  String.format("%02d", calendar.get(Calendar.SECOND));
		
		return "["+h+":"+m+":"+s+"] ["+this.levelStr(level)+"] ["+this.modid+"]: "+ msg;
	}
	
	public void debug(String msg) {
		if (this.level <= LEVEL_DEBUG) {
			System.out.println(this.format(msg, LEVEL_DEBUG));
		}
	}
	
	public void info(String msg) {
		if (this.level <= LEVEL_INFO) {
			System.out.println(this.format(msg, LEVEL_INFO));
		}
	}
	
	public void warning(String msg) {
		if (this.level <= LEVEL_WARNING) {
			System.err.println(this.format(msg, LEVEL_WARNING));
		}
	}
	
	public void severe(String msg) {
		if (this.level <= LEVEL_SEVERE) {
			System.err.println(this.format(msg, LEVEL_SEVERE));
		}
	}
}
