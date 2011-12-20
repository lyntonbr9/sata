package sata.auto.operacao.ativo;

import java.math.BigDecimal;

import sata.auto.to.DataTO;
import sata.domain.util.IConstants;
import sata.metastock.util.BlackScholes;

public abstract class Opcao extends Ativo implements IConstants {
	
	Acao acao;
	Integer ordem;
	
	BigDecimal blackScholes(boolean call, DataTO data, Acao acao, int mesesParaVencimento, int diaPrecoAcao, int diaPrecoOpcao) {
		int diasParaVencimento = 1;
		if (mesesParaVencimento > 0) 
			diasParaVencimento = (mesesParaVencimento*30)-1;
		double precoAcao = getPrecoAcao(acao, data, diaPrecoAcao).doubleValue();
		double precoExercicioOpcao = getPrecoAcao(acao, data, diaPrecoOpcao).doubleValue() + (ordem * (precoAcao * (SPREAD/100)));
		double tempoParaVencimentoOpcaoEmAnos = BlackScholes.getQtdDiasEmAnos(diasParaVencimento);
		double taxaDeJuros = TAXA_DE_JUROS;
		double volatilidade = getVolatilidade(acao, data, diaPrecoAcao).doubleValue();
		double valor = BlackScholes.blackScholes(call, precoAcao, precoExercicioOpcao, tempoParaVencimentoOpcaoEmAnos, taxaDeJuros, volatilidade);
		return new BigDecimal(valor);
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
}
