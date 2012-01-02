package sata.auto.operacao;

import sata.auto.operacao.ativo.Ativo;

public class Venda extends Operacao {
	
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
			Condicao condicao) {
		super(qtdLotes, ativo, mesesParaVencimento, condicao);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int momento, Condicao condicao, Operacao reversa) {
		super(qtdLotes, ativo, mesesParaVencimento, momento, condicao, reversa);
	}

	public Venda(int qtdLotes, Ativo ativo, int mesesParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento);
	}

	public Venda(int qtdLotes, Ativo ativo) {
		super(qtdLotes, ativo);
	}

	@Override
	public Operacao criaOperacaoReversa(int mesesParaVencimentoReverso, int momentoReverso) {
		return new Compra(qtdLotes, ativo, mesesParaVencimentoReverso, momentoReverso, condicao, this);
	}
}
