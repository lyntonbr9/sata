package sata.domain.to;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.operacao.ativo.Acao;

public class SerieOperacoesTO {

	private Integer id;
	private AlertaTO alerta;
	private InvestidorTO investidor;
	private Date dataExecucao;
	private Acao acao;
	private Integer qtdLotesAcao;
	private BigDecimal precoAcao;
	private boolean ativa;
	private List<OperacaoRealizadaTO> operacoes;
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(15,27).
	       append(id).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public AlertaTO getAlerta() {
		return alerta;
	}
	public void setAlerta(AlertaTO alerta) {
		this.alerta = alerta;
	}
	public InvestidorTO getInvestidor() {
		return investidor;
	}
	public void setInvestidor(InvestidorTO investidor) {
		this.investidor = investidor;
	}
	public Date getDataExecucao() {
		return dataExecucao;
	}
	public void setDataExecucao(Date dataExecucao) {
		this.dataExecucao = dataExecucao;
	}
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
	public Integer getQtdLotesAcao() {
		return qtdLotesAcao;
	}
	public void setQtdLotesAcao(Integer qtdLotesAcao) {
		this.qtdLotesAcao = qtdLotesAcao;
	}
	public BigDecimal getPrecoAcao() {
		return precoAcao;
	}
	public void setPrecoAcao(BigDecimal precoAcao) {
		this.precoAcao = precoAcao;
	}
	public List<OperacaoRealizadaTO> getOperacoes() {
		return operacoes;
	}
	public void setOperacoes(List<OperacaoRealizadaTO> operacoes) {
		this.operacoes = operacoes;
	}
	public boolean isAtiva() {
		return ativa;
	}
	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}
}
