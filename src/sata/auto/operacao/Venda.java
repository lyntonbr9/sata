package sata.auto.operacao;

import sata.auto.operacao.ativo.Ativo;

public class Venda extends Operacao {
	
	public Venda() {}
	
	public Venda(Ativo ativo) {
		this.ativo = ativo;
	}
	
	public Venda(Ativo ativo, int mesesParaVencimento, Condicao condicao) {
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.momento = ABERTURA;
		this.condicao = condicao;
	}
	
	protected Venda(Ativo ativo, int mesesParaVencimento, int momento, Condicao condicao) {
		this.ativo = ativo;
		this.mesesParaVencimento = mesesParaVencimento;
		this.momento = momento;
		this.condicao = condicao;
	}
	
	@Override
	public Operacao reversa() {
		return new Compra(ativo, mesesParaVencimento-1, FECHAMENTO, condicao);
	}

	@Override
	public int getMomentoOperacaoOpcao() {
		if (momento == FECHAMENTO) 
			return ABERTURA; // Se está vendendo no fechamento, buscar o valor da abertura
		else return momento;
	}
}
