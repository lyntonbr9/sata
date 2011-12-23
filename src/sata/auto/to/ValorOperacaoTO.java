package sata.auto.to;

import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.domain.util.SATAUtil;

public class ValorOperacaoTO implements Comparable<ValorOperacaoTO> {
	
	private Operacao operacao;
	private DataTO data;
	private Preco preco;
	
	public ValorOperacaoTO() {};
	
	public ValorOperacaoTO (Operacao operacao, DataTO data, Preco preco) {
		this.operacao = operacao;
		this.data = data;
		this.preco = preco;
	}
	
	@Override
	public int compareTo(ValorOperacaoTO other) {
		return data.compareTo(other.data);
	}
	
	@Override
	public String toString() {
		return operacao + " " + data + " = " + SATAUtil.formataNumero(preco.getValor());
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
	public Preco getPreco() {
		return preco;
	}
	public void setPreco(Preco preco) {
		this.preco = preco;
	}
}
