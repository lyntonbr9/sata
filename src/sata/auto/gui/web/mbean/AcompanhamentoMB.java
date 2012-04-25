package sata.auto.gui.web.mbean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;

import sata.auto.enums.Posicao;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.conteiner.AcaoConteiner;
import sata.domain.dao.IAcompanhamentoDAO;
import sata.domain.dao.IOpcaoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.AcompOpcaoTO;
import sata.domain.to.AcompanhamentoTO;
import sata.domain.util.FacesUtil;
import sata.domain.util.IConstants;

@ManagedBean
@SessionScoped
public class AcompanhamentoMB implements IConstants {
	
	IAcompanhamentoDAO acompDAO = SATAFactoryFacade.getAcompanhamentoDAO();
	IOpcaoDAO opcaoDAO = SATAFactoryFacade.getOpcaoDAO();
	
	public AcompanhamentoMB() throws SQLException {
		atualizar();
	}
	
	boolean alterar = false;
	
	List<AcompanhamentoTO> acompanhamentos = new ArrayList<AcompanhamentoTO>();
	List<Date> datasVencimento = new ArrayList<Date>();
	
	AcompanhamentoTO acompanhamento = new AcompanhamentoTO();
	AcompOpcaoTO acompOpcao = new AcompOpcaoTO();
	
	public void editar() {
		alterar = true;
	}
	
	public void incluirAcomp() {
		acompanhamento = new AcompanhamentoTO();
		acompanhamento.setInvestidor(FacesUtil.getInvestidorLogado());
		alterar = false;
	}
	
	public void incluirAcompOpcao() {
		acompOpcao = new AcompOpcaoTO();
		acompOpcao.setAcompanhamento(acompanhamento);
		alterar = false;
	}
	
	public void salvarAcomp() {
		try {
			if (acompanhamentoValido()) {
				acompDAO.salvar(acompanhamento);
				if (!alterar) acompanhamentos.add(acompanhamento);
			}
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
		atualizar();
	}
	
	public void salvarAcompOpcao() {
		try {
			if (acompOpcaoValido()) {
				if (!alterar) acompanhamento.getAcompanhamentos().add(acompOpcao);
				acompDAO.salvar(acompanhamento);
			}
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
		atualizar();
	}
	
	public void excluirAcomp() {
		try {
			acompDAO.excluir(acompanhamento);
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
		atualizar();
	}
	
	public void excluirAcompOpcao() {
		try {
			acompanhamento.getAcompanhamentos().remove(acompOpcao);
			SATAFactoryFacade.getAcompOpcaoDAO().excluir(acompOpcao);
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
		atualizar();
	}
	
	public void atualizar()  {
		try {
			acompOpcao = null;
			acompanhamentos = acompDAO.listar();
			datasVencimento = opcaoDAO.listarDatasVencimento();
		} catch (Exception e) {
			FacesUtil.addException(e);
		}
	}
	
	private boolean acompanhamentoValido() {
		boolean valido = true;
		if (StringUtils.isEmpty(acompanhamento.getNome())) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_ACOMPANHAMENTO_LABEL_NOME);
			valido = false;
		}
		if (acompanhamento.getInvestidor() == null) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_ACOMPANHAMENTO_LABEL_INVESTIDOR);
			valido = false;
		}
		if (acompanhamento.getAcao() == null) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_ACOMPANHAMENTO_LABEL_ACAO);
			valido = false;
		}
		if (acompanhamento.getDataVencimento() == null) {
			FacesUtil.addError(MSG_ERRO_CAMPO_OBRIGATORIO, MSG_ACOMPANHAMENTO_LABEL_DATA_VENCIMENTO);
			valido = false;
		}
		return valido;
	}
	
	private boolean acompOpcaoValido() {
		boolean valido = true;
		if (acompOpcao.getPercToleranciaInferior() < 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_ACOMPANHAMENTO_LABEL_PERC_TOLERANCIA_INFERIOR);
			valido = false;
		}
		if (acompOpcao.getPercToleranciaSuperior() < 0) {
			FacesUtil.addError(MSG_ERRO_VALOR_MAIOR_QUE_ZERO, MSG_ACOMPANHAMENTO_LABEL_PERC_TOLERANCIA_SUPERIOR);
			valido = false;
		}
		return valido;
	}
	
	public List<Acao> getAcoes() {
		return AcaoConteiner.getAcoes();
	}
	
	public List<SelectItem> getPosicoes() {
		return Posicao.getSelectItems();
	}
	
	public List<SelectItem> getDatasVencimento() {
		return FacesUtil.convertToSelectItems(datasVencimento);
	}
	
	public String getTextoOperacao() {
		if (alterar) return MSG_GENERAL_LABEL_ALTERAR;
		else return MSG_GENERAL_LABEL_INCLUIR;
	}

	public boolean isAlterar() {
		return alterar;
	}
	public void setAlterar(boolean alterar) {
		this.alterar = alterar;
	}
	public List<AcompanhamentoTO> getAcompanhamentos() {
		return acompanhamentos;
	}
	public void setAcompanhamentos(List<AcompanhamentoTO> acompanhamentos) {
		this.acompanhamentos = acompanhamentos;
	}
	public AcompanhamentoTO getAcompanhamento() {
		return acompanhamento;
	}
	public void setAcompanhamento(AcompanhamentoTO acompanhamento) {
		this.acompanhamento = acompanhamento;
	}
	public AcompOpcaoTO getAcompOpcao() {
		return acompOpcao;
	}
	public void setAcompOpcao(AcompOpcaoTO acompOpcao) {
		this.acompOpcao = acompOpcao;
	}
	public void setDatasVencimento(List<Date> datasVencimento) {
		this.datasVencimento = datasVencimento;
	}
}
