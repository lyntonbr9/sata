package sata.domain.util;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerUtil {
	
	static Logger logger;
	
	public static void setup(Object obj){
		String nomeEstrategia = obj.getClass().getSimpleName();
		logger = Logger.getLogger(nomeEstrategia);
		String logFileName = "logs/"+nomeEstrategia+".log";

		Properties prop = new Properties();
		prop.setProperty("log4j.rootLogger","DEBUG, stdout, WORKLOG");
		prop.setProperty("log4j.appender.stdout","org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.stdout.layout","org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.WORKLOG","org.apache.log4j.FileAppender");
		prop.setProperty("log4j.appender.WORKLOG.File", logFileName);
		prop.setProperty("log4j.appender.WORKLOG.layout","org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.WORKLOG.append","false");

		PropertyConfigurator.configure(prop);
	}
	
	public static void log(String msg) {
		logger.info(msg);
	}

}
