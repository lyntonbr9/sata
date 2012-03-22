package sata.domain.dao.postgre;

import java.sql.Connection;

import sata.domain.dao.ConnectionPoolManager;
import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.IOperacaoAlertaDAO;

public class PostgreDAOFactory extends DAOFactory{
	
	private static ConnectionPoolManager conPoolManager = new ConnectionPoolManager();
	
	public IAtivoDAO getAtivoDAO(){
		return new PostgreAtivoDAO(conPoolManager.getConnectionFromPool());
	}
	
	public ICotacaoAtivoDAO getCotacaoAtivoDAO(){
		return new PostgreCotacaoAtivoDAO(conPoolManager.getConnectionFromPool());
	}
	
	public IOperacaoAlertaDAO getOperacaoAlertaDAO(){
		return new PostgreOpcaoAlertaDAO(conPoolManager.getConnectionFromPool());
	}
	
	public static void returnConnection(Connection con){
		conPoolManager.returnConnectionToPool(con);
	}
	
}
