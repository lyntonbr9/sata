package sata.domain.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import sata.domain.dao.IAlertaDAO;
import sata.domain.to.AlertaTO;
import sata.domain.to.InvestidorTO;

public class HibernateAlertaDAO extends GenericDAOHibernate<AlertaTO> implements IAlertaDAO {

	private HibernateAlertaDAO() {
		super(AlertaTO.class);
	}

	@Override
	public List<AlertaTO> listaAlertasAtivos() throws SQLException {
		return super.listar("ativo = 1");
	}

	@Override
	public List<AlertaTO> listaAlertasInvestidor(InvestidorTO investidor) throws SQLException {
		return null;
	}
	
	// Implementação do singleton
	private static HibernateAlertaDAO instance;
	public static HibernateAlertaDAO singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateAlertaDAO create() {
		if (instance == null) instance = new HibernateAlertaDAO();
		return instance;
	}
}
