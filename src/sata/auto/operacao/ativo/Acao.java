package sata.auto.operacao.ativo;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import sata.auto.exception.SATAEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoAcao;
import sata.auto.to.Dia;
import sata.domain.to.TO;

@Embeddable
public class Acao extends Ativo implements TO {
	
	String nome;
	
	public Acao() {}
	
	public Acao(String nome) {
		this.nome = nome;
	}
	
	@Override
	public Preco criaPreco(Dia dia, Operacao operacao) throws SATAEX {
		return new PrecoAcao(this, dia);
	}
	
	@Override
	public String getBundleMessage() {
		return "list.ativo.acao";
	}
	
	@Override
	public Integer getId() {
		return nome.hashCode();
	}

	public BigDecimal getPreco(Dia dia) throws SATAEX {
		return calculaPreco(dia, null).getValor();
	}
	
	public BigDecimal getVolatilidade(Dia dia) throws SATAEX {
		return calculaPreco(dia, null).getVolatilidade();
	}
	
	public BigDecimal getMediaMovel(Dia dia, Integer periodo) throws SATAEX {
		return calculaPreco(dia, null).getMediaMovel(periodo);
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