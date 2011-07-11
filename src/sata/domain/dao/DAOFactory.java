package sata.domain.dao;

import sata.domain.dao.arquivo.ArquivoDAOFactory;
import sata.domain.dao.postgre.PostgreDAOFactory;

public abstract class DAOFactory {
	
	public static final int ARQUIVO = 1;
	public static final int POSTGRESQL = 2;
	
	public abstract IAtivoDAO getAtivoDAO();
	public abstract ICotacaoAtivoDAO getCotacaoAtivoDAO();
	
	public static DAOFactory getDAOFactory(int wichFactory){
		switch(wichFactory)
		{
			case ARQUIVO:
				return new ArquivoDAOFactory();
			case POSTGRESQL:
				return new PostgreDAOFactory();
			default:
				return null;
		}
	}

}
