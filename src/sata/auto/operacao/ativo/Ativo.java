package sata.auto.operacao.ativo;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.to.Dia;

public abstract class Ativo {
	
	abstract Preco criaPreco(Dia dia, Operacao operacao) throws CotacaoInexistenteEX;
	
	public Preco calculaPreco(Dia dia, Operacao operacao) throws CotacaoInexistenteEX {
		Preco preco = criaPreco(dia, operacao); 
		preco.calculaPreco();
		return preco;
	}
	
	public Preco getPreco(Dia dia, Operacao operacao) throws CotacaoInexistenteEX {
		return calculaPreco(dia, operacao);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
