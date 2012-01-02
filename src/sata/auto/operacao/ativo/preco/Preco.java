package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.to.Dia;

public abstract class Preco {
	
	BigDecimal valor;
	BigDecimal volatilidade;
	Dia dia;

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
	public Dia getDia() {
		return dia;
	}
	public void setDia(Dia dia) {
		this.dia = dia;
	}
}
