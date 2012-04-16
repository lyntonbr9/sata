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
		} finally {
			finalizeSession();
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
		} finally {
			finalizeSession();
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
		} finally {
			finalizeSession();
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
		} finally {
			finalizeSession();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> listar(String where, Object... params) throws SQLException {
		Query query = executeQuery(getBaseHQL() + where);
		for (int i=0; i<params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return (List<T>) query.list();
	}
	
	protected Query executeQuery(String hql) throws SQLException {
		Session sessao = getSession(); 
		Query query = null;
		try {
			query = sessao.createQuery(hql);
		} catch (Exception e) { 
			throw new SQLException(e);
		} finally {
			finalizeSession();
		}
		return query;
	}
	
	private String getBaseHQL() {
		return "from " + type.getSimpleName() + " ";
	}

	protected static Session getSession() {
		return HibernateUtil.getSession();
	}
	
	protected static void finalizeSession() {
		// N�o fecha a sess�o
	}
}
