package sata.domain.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import sata.domain.dao.IAtivoDAO;
import sata.domain.dao.IOperacaoAlertaDAO;
import sata.domain.to.AtivoTO;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.OperacaoRealizadaTO;
import sata.domain.util.SATAUtil;

public class MySQLOperacaoAlertaDAO implements IOperacaoAlertaDAO {

	private Connection con;
	
	public MySQLOperacaoAlertaDAO(Connection dbConnection){
		this.con = dbConnection;
	}

	@Override
	public List<OperacaoRealizadaTO> getOperacoesParaAcompanhar() {
		List<OperacaoRealizadaTO> listaOperacoesParaAcompanhar = new ArrayList<OperacaoRealizadaTO>();
		String sqlStmt = "SELECT * FROM OperacaoAlerta WHERE "
			+ " acompanhar = '1' ";
		try {
			PreparedStatement ps = con.prepareStatement(sqlStmt);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				OperacaoRealizadaTO orTO = new OperacaoRealizadaTO(); 
				orTO.setCodigoAtivo(rs.getString("codigoAtivo"));
				orTO.setDataExecucao(SATAUtil.getTimeStampFormatado(rs.getTimestamp("dataExecucao"),false));
				orTO.setPorcentagemGanhoAlerta(rs.getString("porcentagemGanhoAlerta"));
				orTO.setPorcentagemPerdaAlerta(rs.getString("porcentagemPerdaAlerta"));
				orTO.setPosicao(rs.getString("posicao"));
				orTO.setQuantidadeLotes(rs.getInt("quantidadeLotes"));
				orTO.setTipo(rs.getString("tipo"));
				orTO.setValor(rs.getString("valor"));
				orTO.setValorAlertaInferior(rs.getString("valorAlertaInferior"));
				orTO.setValorAlertaSuperior(rs.getString("valorAlertaSuperior"));
				orTO.setAcompanhar((rs.getString("acompanhar").equals("1")) ? true : false);
				listaOperacoesParaAcompanhar.add(orTO);
			}
			MySQLDAOFactory.returnConnection(con);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return listaOperacoesParaAcompanhar;
	}


}
