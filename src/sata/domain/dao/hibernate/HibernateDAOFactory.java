package sata.domain.dao.hibernate;

import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAlertaDAO;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.IInvestidorDAO;
import sata.domain.dao.IAcompOpcoesDAO;
import sata.domain.dao.IOpcaoDAO;
import sata.domain.dao.IOperacaoRealizadaDAO;
import sata.domain.dao.ISerieOperacoesDAO;

public class HibernateDAOFactory extends DAOFactory {

	@Override
	public IAtivoDAO getAtivoDAO() {
		return null;
	}

	@Override
	public ICotacaoAtivoDAO getCotacaoAtivoDAO() {
		return HibernateCotacaoAtivoDAO.singleton();
	}

	@Override
	public IAlertaDAO getAlertaDAO() {
		return HibernateAlertaDAO.singleton();
	}
	
	@Override
	public ISerieOperacoesDAO getSerieOperacoesDAO() {
		return HibernateSerieOperacoesDAO.singleton();
	}

	@Override
	public IOperacaoRealizadaDAO getOperacaoRealizadaDAO() {
		return HibernateOperacaoRealizadaDAO.singleton();
	}

	@Override
	public IInvestidorDAO getInvestidorDAO() {
		return HibernateInvestidorDAO.singleton();
	}
	
	@Override
	public IAcompOpcoesDAO getAcompOpcoesDAO() {
		return HibernateAcompOpcoesDAO.singleton();
	}
	
	@Override
	public IOpcaoDAO getOpcaoDAO() {
		return HibernateOpcaoDAO.singleton();
	}

	// Implementação do singleton
	private HibernateDAOFactory() {}
	private static HibernateDAOFactory instance;
	public static HibernateDAOFactory singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized HibernateDAOFactory create() {
		if (instance == null) instance = new HibernateDAOFactory();
		return instance;
	}
}
