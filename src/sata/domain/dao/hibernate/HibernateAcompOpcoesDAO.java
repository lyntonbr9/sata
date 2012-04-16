package sata.domain.dao.hibernate;

import sata.domain.dao.IAcompOpcoesDAO;
import sata.domain.to.AcompanhamentoTO;

public class HibernateAcompOpcoesDAO extends GenericDAOHibernate<AcompanhamentoTO> implements IAcompOpcoesDAO {

	public HibernateAcompOpcoesDAO() {
		super(AcompanhamentoTO.class);
	}
	
	// Implementação do singleton
	private static HibernateAcompOpcoesDAO instance;
	public static HibernateAcompOpcoesDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateAcompOpcoesDAO create() {
		if (instance == null) instance = new HibernateAcompOpcoesDAO();
		return instance;
	}
}
