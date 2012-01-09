package sata.auto.operacao;

import sata.auto.operacao.ativo.Ativo;

public class Venda extends Operacao {
	
	@Override
	public Operacao criaOperacaoReversa(int mesesParaVencimentoReverso, int momentoReverso, int mesesParaReversaoReverso) {
		return new Compra(qtdLotes, ativo, mesesParaVencimentoReverso, momentoReverso, condicao, this, mesesParaReversaoReverso);
	}

	public Venda() {
		super();
	}

	public Venda(Ativo ativo, int mesesParaVencimento, Condicao condicao) {
		super(ativo, mesesParaVencimento, condicao);
	}

	public Venda(Ativo ativo, int mesesParaVencimento) {
		super(ativo, mesesParaVencimento);
	}

	public Venda(Ativo ativo) {
		super(ativo);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			Condicao condicao, int mesesParaReversao) {
		super(qtdLotes, ativo, mesesParaVencimento, condicao, mesesParaReversao);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			Condicao condicao) {
		super(qtdLotes, ativo, mesesParaVencimento, condicao);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int momento, Condicao condicao, Operacao reversa,
			int mesesParaReversao) {
		super(qtdLotes, ativo, mesesParaVencimento, momento, condicao, reversa,
				mesesParaReversao);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento);
	}

	public Venda(int qtdLotes, Ativo ativo) {
		super(qtdLotes, ativo);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int mesesParaReversao) {
		super(qtdLotes, ativo, mesesParaVencimento, mesesParaReversao);
	}

	public Venda(Ativo ativo, boolean reversivel) {
		super(ativo, reversivel);
	}

	public Venda(Ativo ativo, Condicao condicao) {
		super(ativo, condicao);
	}
}
