package sata.domain.dao;

import java.sql.SQLException;
import java.util.List;

import sata.domain.to.AlertaTO;
import sata.domain.to.InvestidorTO;
import sata.domain.to.OperacaoRealizadaTO;
import sata.domain.to.SerieOperacoesTO;

public interface IAlertaDAO {

	public AlertaTO recuperaAlerta(Integer id) throws SQLException;
	public List<AlertaTO> listaAlertasAtivos() throws SQLException;
	public List<AlertaTO> listaAlertasInvestidor(InvestidorTO investidor) throws SQLException;
	public List<AlertaTO> listaAlertas() throws SQLException;
	public void salvarAlerta(AlertaTO alerta) throws SQLException;
	public void salvarSerie(SerieOperacoesTO serie) throws SQLException;
	public void salvarOperacao(SerieOperacoesTO serie, OperacaoRealizadaTO operacao) throws SQLException;
	public void excluirAlerta(AlertaTO alerta) throws SQLException;
	public void excluirSerie(SerieOperacoesTO serie) throws SQLException;
	public void excluirOperacao(OperacaoRealizadaTO operacao) throws SQLException;
}
