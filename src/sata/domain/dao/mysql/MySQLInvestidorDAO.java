package sata.domain.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sata.domain.dao.IInvestidorDAO;
import sata.domain.to.InvestidorTO;

public class MySQLInvestidorDAO implements IInvestidorDAO {
	
	private Connection con;
	
	@Override
	public List<InvestidorTO> listaInvestidores() throws SQLException {
		List<InvestidorTO> lista = new ArrayList<InvestidorTO>();
		String query = "SELECT * FROM Investidor";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			InvestidorTO investidor = new InvestidorTO();
			investidor.setId(rs.getInt("id"));
			investidor.setNome(rs.getString("nome"));
			investidor.setEmail(rs.getString("email"));
			lista.add(investidor);
		}
		return lista;
	}
	
	// Implementação do singleton
	private MySQLInvestidorDAO(Connection connection){
		this.con = connection;
	}
	private static MySQLInvestidorDAO instance;
	public static MySQLInvestidorDAO get(Connection connection) {	
		if (instance != null) {
			instance.con = connection;
			return instance;
		}
		return create(connection);  
	}
	private static synchronized MySQLInvestidorDAO create(Connection connection) {
		if (instance == null) instance = new MySQLInvestidorDAO(connection);
		return instance;
	}
}
