package sata.auto.operacao;

import sata.auto.operacao.ativo.Ativo;

public class Compra extends Operacao {
	
	@Override
	public Operacao criaOperacaoReversa(int mesesParaVencimentoReverso, int momentoReverso, int mesesParaReversaoReverso) {
		return new Venda(qtdLotes, ativo, mesesParaVencimentoReverso, momentoReverso, condicao, this, mesesParaReversaoReverso);
	}
	
	public Compra() {
		super();
	}

	public Compra(Ativo ativo, int mesesParaVencimento, Condicao condicao) {
		super(ativo, mesesParaVencimento, condicao);
	}

	public Compra(Ativo ativo, int mesesParaVencimento) {
		super(ativo, mesesParaVencimento);
	}

	public Compra(Ativo ativo) {
		super(ativo);
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			Condicao condicao, int mesesParaReversao) {
		super(qtdLotes, ativo, mesesParaVencimento, condicao, mesesParaReversao);
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			Condicao condicao) {
		super(qtdLotes, ativo, mesesParaVencimento, condicao);
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int momento, Condicao condicao, Operacao reversa,
			int mesesParaReversao) {
		super(qtdLotes, ativo, mesesParaVencimento, momento, condicao, reversa,
				mesesParaReversao);
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento);
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int mesesParaReversao) {
		super(qtdLotes, ativo, mesesParaVencimento, mesesParaReversao);
	}

	public Compra(int qtdLotes, Ativo ativo) {
		super(qtdLotes, ativo);
	}

	public Compra(Ativo ativo, boolean reversivel) {
		super(ativo, reversivel);
	}

	public Compra(Ativo ativo, Condicao condicao) {
		super(ativo, condicao);
	}
}
