package sata.auto.operacao.ativo;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	
	public BigDecimal getMediaMovel(Dia dia, Integer periodo) throws CotacaoInexistenteEX {
		return calculaPreco(dia, null).getMediaMovel(periodo);
	}
	
	@Override
	public String toString() {
		return "Ação("+nome+")";
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,27).
	       append(nome).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}