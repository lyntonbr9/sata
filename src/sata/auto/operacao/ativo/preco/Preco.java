package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;

import sata.auto.exception.CotacaoInexistenteEX;

public abstract class Preco {
	
	BigDecimal valor;

	public abstract void calculaPreco() throws CotacaoInexistenteEX;

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getValor() {
		return valor;
	}
}
