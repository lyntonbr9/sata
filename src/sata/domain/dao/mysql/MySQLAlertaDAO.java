package sata.domain.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.domain.dao.IAlertaDAO;
import sata.domain.to.AlertaTO;
import sata.domain.to.InvestidorTO;
import sata.domain.to.OperacaoRealizadaTO;
import sata.domain.to.SerieOperacoesTO;
import sata.domain.util.SATAUtil;

public class MySQLAlertaDAO implements IAlertaDAO {
	
	private Connection con;
	
	public List<AlertaTO> getAlertasAtivos() throws SQLException {
		List<AlertaTO> listaAlertasAtivos = new ArrayList<AlertaTO>();
		PreparedStatement ps = con.prepareStatement("SELECT * FROM Alerta WHERE ativo = 1");
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			AlertaTO alerta = new AlertaTO(); 
			alerta.setId(rs.getInt("id"));
			alerta.setNome(rs.getString("nome"));
			alerta.setPorcentagemGanho(rs.getInt("porcentagemGanho"));
			alerta.setPorcentagemPerda(rs.getInt("porcentagemPerda"));
			alerta.setTipoCalculoVI(TipoCalculoValorInvestido.get(rs.getString("tipoCalculoVI")));
			alerta.setPercCalculoVI(rs.getInt("percCalculoVI"));
			alerta.setSeries(new ArrayList<SerieOperacoesTO>());
			
			PreparedStatement psSerie = con.prepareStatement("SELECT * FROM SerieOperacoes WHERE id_alerta = ? AND ativo = 1");
			psSerie.setInt(1, alerta.getId());
			ResultSet rsSerie = psSerie.executeQuery();
			while(rsSerie.next()){
				SerieOperacoesTO serie = new SerieOperacoesTO();
				serie.setId(rsSerie.getInt("id"));
				serie.setAlerta(alerta);
				serie.setAcao(AcaoConteiner.get(rsSerie.getString("acao")));
				serie.setPrecoAcao(rsSerie.getBigDecimal("precoAcao"));
				serie.setQtdLotesAcao(rsSerie.getInt("qtdLotesAcao"));
				serie.setDataExecucao(SATAUtil.converte(rsSerie.getDate("dataExecucao")));
				
				PreparedStatement psInvest = con.prepareStatement("SELECT * FROM Investidor WHERE id = ?");
				psInvest.setInt(1, rsSerie.getInt("id_investidor"));
				ResultSet rsInvest = psInvest.executeQuery();
				if (rsInvest.next()) {
					InvestidorTO investidor = new InvestidorTO();
					investidor.setId(rsInvest.getInt("id"));
					investidor.setNome(rsInvest.getString("nome"));
					investidor.setEmail(rsInvest.getString("email"));
					serie.setInvestidor(investidor);
				}

				serie.setOperacoes(new ArrayList<OperacaoRealizadaTO>());
				
				PreparedStatement psOp = con.prepareStatement("SELECT * FROM OperacaoRealizada WHERE id_serie = ?");
				psOp.setInt(1, serie.getId());
				ResultSet rsOp = psOp.executeQuery();
				while(rsOp.next()){
					OperacaoRealizadaTO op = new OperacaoRealizadaTO();
					op.setId(rsOp.getInt("id"));
					op.setPosicao(rsOp.getString("posicao").charAt(0));
					op.setQtdLotes(rsOp.getInt("qtdLotes"));
					op.setAtivo(rsOp.getString("ativo"));
					op.setValor(rsOp.getBigDecimal("valor"));
					serie.getOperacoes().add(op);
				}
				alerta.getSeries().add(serie);
			}
			
			listaAlertasAtivos.add(alerta);
		}
		MySQLDAOFactory.returnConnection(con);
		return listaAlertasAtivos;
	}
	
	// Implementação do singleton
	private MySQLAlertaDAO(Connection connection){
		this.con = connection;
	}
	private static MySQLAlertaDAO instance;
	public static MySQLAlertaDAO get(Connection connection) {	
		return (instance != null)? instance : create(connection); 
	}
	private static synchronized MySQLAlertaDAO create(Connection connection) {
		if (instance == null) instance = new MySQLAlertaDAO(connection);
		return instance;
	}
}
