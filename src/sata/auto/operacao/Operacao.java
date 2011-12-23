package sata.auto.operacao;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Ativo;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.to.DataTO;
import sata.domain.util.IConstants;

public abstract class Operacao implements IConstants {
	
	int momento;
	Ativo ativo;
	int mesesParaVencimento;
	
	public Preco getPreco(DataTO data) throws CotacaoInexistenteEX {
		return ativo.getPreco(data, this);
	}
	
	public abstract int getMomentoOperacaoOpcao();
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + ativo;
	}
	public void setAtivo(Ativo ativo) {
		this.ativo = ativo;
	}
	public Ativo getAtivo() {
		return ativo;
	}
	public int getMomento() {
		return momento;
	}
	public void setMomento(int momento) {
		this.momento = momento;
	}

	public int getMesesParaVencimento() {
		return mesesParaVencimento;
	}
	public void setMesesParaVencimento(int mesesParaVencimento) {
		this.mesesParaVencimento = mesesParaVencimento;
	}
}
