package mods.gollum.core.log;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Logger {

	public static final int LEVEL_INFO    = 0;
	public static final int LEVEL_WARNING = 1;
	public static final int LEVEL_SEVERE  = 2;
	public static final int LEVEL_NONE    = 99;

	java.util.logging.Logger log;
	int level = LEVEL_INFO;
	
	public Logger(FMLPreInitializationEvent evt, int levelDisplay) {
		this.log   = evt.getModLog();
		this.level = levelDisplay;
	}
	
	public Logger(FMLPreInitializationEvent evt, String levelDisplay) {
		
		this(evt, LEVEL_INFO);
		
		if (levelDisplay.equals("WARNING")) this.level = LEVEL_WARNING;
		if (levelDisplay.equals("SEVERE"))  this.level = LEVEL_SEVERE;
		if (levelDisplay.equals("NONE"))    this.level = LEVEL_NONE;
		
	}
	
	public Logger(FMLPreInitializationEvent evt) {
		this(evt, LEVEL_WARNING);
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
