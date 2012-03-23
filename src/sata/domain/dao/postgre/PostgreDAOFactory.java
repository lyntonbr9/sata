package sata.domain.dao.postgre;

import java.sql.Connection;

import sata.domain.dao.ConnectionPoolManager;
import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.IAlertaDAO;

public class PostgreDAOFactory extends DAOFactory{
	
	private static ConnectionPoolManager conPoolManager = new ConnectionPoolManager();
	
	public IAtivoDAO getAtivoDAO(){
		return new PostgreAtivoDAO(conPoolManager.getConnectionFromPool());
	}
	
	public ICotacaoAtivoDAO getCotacaoAtivoDAO(){
		return new PostgreCotacaoAtivoDAO(conPoolManager.getConnectionFromPool());
	}
	
	public IAlertaDAO getAlertaDAO() {
		return null;
	}
	
	public static void returnConnection(Connection con){
		conPoolManager.returnConnectionToPool(con);
	}
	
	// Implementação do singleton
	private PostgreDAOFactory() {}
	private static PostgreDAOFactory instance;
	public static PostgreDAOFactory singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized PostgreDAOFactory create() {
		if (instance == null) instance = new PostgreDAOFactory();
		return instance;
	}
}
