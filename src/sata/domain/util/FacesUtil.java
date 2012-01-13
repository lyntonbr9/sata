package sata.domain.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class FacesUtil {

	public static void addException(Exception e) {
		addError(e.getLocalizedMessage());
		e.printStackTrace();
	}

	public static void addInfo(String msg, String detalhe) {  
		addMsg(FacesMessage.SEVERITY_INFO, msg, detalhe);  
	}  

	public static void addInfo(String msg) {
		addInfo(msg, null);
	}

	public static void addWarn(String msg, String detalhe) {  
		addMsg(FacesMessage.SEVERITY_WARN, msg, detalhe);  
	}  

	public static void addWarn(String msg) {
		addWarn(msg, null);
	}

	public static void addError(String msg, String detalhe) {  
		addMsg(FacesMessage.SEVERITY_ERROR, msg, detalhe);  
	}  

	public static void addError(String msg) {
		addError(msg, null);
	}

	public static void addFatal(String msg, String detalhe) {  
		addMsg(FacesMessage.SEVERITY_FATAL, msg, detalhe);  
	}

	public static void addFatal(String msg) {
		addFatal(msg, null);
	}

	private static void addMsg(Severity severity, String msg, String detalhe) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, msg, detalhe));
	}
}
