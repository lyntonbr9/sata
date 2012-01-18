package sata.domain.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;

import sata.auto.gui.web.mbean.LocaleMB;

public class FacesUtil {
	
	private static ResourceBundle getBundle() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (context != null)
			return context.getApplication().getResourceBundle(context, "msgs");
		else return ResourceBundle.getBundle("msg/messages");
	}
	
	private static Map<String, MessageFormat> patterns = new TreeMap<String, MessageFormat>();
	
	private static MessageFormat getMessageFormat(String pattern){
    	MessageFormat formatter = (MessageFormat) patterns.get(pattern);
    	if(formatter == null){
            synchronized(patterns){
                formatter = patterns.get(pattern);
                if(formatter == null){
                    formatter = new MessageFormat(pattern);
                    patterns.put(pattern, formatter);
                }
            }
        }
    	return formatter;
    }
	
	private static String[] bundleArguments(String[] arguments) {
		for (int i=0; i<arguments.length; i++) {
			try {
				arguments[i] = getMessage(arguments[i]);
			} catch (MissingResourceException e) {}
		}
		return arguments;
	}

	protected static String getMessage(String key, String... arguments) throws MissingResourceException {
		String pattern = getBundle().getString(key);
		if(arguments == null)
			return pattern;
		return getMessageFormat(pattern).format(bundleArguments(arguments));
	}

	public static void addException(Exception e) {
		addNonBundleMsg(FacesMessage.SEVERITY_FATAL, getMessage(e));
		e.printStackTrace();
	}

	public static void addInfo(String key, String... arguments) {  
		addBundleMsg(FacesMessage.SEVERITY_INFO, key, arguments);  
	}  

	public static void addWarn(String key, String... arguments) {  
		addBundleMsg(FacesMessage.SEVERITY_WARN, key, arguments);  
	}  
	
	public static void addError(String key, String... arguments) {  
		addBundleMsg(FacesMessage.SEVERITY_ERROR, key, arguments);  
	}  

	public static void addFatal(String key, String... arguments) {  
		addBundleMsg(FacesMessage.SEVERITY_FATAL, key, arguments);  
	}
	
	private static void addBundleMsg(Severity severity, String key, String... arguments) {
		addMsg(severity, key, true, arguments);
	}
	
	private static void addNonBundleMsg(Severity severity, String key, String... arguments) {
		addMsg(severity, key, false, arguments);
	}

	private static void addMsg(Severity severity, String msgKey, boolean bundle, String... arguments) {
		String msg;
		if (bundle) msg = getMessage(msgKey, arguments);
		else msg = msgKey;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msg, null));
	}

	private static String getMessage(Exception e) {
		String msg = e.getLocalizedMessage();
		if (StringUtils.isEmpty(msg)) {
			msg = e.getClass().getSimpleName();
		}
		return msg;
	}
	
	public static Locale getCurrentLocale() {
		LocaleMB localeMB = getMB(LocaleMB.class);
		if (localeMB!= null) return localeMB.getCurrentLocale();
		else return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <MB> MB getMB(Class<MB> clazz) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    if (context != null)
	    	return (MB) context.getApplication().evaluateExpressionGet(context, "#{" + clazz.getSimpleName().toLowerCase().replace("mb", "MB") + "}", Object.class);
	    else return null;
	}
}
