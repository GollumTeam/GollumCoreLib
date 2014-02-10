package mods.gollum.core.log;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Logger {

	public static final int LEVEL_INFO    = 0;
	public static final int LEVEL_WARNING = 1;
	public static final int LEVEL_SEVERE  = 2;
	public static final int LEVEL_NONE    = 99;

	private java.util.logging.Logger log;
	private static int level = LEVEL_INFO;
	
	public Logger(FMLPreInitializationEvent evt) {
		this.log   = evt.getModLog();
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
		if (levelDisplay.equals("WARNING")) Logger.level = LEVEL_WARNING;
		if (levelDisplay.equals("SEVERE"))  Logger.level = LEVEL_SEVERE;
		if (levelDisplay.equals("NONE"))    Logger.level = LEVEL_NONE;
	}
	
	public void info(String msg) {
		if (this.level <= LEVEL_INFO) {
			this.log.info(msg);
		}
	}
	
	public void warning(String msg) {
		if (this.level <= LEVEL_WARNING) {
			this.log.warning(msg);
		}
	}
	
	public void severe(String msg) {
		if (this.level <= LEVEL_SEVERE) {
			this.log.severe(msg);
		}
	}
}
