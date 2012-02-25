package sata.domain.dao;

public class SATAFactoryFacade {
	
	public static IAtivoDAO getAtivoDAO(){
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		return daoFactory.getAtivoDAO();
	}

	public static ICotacaoAtivoDAO getCotacaoAtivoDAO(){
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		return daoFactory.getCotacaoAtivoDAO();
	}
}
