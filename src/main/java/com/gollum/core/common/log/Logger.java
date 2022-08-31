package com.gollum.core.common.log;


import java.util.Hashtable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.RolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;

import com.gollum.core.ModGollumCoreLib;
import com.gollum.core.common.context.ModContext;

public class Logger {
	
	private final static String FILE_NAME = "logs/"+ModGollumCoreLib.MODID+".log";
	
	public static final int LEVEL_DEBUG   = 0;
	public static final int LEVEL_INFO    = 1;
	public static final int LEVEL_MESSAGE = 2;
	public static final int LEVEL_WARNING = 3;
	public static final int LEVEL_SEVERE  = 4;
	public static final int LEVEL_NONE    = 99;
	
	private static final LogFormatter formater = new LogFormatter();
	
	private static int level = LEVEL_INFO;
	private static RollingFileAppender fileAppender = null;

	private static Hashtable<String, org.apache.logging.log4j.Logger> loggers = new Hashtable<String, org.apache.logging.log4j.Logger>();

	
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
			if (s == null) {
				builder.append("null");
			} else {
				builder.append(s.toString());
			}
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
		if (levelDisplay.equals("MESSAGE")) Logger.level = LEVEL_MESSAGE;
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
	
	public void message(Object... msg) {
		this.log (this.modId, LEVEL_MESSAGE ,this.implode (msg));
	}
	
	public void warning(Object...  msg) {
		this.log (this.modId, LEVEL_WARNING ,this.implode (msg));
	}

	public void severe(Object... msg) {
		this.log (this.modId, LEVEL_SEVERE ,this.implode (msg));
	}
	
	public static org.apache.logging.log4j.Logger getLogger (String key) {
		
		if (loggers.containsKey(key)) {
			return loggers.get (key);
		}
		
		org.apache.logging.log4j.Logger logger = LogManager.getLogger(key);
		
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration conf = ctx.getConfiguration();
		
		if (fileAppender == null) {
			
			try {
				if (ModGollumCoreLib.config != null) {
					// log file max size 2Mb, 3 rolling files, append-on-open
					RolloverStrategy strategy = DefaultRolloverStrategy.createStrategy(new Integer(ModGollumCoreLib.config.numberLogFilesUse).toString(), null, "nomax", null, null, false, conf);
					TriggeringPolicy policy = OnStartupTriggeringPolicy.createPolicy(0);
					fileAppender = RollingFileAppender.newBuilder()
						.withFileName(FILE_NAME)
						.withName(FILE_NAME)
						.withFilePattern("logs/%d"+ModGollumCoreLib.MODID+"{yyyy-MM-dd}-%i.log.gz")
						.withAppend(true)
						.withBufferedIo(false)
						.withImmediateFlush(true)
						.withStrategy(strategy)
						.withPolicy(policy)
						.build()
					;
					fileAppender.start();
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (fileAppender != null) {
			}
		}
		if (fileAppender != null) {
			conf.getLoggerConfig(key).addAppender(fileAppender, Level.DEBUG, null);
			ctx.updateLoggers(conf);
			
			loggers.put (key, logger);
		}
		
		return logger;
	}
	
	public static void log(String key, int level, Object msg) {
		
		org.apache.logging.log4j.Logger log = getLogger (key);
		
		switch (level) {
			
			case LEVEL_SEVERE:
				if (Logger.level <= LEVEL_SEVERE) {
					log.error(msg.toString());
				}
				break;
			case LEVEL_WARNING:
				if (Logger.level <= LEVEL_WARNING) {
					log.warn(msg.toString());
				}
				break;
			case LEVEL_MESSAGE:
				if (Logger.level <= LEVEL_MESSAGE) {
					log.info (msg.toString());
				}
				break;
			case LEVEL_INFO:
				if (Logger.level <= LEVEL_INFO) {
					log.info (msg.toString());
				}
				break;
			case LEVEL_DEBUG:
				if (Logger.level <= LEVEL_DEBUG) {
					log.info (msg.toString());
				}
				break;
	
			default:
				break;
		}
	}
}
