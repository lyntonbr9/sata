package sata.domain.dao.hibernate;

import java.sql.SQLException;

import sata.domain.dao.IAcompOpcoesDAO;
import sata.domain.to.AcompanhamentoTO;

public class HibernateAcompOpcoesDAO extends GenericDAOHibernate<AcompanhamentoTO> implements IAcompOpcoesDAO {

	public HibernateAcompOpcoesDAO() {
		super(AcompanhamentoTO.class);
	}
	
	@Override
	public AcompanhamentoTO recuperar(Integer id) throws SQLException {
		return super.recuperar(id);
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
