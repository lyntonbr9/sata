package sata.auto.operacao.ativo;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.to.DataTO;

public abstract class Ativo {
	
	abstract Preco criaPreco(DataTO data, Operacao operacao) throws CotacaoInexistenteEX;
	
	public Preco calculaPreco(DataTO data, Operacao operacao) throws CotacaoInexistenteEX {
		Preco preco = criaPreco(data, operacao); 
		preco.calculaPreco();
		return preco;
	}
	
	public Preco getPreco(DataTO data, Operacao operacao) throws CotacaoInexistenteEX {
		return getPrecoAtivo(data, operacao);
	}
	
	Preco getPrecoAtivo(DataTO data, Operacao operacao) throws CotacaoInexistenteEX {
		return calculaPreco(data, operacao);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
