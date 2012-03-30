package sata.domain.dao;

//import sata.domain.dao.arquivo.ArquivoDAOFactory;
import sata.domain.dao.mysql.MySQLDAOFactory;
import sata.domain.dao.postgre.PostgreDAOFactory;

public abstract class DAOFactory {
	
	public static final int ARQUIVO = 1;
	public static final int POSTGRESQL = 2;
	public static final int MYSQL = 3;
	
	public abstract IAtivoDAO getAtivoDAO();
	public abstract ICotacaoAtivoDAO getCotacaoAtivoDAO();
	public abstract IAlertaDAO getAlertaDAO();
	public abstract IInvestidorDAO getInvestidorDAO();
	
	public static DAOFactory getDAOFactory(int wichFactory){
		switch(wichFactory)
		{
			case ARQUIVO:
//				return new ArquivoDAOFactory();
			case POSTGRESQL:
				return PostgreDAOFactory.singleton();
			case MYSQL:
				return MySQLDAOFactory.singleton();
			default:
				return null;
		}
	}

}
