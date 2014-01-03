package sata.auto.gui.web.mbean;

import static sata.domain.util.IConstants.LOCALE_BRASIL;
import static sata.domain.util.IConstants.MSG_BUNDLE;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import sata.auto.web.util.FacesUtil;
import sata.auto.web.util.LocaleUtil;

@ManagedBean
@SessionScoped
public class LocaleMB {
	
	String selectedLanguage = "pt";
	String selectedVariant = "BR";
	Locale currentLocale = new Locale(selectedLanguage, selectedVariant);
	
	static {
		// define o local do LocaleUtil
		LocaleUtil.setLocale(getStaticCurrentLocale());
		// define o Resource Bundle do LocaleUtil
		LocaleUtil.setRb(ResourceBundle.getBundle(MSG_BUNDLE, getStaticCurrentLocale()));
	}
	
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

	public static Locale getStaticCurrentLocale() {
		LocaleMB localeMB = FacesUtil.getMB(LocaleMB.class);
		if (localeMB.getCurrentLocale() != null) 
			return localeMB.getCurrentLocale();
		else 
			return LOCALE_BRASIL;
	}
	
}
