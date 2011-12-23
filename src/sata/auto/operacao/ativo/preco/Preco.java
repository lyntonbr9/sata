package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;

import sata.auto.exception.CotacaoInexistenteEX;

public abstract class Preco {
	
	BigDecimal valor;
	BigDecimal volatilidade;
	String periodo;

	public abstract void calculaPreco() throws CotacaoInexistenteEX;

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public BigDecimal getVolatilidade() {
		return volatilidade;
	}
	public void setVolatilidade(BigDecimal volatilidade) {
		this.volatilidade = volatilidade;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
}
