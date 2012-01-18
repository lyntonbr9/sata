package sata.auto.gui.web.mbean;

import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class LocaleMB {
	
	String selectedLanguage = "pt";
	String selectedVariant = "BR";
	Locale currentLocale = new Locale(selectedLanguage, selectedVariant);
	
	public void changeLocal() {
		currentLocale = new Locale(selectedLanguage,selectedVariant);
	}
		
	public String getSelectedLanguage() {
		return selectedLanguage;
	}
	
	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}
	
	public String getSelectedVariant() {
		return selectedVariant;
	}

	public void setSelectedVariant(String selectedVariant) {
		this.selectedVariant = selectedVariant;
	}

	public Locale getCurrentLocale() {
		return currentLocale;
	}
	
	public void setCurrentLocale(Locale currentLocale) {
		this.currentLocale = currentLocale;
	}

}
