package mods.gollum.core.log;

import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import mods.gollum.core.ModGollumCoreLib;
import mods.gollum.core.context.ModContext;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Logger {
	
	private final static String FILE_NAME = ModGollumCoreLib.MODID+".log";
	
	public static final int LEVEL_DEBUG   = 0;
	public static final int LEVEL_INFO    = 1;
	public static final int LEVEL_WARNING = 2;
	public static final int LEVEL_SEVERE  = 3;
	public static final int LEVEL_NONE    = 99;
	
	private static final LogFormatter formater = new LogFormatter();
	
	private static int level = LEVEL_INFO;
	private static Handler fileHandler = null;

	private static Hashtable<String, java.util.logging.Logger> loggers = new Hashtable<String, java.util.logging.Logger>();
	
	private String modId = null;
	
	public Logger() {
		this.modId = ModContext.instance().getCurrent().getModId();
	}
	
	/**
	 * Renvoi le level
	 */
	public static int getLevel () {
		return Logger.level;
	}
	
	/**
	 * Change le niveau d'affichage des logs
	 * @param levelDisplay
	 */
	public static void setLevelDisplay (int levelDisplay) {
		Logger.level = levelDisplay;
	}
	
	private String implode (Object[] list) {
		// Format the ArrayList as a string, similar to implode
		StringBuilder builder = new StringBuilder();
		
		int i = 0;
		for (Object s : list) {
			if (i != 0) builder.append(", ");
			builder.append(s.toString());
			i++;
		}
		
		return builder.toString();
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

	public void debug(Object... msg) {
		this.log (this.modId, LEVEL_DEBUG ,this.implode (msg));
	}
	
	public void info(Object... msg) {
		this.log (this.modId, LEVEL_INFO ,this.implode (msg));
	}
	
	public void warning(Object...  msg) {
		this.log (this.modId, LEVEL_WARNING ,this.implode (msg));
	}

	public void severe(Object... msg) {
		this.log (this.modId, LEVEL_SEVERE ,this.implode (msg));
	}
	
	public static java.util.logging.Logger getLogger (String key) {
		
		if (loggers.containsKey(key)) {
			return loggers.get (key);
		}
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(key);
		
		if (fileHandler == null) {
			
			try {
				if (ModGollumCoreLib.config != null) {
					// log file max size 2Mb, 3 rolling files, append-on-open
					fileHandler = new FileHandler(FILE_NAME, 2000000, ModGollumCoreLib.config.numberLogFilesUse, true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (fileHandler != null) {
				fileHandler.setLevel(Level.INFO);
				fileHandler.setFormatter(formater);
			}
		}
		if (fileHandler != null) {
			logger.addHandler(fileHandler);
			loggers.put (key, logger);
		}
		
		return logger;
	}
	
	public static void log(String key, int level, Object msg) {
		
		return;
//		
//		java.util.logging.Logger log = getLogger (key);
//		log.setLevel(Level.INFO);
//		
//		
//		
//		switch (level) {
//			
//			case LEVEL_SEVERE:
//				if (Logger.level <= LEVEL_SEVERE) {
//					log.log(Level.SEVERE, msg.toString());
//				}
//			case LEVEL_WARNING:
//				if (Logger.level <= LEVEL_WARNING) {
//					log.log(Level.WARNING, msg.toString());
//				}
//			case LEVEL_INFO:
//				if (Logger.level <= LEVEL_INFO) {
//					log.log(Level.INFO, msg.toString());
//				}
//			case LEVEL_DEBUG:
//				if (Logger.level <= LEVEL_DEBUG) {
//					log.log(Level.INFO, msg.toString());
//				}
//				break;
//	
//			default:
//				break;
//		}
	}
}
