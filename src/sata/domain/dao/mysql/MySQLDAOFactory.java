package sata.domain.dao.mysql;

import java.sql.Connection;

import sata.domain.dao.ConnectionPoolManager;
import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.IOperacaoAlertaDAO;

public class MySQLDAOFactory extends DAOFactory{
	
	private static ConnectionPoolManager conPoolManager = new ConnectionPoolManager();
	
	public static void returnConnection(Connection con){
		conPoolManager.returnConnectionToPool(con);
	}
	
	@Override
	public ICotacaoAtivoDAO getCotacaoAtivoDAO(){
		return new MySQLCotacaoAtivoDAO(conPoolManager.getConnectionFromPool());
	}
	
	public IOperacaoAlertaDAO getOperacaoAlertaDAO(){
		return new MySQLOperacaoAlertaDAO(conPoolManager.getConnectionFromPool());
	}
	
	@Override
	public IAtivoDAO getAtivoDAO() {
		return null;
	}
	
}
