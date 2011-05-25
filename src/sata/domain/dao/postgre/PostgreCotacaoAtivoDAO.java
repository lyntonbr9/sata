package sata.domain.dao.postgre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.util.SATAUtil;

public class PostgreCotacaoAtivoDAO implements ICotacaoAtivoDAO {

	private Connection con;
	
	public PostgreCotacaoAtivoDAO(Connection postgreConnection){
		this.con = postgreConnection;
	}

	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo) {
		List<CotacaoAtivoTO> listaCotacoesDoAtivo = new ArrayList<CotacaoAtivoTO>();
		String sqlStmt = "SELECT * FROM \"CotacaoAtivo\" WHERE "
			+ " \"codigoAtivo\" = '" + codigoAtivo + "' "
			+ " ORDER BY periodo ASC";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CotacaoAtivoTO caTO = new CotacaoAtivoTO(); 
				caTO.setCodigo(rs.getString("codigoAtivo"));
				caTO.setPeriodo(SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"),false));
				caTO.setAbertura(rs.getString("abertura"));
				caTO.setMaxima(rs.getString("maxima"));
				caTO.setMinima(rs.getString("minima"));
				caTO.setFechamento(rs.getString("fechamento"));
				caTO.setAno(rs.getString("ano"));
				listaCotacoesDoAtivo.add(caTO);
			}
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaCotacoesDoAtivo;
	}
	
	public List<CotacaoAtivoTO> getCotacoesDoAtivo(String codigoAtivo, String ano) {
		List<CotacaoAtivoTO> listaCotacoesDoAtivo = new ArrayList<CotacaoAtivoTO>();
		String sqlStmt = "SELECT * FROM \"CotacaoAtivo\" WHERE "
			+ " \"codigoAtivo\" = '" + codigoAtivo + "' "
			+ " AND ano = '" + ano + "' " 
			+ " ORDER BY periodo ASC";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CotacaoAtivoTO caTO = new CotacaoAtivoTO(); 
				caTO.setCodigo(rs.getString("codigoAtivo"));
				caTO.setPeriodo(SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"),false));
				caTO.setAbertura(rs.getString("abertura"));
				caTO.setMaxima(rs.getString("maxima"));
				caTO.setMinima(rs.getString("minima"));
				caTO.setFechamento(rs.getString("fechamento"));
				caTO.setAno(rs.getString("ano"));
				listaCotacoesDoAtivo.add(caTO);
			}
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaCotacoesDoAtivo;
	}

	public void insertCotacaoDoAtivo(CotacaoAtivoTO caTO) {
		
		String tabela = "CotacaoAtivo"; //tabela das acoes
		if (caTO.getCodigo().length() != 5)
			tabela = "CotacaoOpcao"; //tabela das opções
		
		String sqlStmt = "INSERT INTO \"" + tabela + "\"" 
			+ "(\"codigoAtivo\", periodo, tipoperiodo, abertura, maxima, minima, fechamento, ano, volume) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1,caTO.getCodigo());
			ps.setTimestamp(2,SATAUtil.getTimeStampPeriodoCotacao(caTO.getPeriodo()));
			ps.setString(3,caTO.getTipoPeriodo());
			ps.setString(4,caTO.getAbertura());
			ps.setString(5,caTO.getMaxima());
			ps.setString(6,caTO.getMinima());
			ps.setString(7,caTO.getFechamento());
			ps.setString(8,caTO.getAno());
			ps.setString(9, caTO.getVolume());
			ps.executeUpdate();
			
			PostgreDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public boolean existeCotacao(String codigoAtivo, String periodo){
		
		String sqlStmt = "SELECT \"codigoAtivo\", periodo FROM \"CotacaoAtivo\"" 
			+ " WHERE \"codigoAtivo\" = ? AND periodo = ? ";
		
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, codigoAtivo);
			ps.setTimestamp(2,SATAUtil.getTimeStampPeriodoCotacao(periodo));
			ResultSet rs = ps.executeQuery();
			if(rs.next()) //se existe
			{
				PostgreDAOFactory.returnConnection(con);
				return true;
			}
			PostgreDAOFactory.returnConnection(con);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public String getDataUltimoCadastro(String codigoAtivo) {
		
		String dataUltimoCadastro = "";
		String sqlStmt = "SELECT \"codigoAtivo\", periodo FROM \"CotacaoAtivo\"" 
			+ " WHERE \"codigoAtivo\" = ? ORDER BY periodo DESC";
		
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ps.setString(1, codigoAtivo);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) //pega o primeiro registro
			{
				dataUltimoCadastro = SATAUtil.getTimeStampFormatado(rs.getTimestamp("periodo"), false);
			}
			PostgreDAOFactory.returnConnection(con);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dataUltimoCadastro;
	}

}
