package sata.auto.operacao.ativo;

import java.math.BigDecimal;

import sata.auto.operacao.Operacao;
import sata.auto.to.DataTO;


public class Acao extends Ativo {
	
	String nome;
	
	public Acao() {}
	
	public Acao(String nome) {
		this.nome = nome;
	}
	
	@Override
	public BigDecimal getValor(DataTO data, Operacao operacao) {
		return getPrecoAcao(this, data, operacao.getDia());
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}