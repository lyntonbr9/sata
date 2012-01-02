package sata.auto.operacao.ativo;

import java.math.BigDecimal;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoAcao;
import sata.auto.to.Dia;


public class Acao extends Ativo {
	
	String nome;
	
	public Acao() {}
	
	public Acao(String nome) {
		this.nome = nome;
	}
	
	@Override
	public Preco criaPreco(Dia dia, Operacao operacao) throws CotacaoInexistenteEX {
		return new PrecoAcao(this, dia);
	}
	
	public BigDecimal getPreco(Dia dia) throws CotacaoInexistenteEX {
		return calculaPreco(dia, null).getValor();
	}
	
	public BigDecimal getVolatilidade(Dia dia) throws CotacaoInexistenteEX {
		return calculaPreco(dia, null).getVolatilidade();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Acao))
			return false;
		return ((Acao)other).nome.equals(nome);
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}