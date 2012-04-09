package sata.auto.gui.web.mbean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;

import sata.auto.enums.Posicao;
import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.domain.alert.Alerta;
import sata.domain.dao.IAlertaDAO;
import sata.domain.dao.IInvestidorDAO;
import sata.domain.dao.IOperacaoRealizadaDAO;
import sata.domain.dao.ISerieOperacoesDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.AlertaTO;
import sata.domain.to.InvestidorTO;
import sata.domain.to.OperacaoRealizadaTO;
import sata.domain.to.SerieOperacoesTO;
import sata.domain.util.FacesUtil;
import sata.domain.util.IConstants;

@ManagedBean
@SessionScoped
public class AlertaMB implements IConstants {
	
	IAlertaDAO alertaDAO = SATAFactoryFacade.getAlertaDAO();
	ISerieOperacoesDAO serieDAO = SATAFactoryFacade.getSerieOperacoesDAO();
	IOperacaoRealizadaDAO opDAO = SATAFactoryFacade.getOperacaoRealizadaDAO();
	
	public AlertaMB() throws SQLException {
		atualizar();
	}
	
	boolean alterar = false;
	
	List<AlertaTO> alertas = new ArrayList<AlertaTO>();
	List<InvestidorTO> investidores = new ArrayList<InvestidorTO>();
	
	AlertaTO alerta = new AlertaTO();
	SerieOperacoesTO serie = new SerieOperacoesTO();
	OperacaoRealizadaTO operacao = new OperacaoRealizadaTO();
	
	public void salvarAlerta() {
		try {
			if (alertaValido()) {
				alertaDAO.salvar(alerta);
				if (!alterar) alertas.add(alerta);
			}
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
	}

	public void salvarSerie() {
		try {
			if (serieValida()) {
				serie.setAlerta(alerta);
				serieDAO.salvar(serie);
				if (!alterar) alerta.getSeries().add(serie);
			}
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void salvarOperacao() {
		try {
			if (operacaoValida()) {
				operacao.setSerie(serie);
				opDAO.salvar(operacao);
				if (!alterar) serie.getOperacoes().add(operacao);
			}
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void excluirAlerta() {
		try {
			alertaDAO.excluir(alerta);
			alertas = alertaDAO.listar();
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void excluirSerie() {
		try {
			serieDAO.excluir(serie);
			alerta.getSeries().remove(serie);
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void excluirOperacao() {
		try {
			opDAO.excluir(operacao);
			serie.getOperacoes().remove(operacao);
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	public void editar() {
		alterar = true;
	}
	
	public void incluirAlerta() {
		alerta = new AlertaTO();
		alterar = false;
	}
	
	public void incluirSerie() {
		serie = new SerieOperacoesTO();
		alterar = false;
	}
	
	public void incluirOperacao() {
		operacao = new OperacaoRealizadaTO();
		alterar = false;
	}
	
	public void atualizar() throws SQLException {
		IInvestidorDAO investidorDAO = SATAFactoryFacade.getInvestidorDAO();
		alertas = alertaDAO.listar();
		investidores = investidorDAO.listar();
	}
	
	private boolean alertaValido() {
		boolean valido = true;
		if (StringUtils.isEmpty(alerta.getNome())) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_ALERTA_LABEL_NOME);
			valido = false;
		}
		if (alerta.getPorcentagemGanho() <= 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_ALERTA_LABEL_PERC_GANHO);
			valido = false;
		}
		if (alerta.getPorcentagemPerda() <= 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_ALERTA_LABEL_PERC_PERDA);
			valido = false;
		}
		return valido;
	}
	
	private boolean serieValida() {
		boolean valido = true;
		if (serie.getDataExecucao() == null) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_ALERTA_LABEL_DATA_EXECUCAO);
			valido = false;
		}
		if (serie.getAcao() == null) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_ALERTA_LABEL_ACAO);
			valido = false;
		}
		if (serie.getQtdLotesAcao() <= 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_ALERTA_LABEL_QTD_LOTES_ACAO);
			valido = false;
		}
		if (serie.getPrecoAcao().doubleValue() <= 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_ALERTA_LABEL_PRECO_ACAO);
			valido = false;
		}
		if (serie.getInvestidor() == null) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_ALERTA_LABEL_INVESTIDOR);
			valido = false;
		}
		return valido;
	}
	
	private boolean operacaoValida() {
		boolean valido = true;
		if (operacao.getPosicao() == null) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_ALERTA_LABEL_POSICAO);
			valido = false;
		}
		if (operacao.getQtdLotes() <= 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_ALERTA_LABEL_QTD_LOTES);
			valido = false;
		}
		if (StringUtils.isEmpty(operacao.getAtivo())) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_ALERTA_LABEL_ATIVO);
			valido = false;
		}
		if (operacao.getValor().doubleValue() <= 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_ALERTA_LABEL_VALOR);
			valido = false;
		}
		return valido;
	}
	
	public String getSerieNow() {
		if (serie.getId() != null) return FacesUtil.formataTexto(Alerta.getMensagemSerie(serie));
		else return "";
	}
	
	public List<SelectItem> getTiposCalculoValorInvestido() {
		return TipoCalculoValorInvestido.getSelectItems();
	}
	
	public List<Acao> getAcoes() {
		return AcaoConteiner.getAcoes();
	}
	
	public List<SelectItem> getPosicoes() {
		return Posicao.getSelectItems();
	}
	
	public String getTextoOperacao() {
		if (alterar) return MSG_GENERAL_LABEL_ALTERAR;
		else return MSG_GENERAL_LABEL_INCLUIR;
	}
	
	public boolean isShowPercValorInvestido() {
		return alerta.getTipoCalculoVI() == TipoCalculoValorInvestido.PRECO_ACAO;
	}
	
	public List<AlertaTO> getAlertas() {
		return alertas;
	}
	public void setAlertas(List<AlertaTO> alertas) {
		this.alertas = alertas;
	}
	public AlertaTO getAlerta() {
		return alerta;
	}
	public void setAlerta(AlertaTO alerta) {
		this.alerta = alerta;
	}
	public SerieOperacoesTO getSerie() {
		return serie;
	}
	public void setSerie(SerieOperacoesTO serie) {
		this.serie = serie;
	}
	public List<InvestidorTO> getInvestidores() {
		return investidores;
	}
	public void setInvestidores(List<InvestidorTO> investidores) {
		this.investidores = investidores;
	}
	public OperacaoRealizadaTO getOperacao() {
		return operacao;
	}
	public void setOperacao(OperacaoRealizadaTO operacao) {
		this.operacao = operacao;
	}
}
