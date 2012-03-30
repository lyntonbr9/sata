package sata.auto.gui.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import sata.domain.to.TO;
import sata.domain.util.Cache;

public class TOConverter implements Converter {
	
	private static Cache<Integer, TO> cache = new Cache<Integer, TO>(100);

	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value != null && cache.containsKey(getKey(value))) {
			return cache.get(getKey(value));
		}
		return null;
	}

	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		if (value != null && !"".equals(value)) {
			TO to = (TO) value;

			// adiciona item como atributo do componente
			this.addAttribute(to);

			String codigo = to.getId().toString();
			if (codigo != null) {
				return codigo;
			}
		}
		return (String) value;
	}

	private void addAttribute( TO to) {
		Integer key = to.getId();
		if (!cache.containsKey(key))
			cache.put(key, to);
	}
	
	private Integer getKey(String value) {
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e) {
			return null;
		}
	}
}
