package sata.auto.operacao.ativo;

import java.math.BigDecimal;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoAcao;
import sata.auto.to.DataTO;


public class Acao extends Ativo {
	
	String nome;
	
	public Acao() {}
	
	public Acao(String nome) {
		this.nome = nome;
	}
	
	@Override
	public Preco criaPreco(DataTO data, Operacao operacao) throws CotacaoInexistenteEX {
		return new PrecoAcao(this, data, operacao.getMomento());
	}
	
	public BigDecimal getPreco(DataTO data, int momento) throws CotacaoInexistenteEX {
		return getPrecoAtivo(data, criaOperacaoFake(momento)).getValor();
	}
	
	public BigDecimal getVolatilidade(DataTO data, int momento) throws CotacaoInexistenteEX {
		return getPrecoAtivo(data, criaOperacaoFake(momento)).getVolatilidade();
	}
	
	public String getDia(DataTO data, int momento) throws CotacaoInexistenteEX {
		return getPrecoAtivo(data, criaOperacaoFake(momento)).getPeriodo();
	}
	
	private Operacao criaOperacaoFake(int momento) {
		Operacao compra = new Compra();
		compra.setMomento(momento);
		return compra;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}