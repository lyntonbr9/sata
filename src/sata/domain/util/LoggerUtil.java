package sata.domain.util;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerUtil {
	
	static Logger logger;
	static boolean settedUp = false;
	
	public static void setup(Class<?> clazz){
		setup(clazz.getSimpleName());
	}
	
	public static void setup(String nomeArquivo){
		if (!settedUp) {
			logger = Logger.getLogger(nomeArquivo);
			String logFileName = "logs/"+nomeArquivo+".log";

			Properties prop = new Properties();
			prop.setProperty("log4j.rootLogger","DEBUG, stdout, WORKLOG");
			prop.setProperty("log4j.appender.stdout","org.apache.log4j.ConsoleAppender");
			prop.setProperty("log4j.appender.stdout.layout","org.apache.log4j.PatternLayout");
			prop.setProperty("log4j.appender.WORKLOG","org.apache.log4j.FileAppender");
			prop.setProperty("log4j.appender.WORKLOG.File", logFileName);
			prop.setProperty("log4j.appender.WORKLOG.layout","org.apache.log4j.PatternLayout");
			prop.setProperty("log4j.appender.WORKLOG.append","false");

			PropertyConfigurator.configure(prop);
			settedUp = true;
		}
	}
	
	public static void log(String msg) {
		logger.info(msg);
	}
}
