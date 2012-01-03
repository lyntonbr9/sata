package sata.auto.to;

import java.math.BigDecimal;

import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class ValorOperacao implements Comparable<ValorOperacao>, IConstants {
	
	private Operacao operacao;
	private Mes mes;
	private Preco preco;
	
	public ValorOperacao() {};
	
	public ValorOperacao (Operacao operacao, Mes mes, Preco preco) {
		this.operacao = operacao;
		this.mes = mes;
		this.preco = preco;
	}
	
	public BigDecimal getValor() {
		return preco.getValor().multiply(new BigDecimal(operacao.getQtdLotes()));
	}
	
	@Override
	public int compareTo(ValorOperacao other) {
		int comp = mes.compareTo(other.mes);
		if (comp != 0) return comp;
		if (operacao.getMomento() == other.operacao.getMomento()) return 0;
		if (operacao.getMomento() == ABERTURA) return -1;
		return 1;
	}
	
	@Override
	public String toString() {
		return mes + " - " + operacao + " = " + SATAUtil.formataNumero(getValor()) + " [" + preco + "] " ;
	}
	
	public Operacao getOperacao() {
		return operacao;
	}
	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}
	public Mes getMes() {
		return mes;
	}
	public void setMes(Mes mes) {
		this.mes = mes;
	}
	public Preco getPreco() {
		return preco;
	}
	public void setPreco(Preco preco) {
		this.preco = preco;
	}
}
