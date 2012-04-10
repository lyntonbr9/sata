package sata.domain.to;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.operacao.ativo.Acao;
import sata.metastock.robos.CotacaoLopesFilho;

@Entity
@Table(name="SerieOperacoes")
public class SerieOperacoesTO implements TO {

	@Id	@GeneratedValue
	@Column
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="id_alerta")
	private AlertaTO alerta;
	
	@ManyToOne
	@JoinColumn(name="id_investidor")
	private InvestidorTO investidor;
	
	@Column
	private Date dataExecucao;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="nome", column=@Column(name="acao"))
	})
	private Acao acao;
	
	@Column
	private Integer qtdLotesAcao;
	
	@Column
	private BigDecimal precoAcao;
	
	@Transient
	private BigDecimal precoAcaoAtual;
	
	@Column(name="ativo")
	private boolean ativa;
	
	@OneToMany(mappedBy = "serie")
	private List<OperacaoRealizadaTO> operacoes;
	
	public BigDecimal getPrecoAcaoAtual() {
		if (precoAcaoAtual == null)
			precoAcaoAtual = CotacaoLopesFilho.getCotacao(acao.getNome()).setScale(50);
		return precoAcaoAtual;
	}
	
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
	public void setPrecoAcaoAtual(BigDecimal precoAcaoAtual) {
		this.precoAcaoAtual = precoAcaoAtual;
	}
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
}
