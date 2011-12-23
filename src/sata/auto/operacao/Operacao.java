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
	Condicao condicao;
	
	public Preco getPreco(DataTO data) throws CotacaoInexistenteEX {
		return ativo.getPreco(data, this);
	}
	
	public boolean condicaoVerdadeira(Preco preco) {
		if (condicao == null) return true;
		double valor = 0;
		
		switch (condicao.atributo) {
		case Condicao.PRECO:
			valor = preco.getValor().doubleValue();
			break;
		case Condicao.VOLATILIDADE:
			valor = preco.getVolatilidade().doubleValue();
			break;
		}
		
		switch (condicao.operacao) {
		case Condicao.IGUAL:
			return valor == condicao.valor;
			
		case Condicao.DIFERENTE:
			return valor != condicao.valor;
			
		case Condicao.MAIOR:
			return valor > condicao.valor;
			
		case Condicao.MENOR:
			return valor < condicao.valor;
			
		case Condicao.MAIOR_IGUAL:
			return valor >= condicao.valor;
			
		case Condicao.MENOR_IGUAL:
			return valor <= condicao.valor;
		}
		
		return false;
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
	public Condicao getCondicao() {
		return condicao;
	}
	public void setCondicao(Condicao condicao) {
		this.condicao = condicao;
	}
}
