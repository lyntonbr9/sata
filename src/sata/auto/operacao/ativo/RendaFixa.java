package sata.auto.operacao.ativo;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoRendaFixa;
import sata.auto.to.Dia;

public class RendaFixa extends Ativo {
	
	Acao acao;
	Double percentual;
	
	public RendaFixa() {}
	
	public RendaFixa(Acao acao, Double percentual) {
		this.acao = acao;
		this.percentual = percentual;
	}
	
	@Override
	public Preco criaPreco(Dia dia, Operacao operacao) throws CotacaoInexistenteEX {
		return new PrecoRendaFixa(this, dia);
	}
	
	@Override
	public String toString() {
		return super.toString() + " " + percentual*100 + "%";
	}
	
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
	public Double getPercentual() {
		return percentual;
	}
	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}
}
