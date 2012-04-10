package sata.domain.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
		Session sessao = getSession(); 
		Integer id = null;
		Transaction transaction = null;
		try {
			transaction = sessao.beginTransaction();
			id = (Integer) sessao.save(to);
			transaction.commit();
		} catch (Exception e) { 
			if (transaction != null) {
				transaction.rollback();
			}
			throw new SQLException(e);
		}
		return id;
	}
	
	public void alterar(T to) throws SQLException {
		Session sessao = getSession(); 
		Transaction transaction = null;
		try {
			transaction = sessao.beginTransaction();
			sessao.update(to);
			transaction.commit();
		} catch (Exception e) { 
			if (transaction != null) {
				transaction.rollback();
			}
			throw new SQLException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public T recuperar(Integer id) throws SQLException {
		Session sessao = getSession(); 
		T to = null;
		try {
			to = (T) sessao.get(type, id);
		} catch (Exception e) { 
			throw new SQLException(e);
		}
		return to;
	}
	
	public List<T> listar() throws SQLException {
		return listar("");
	}
	
	public void excluir(T to) throws SQLException {
		Session sessao = getSession(); 
		Transaction transaction = null;
		try {
			transaction = sessao.beginTransaction();
			sessao.delete(to);
			transaction.commit();
		} catch (Exception e) { 
			if (transaction != null) {
				transaction.rollback();
			}
			throw new SQLException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> listar(String where) throws SQLException {
		return (List<T>) executeQuery(getBaseHQL() + where);
	}
	
	private String getBaseHQL() {
		return "from " + type.getSimpleName() + " ";
	}

	protected static Session getSession() {
		return HibernateUtil.getSession();
	}
	
	@SuppressWarnings("rawtypes")
	protected List executeQuery(String query) throws SQLException {
		Session sessao = getSession(); 
		List list = null;
		try {
			Query select = sessao.createQuery(query);
			list = select.list();
		} catch (Exception e) { 
			throw new SQLException(e);
		}
		return list;
	}
}
