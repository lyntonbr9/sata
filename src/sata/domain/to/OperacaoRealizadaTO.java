package sata.domain.to;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.enums.Posicao;
import sata.metastock.robos.CotacaoLopesFilho;

public class OperacaoRealizadaTO  {
	
	public static final char COMPRADO = 'C';
	public static final char VENDIDO = 'V';
	
	private Integer id;
	private Posicao posicao;
	private Integer qtdLotes;
	private String ativo;
	private BigDecimal valor;
	private BigDecimal valorAtual;
	
	public BigDecimal getValorReal() {
		if (valor != null) {
			valor = valor.setScale(50);
			if (posicao == Posicao.COMPRADO)
				return valor.negate();
		}
		return valor;
	}
	
	public BigDecimal getValorAtual() {
		if (valorAtual == null)
			valorAtual = CotacaoLopesFilho.getCotacao(ativo).setScale(50);
		if (posicao == Posicao.VENDIDO)
			return valorAtual.negate();
		return valorAtual;
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
	public Posicao getPosicao() {
		return posicao;
	}
	public void setPosicao(Posicao posicao) {
		this.posicao = posicao;
	}
	public Integer getQtdLotes() {
		return qtdLotes;
	}
	public void setQtdLotes(Integer qtdLotes) {
		this.qtdLotes = qtdLotes;
	}
	public String getAtivo() {
		return ativo;
	}
	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}
	public BigDecimal getValor() {
		return this.valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public void setValorAtual(BigDecimal valorAtual) {
		this.valorAtual = valorAtual;
	}
}
