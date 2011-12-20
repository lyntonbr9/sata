package sata.auto.to;

import java.math.BigDecimal;

import sata.auto.operacao.Operacao;
import sata.domain.util.SATAUtil;

public class ValorOperacaoTO implements Comparable<ValorOperacaoTO> {
	
	private Operacao operacao;
	private DataTO data;
	private BigDecimal valor;
	
	public ValorOperacaoTO() {};
	
	public ValorOperacaoTO (Operacao operacao, DataTO data, BigDecimal valor) {
		this.operacao = operacao;
		this.data = data;
		this.valor = valor;
	}
	
	@Override
	public int compareTo(ValorOperacaoTO other) {
		return data.compareTo(other.data);
	}
	
	@Override
	public String toString() {
		return operacao + " " + data + " = " + SATAUtil.formataNumero(valor);
	}
	
	public Operacao getOperacao() {
		return operacao;
	}
	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}
	public DataTO getData() {
		return data;
	}
	public void setData(DataTO data) {
		this.data = data;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
