package sata.domain.dao.mysql;

import java.sql.Connection;

import sata.domain.dao.ConnectionPoolManager;
import sata.domain.dao.DAOFactory;
import sata.domain.dao.IAlertaDAO;
import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.IInvestidorDAO;
import sata.domain.dao.IAcompOpcoesDAO;
import sata.domain.dao.IOpcaoDAO;
import sata.domain.dao.IOperacaoRealizadaDAO;
import sata.domain.dao.ISerieOperacoesDAO;

public class MySQLDAOFactory extends DAOFactory{
	
	private static ConnectionPoolManager conPoolManager = new ConnectionPoolManager();
	
	public static void returnConnection(Connection con){
		conPoolManager.returnConnectionToPool(con);
	}
	
	@Override
	public ICotacaoAtivoDAO getCotacaoAtivoDAO(){
		return MySQLCotacaoAtivoDAO.get(conPoolManager.getConnectionFromPool());
	}
	
	@Override
	public IAlertaDAO getAlertaDAO(){
		return MySQLAlertaDAO.get(conPoolManager.getConnectionFromPool());
	}
	
	@Override
	public ISerieOperacoesDAO getSerieOperacoesDAO() {
		return MySQLSerieOperacoesDAO.get(conPoolManager.getConnectionFromPool());
	}

	@Override
	public IOperacaoRealizadaDAO getOperacaoRealizadaDAO() {
		return MySQLOperacaoRealizadaDAO.get(conPoolManager.getConnectionFromPool());
	}
	
	@Override
	public IInvestidorDAO getInvestidorDAO() {
		return MySQLInvestidorDAO.get(conPoolManager.getConnectionFromPool());
	}
	
	@Override
	public IAtivoDAO getAtivoDAO() {
		return null;
	}
	
	@Override
	public IAcompOpcoesDAO getAcompOpcoesDAO() {
		return null;
	}
	
	@Override
	public IOpcaoDAO getOpcaoDAO() {
		return null;
	}
	
	// Implementação do singleton
	private MySQLDAOFactory() {}
	private static MySQLDAOFactory instance;
	public static MySQLDAOFactory singleton() {	
		return (instance != null)? instance : create(); 
	}
	private static synchronized MySQLDAOFactory create() {
		if (instance == null) instance = new MySQLDAOFactory();
		return instance;
	}
}
