package sata.auto.operacao.ativo;

import java.math.BigDecimal;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.auto.to.Dia;

public abstract class Opcao extends Ativo {
	
	Acao acao;
	Integer ordem;
	boolean volatil;
	
	abstract boolean isCall();
	
	@Override
	public Preco criaPreco(Dia dia, Operacao operacao) throws CotacaoInexistenteEX {
		return new PrecoOpcao(isCall(), this, operacao.getDiasParaVencimento(dia), dia, operacao.getPrecoExercicioOpcao(dia, this));
	}
	
	public Integer getOrdem(BigDecimal volatilidade) {
		if (volatil && ordem > 0) {
			int ordemVolatil = (int) Math.round(volatilidade.doubleValue() * 10);
			return ordem+ordemVolatil-1;
		}
		return ordem;
	}
	
	@Override
	public String toString() {
		return super.toString() + "(" + ordem + ")";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Opcao))
			return false;
		return ((Opcao)other).acao.equals(acao)
			&& ((Opcao)other).ordem.equals(ordem)
			&& ((Opcao)other).isCall() == isCall();
	}
	
	public Integer getOrdem() {
		return ordem;
	}
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public boolean isVolatil() {
		return volatil;
	}
	public void setVolatil(boolean volatil) {
		this.volatil = volatil;
	}
}
