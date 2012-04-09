package sata.domain.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import sata.domain.to.TO;

public class GenericDAOHibernate <T extends TO> {
	private Class<T> type;

	public GenericDAOHibernate(Class<T> type) {
		this.type = type;
	}
	
	public void salvar(T to) throws SQLException {
		if (to.getId() == null) 
			incluir(to);
		else alterar(to);
	}

	public Integer incluir(T to) throws SQLException {
		return (Integer) getSession().save(to);
	}
	
	public void alterar(T to) {
		getSession().update(to);
	}

	@SuppressWarnings("unchecked")
	public T recuperar(Integer id) throws SQLException {
		return (T) getSession().get(type, id);
	}
	
	public List<T> listar() throws SQLException {
		return listar("");
	}
	
	public void excluir(T to) throws SQLException {
		getSession().delete(to);
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> listar(String where) {
		Query select = getSession().createQuery(getBaseHQL() + where);
		return (List<T>) select.list();
	}
	
	private String getBaseHQL() {
		return "from " + type.getSimpleName() + " ";
	}

	protected static Session getSession() {
		return HibernateUtil.getSession();
	}
	
	@SuppressWarnings("rawtypes")
	protected List executeQuery(String query) {
		Query select = getSession().createQuery(query);
		return select.list();
	}
}
