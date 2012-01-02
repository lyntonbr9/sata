package sata.auto.operacao;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Ativo;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.to.Dia;

public class Compra extends Operacao {
	
	public Compra() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Compra(Ativo ativo, int mesesParaVencimento, Condicao condicao) {
		super(ativo, mesesParaVencimento, condicao);
		// TODO Auto-generated constructor stub
	}

	public Compra(Ativo ativo, int mesesParaVencimento) {
		super(ativo, mesesParaVencimento);
		// TODO Auto-generated constructor stub
	}

	public Compra(Ativo ativo) {
		super(ativo);
		// TODO Auto-generated constructor stub
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			Condicao condicao) {
		super(qtdLotes, ativo, mesesParaVencimento, condicao);
		// TODO Auto-generated constructor stub
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento,
			int momento, Condicao condicao, Operacao reversa) {
		super(qtdLotes, ativo, mesesParaVencimento, momento, condicao, reversa);
		// TODO Auto-generated constructor stub
	}

	public Compra(int qtdLotes, Ativo ativo, int mesesParaVencimento) {
		super(qtdLotes, ativo, mesesParaVencimento);
		// TODO Auto-generated constructor stub
	}

	public Compra(int qtdLotes, Ativo ativo) {
		super(qtdLotes, ativo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Operacao criaOperacaoReversa(int mesesParaVencimentoReverso, int momentoReverso) {
		return new Venda(qtdLotes, ativo, mesesParaVencimentoReverso, momentoReverso, condicao, this);
	}
	
	@Override
	public Preco getPreco(Dia dia) throws CotacaoInexistenteEX {
		Preco preco = super.getPreco(dia);
		preco.setValor(preco.getValor().negate());
		return preco;
	}
}
