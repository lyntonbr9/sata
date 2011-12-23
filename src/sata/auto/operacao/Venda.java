package sata.auto.operacao;

import sata.auto.operacao.ativo.Ativo;

public class Venda extends Operacao {
	
	public Venda() {}
	
	public Venda(Ativo ativo, int mesesParaVencimento, int momento) {
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.momento = momento;
	}

	@Override
	public int getMomentoOperacaoOpcao() {
		if (momento == FECHAMENTO) 
			return ABERTURA; // Se est� vendendo no fechamento, buscar o valor da abertura
		else return momento;
	}
}
