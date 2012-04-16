package sata.domain.dao;

import sata.domain.util.IConstants;
import sata.domain.util.SATAPropertyLoader;

public class SATAFactoryFacade implements IConstants {
	
	public static IAtivoDAO getAtivoDAO(){
		return getDAOFactory().getAtivoDAO();
	}

	public static ICotacaoAtivoDAO getCotacaoAtivoDAO(){
		return getDAOFactory().getCotacaoAtivoDAO();
	}
	public static IAlertaDAO getAlertaDAO(){
		return getDAOFactory().getAlertaDAO();
	}
	public static ISerieOperacoesDAO getSerieOperacoesDAO(){
		return getDAOFactory().getSerieOperacoesDAO();
	}
	public static IOperacaoRealizadaDAO getOperacaoRealizadaDAO(){
		return getDAOFactory().getOperacaoRealizadaDAO();
	}
	public static IInvestidorDAO getInvestidorDAO(){
		return getDAOFactory().getInvestidorDAO();
	}
	public static IAcompOpcoesDAO getAcompOpcoesDAO(){
		return getDAOFactory().getAcompOpcoesDAO();
	}
	public static IOpcaoDAO getOpcaoDAO(){
		return getDAOFactory().getOpcaoDAO();
	}
	
	private static DAOFactory getDAOFactory() {
		String driver = SATAPropertyLoader.getProperty(PROP_SATA_BD);
		if (driver.equals(BD_MYSQL))
			return DAOFactory.getDAOFactory(DAOFactory.MYSQL);
		
		else if (driver.equals(BD_POSTGRE))
			return DAOFactory.getDAOFactory(DAOFactory.POSTGRESQL);
		
		else if (driver.equals(BD_HIBERNATE))
			return DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
		
		else return null;
	}
}
