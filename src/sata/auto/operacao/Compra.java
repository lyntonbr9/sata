package sata.auto.operacao;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Ativo;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.to.DataTO;

public class Compra extends Operacao {
	
	public Compra() {}
	
	public Compra(Ativo ativo, int mesesParaVencimento, int momento) {
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.momento = momento;
	}
	
	@Override
	public Preco getPreco(DataTO data) throws CotacaoInexistenteEX {
		Preco preco = super.getPreco(data);
		preco.setValor(preco.getValor().negate());
		return preco;
	}

	@Override
	public int getMomentoOperacaoOpcao() {
		if (momento == FECHAMENTO) 
			return ABERTURA; // Se está comprando no fechamento, buscar o valor da abertura
		else return momento;
	}
}
