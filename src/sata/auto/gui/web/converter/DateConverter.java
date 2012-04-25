package sata.auto.gui.web.converter;

import java.text.ParseException;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import sata.domain.util.SATAUtil;

public class DateConverter implements Converter {
	
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		try {
			return SATAUtil.formataData(value);
		} catch (ParseException e) {
			return null;
		}
	}

	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		try {
			return SATAUtil.formataData((Date)value);
		}
		catch (ClassCastException e) {
			return value.toString();
		}
	}
}
