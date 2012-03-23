package sata.domain.to;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.operacao.ativo.Acao;

public class SerieOperacoesTO {

	private Integer id;
	private AlertaTO alerta;
	private InvestidorTO investidor;
	private Calendar dataExecucao;
	private Acao acao;
	private Integer qtdLotesAcao;
	private BigDecimal precoAcao;
	private Collection<OperacaoRealizadaTO> operacoes;
	
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
	public Calendar getDataExecucao() {
		return dataExecucao;
	}
	public void setDataExecucao(Calendar dataExecucao) {
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

	public Collection<OperacaoRealizadaTO> getOperacoes() {
		return operacoes;
	}
	public void setOperacoes(Collection<OperacaoRealizadaTO> operacoes) {
		this.operacoes = operacoes;
	}
}
