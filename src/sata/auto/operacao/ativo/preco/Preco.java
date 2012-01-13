package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.to.Dia;

public abstract class Preco {
	
	BigDecimal valor;
	BigDecimal volatilidade;
	Dia dia;
	Map<Integer, BigDecimal> mediasMoveis = new HashMap<Integer, BigDecimal>();

	public abstract void calculaPreco() throws CotacaoInexistenteEX;
	
	public abstract BigDecimal calculaMediaMovel(Integer periodo) throws CotacaoInexistenteEX;

	public BigDecimal getMediaMovel(Integer periodo) throws CotacaoInexistenteEX {
		if (!mediasMoveis.containsKey(periodo))
			mediasMoveis.put(periodo, calculaMediaMovel(periodo));
		return mediasMoveis.get(periodo);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(10,27).
	       append(valor).
	       append(volatilidade).
	       append(dia).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
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
