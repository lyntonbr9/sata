package sata.auto.to;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.domain.util.SATAUtil;

public class ValorResultado {
	
	String valor;
	BigDecimal resultado;
	
	public ValorResultado() {}
	
	public ValorResultado(String valor, BigDecimal resultado) {
		this.valor = valor;
		this.resultado = resultado;
	}
	
	@Override
	public String toString() {
		return valor + " = " + SATAUtil.formataNumero(resultado);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(14,30).
	       append(valor).
	       append(resultado).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public BigDecimal getResultado() {
		return resultado;
	}
	public void setResultado(BigDecimal resultado) {
		this.resultado = resultado;
	}
}
