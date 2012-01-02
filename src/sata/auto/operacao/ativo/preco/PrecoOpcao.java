package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Opcao;
import sata.auto.to.Dia;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;
import sata.metastock.util.BlackScholes;

public class PrecoOpcao extends Preco implements IConstants {
	
	boolean call;
	Opcao opcao;
	int diasParaVencimento;
	BigDecimal precoAcao;
	BigDecimal precoExercicioOpcao;
	
	public PrecoOpcao() {}
	
	public PrecoOpcao(boolean call, Opcao opcao,
			int diasParaVencimento, Dia dia, BigDecimal precoExercicioOpcao) {
		this.call = call;
		this.opcao = opcao;
		this.diasParaVencimento = diasParaVencimento;
		this.dia = dia;
		this.precoExercicioOpcao = precoExercicioOpcao;
	}

	@Override
	public void calculaPreco() throws CotacaoInexistenteEX {
		valor = blackScholes();
	}

	private BigDecimal blackScholes() throws CotacaoInexistenteEX {
		double precoAcao = calculaPrecoAcao().doubleValue();
		double precoExercicioOpcao = this.precoExercicioOpcao.doubleValue();
		double tempoParaVencimentoOpcaoEmAnos = BlackScholes.getQtdDiasEmAnos(diasParaVencimento);
		double taxaDeJuros = TAXA_DE_JUROS;
		double volatilidade = getVolatilidadeAcao().doubleValue();
		double valor = BlackScholes.blackScholes(call, precoAcao, precoExercicioOpcao, tempoParaVencimentoOpcaoEmAnos, taxaDeJuros, volatilidade);
		return new BigDecimal(valor);
	}
	
	private BigDecimal calculaPrecoAcao() throws CotacaoInexistenteEX {
		precoAcao = opcao.getAcao().getPreco(dia);
		volatilidade = opcao.getAcao().getVolatilidade(dia);
		return precoAcao;
	}
	
	private BigDecimal getVolatilidadeAcao() throws CotacaoInexistenteEX {
		return opcao.getAcao().getVolatilidade(dia);
	}
	
	@Override
	public String toString() {
		String opcao = "Call";
		if (!call) opcao = "Put";
		return opcao + "("+SATAUtil.formataNumero(precoExercicioOpcao)+")"
		+ " " + dia
		+ " = " + SATAUtil.formataNumero(valor)
		+ "; Dias para vencimento = " + diasParaVencimento;
	}

	public boolean isCall() {
		return call;
	}
	public void setCall(boolean call) {
		this.call = call;
	}
	public Opcao getOpcao() {
		return opcao;
	}
	public void setOpcao(Opcao opcao) {
		this.opcao = opcao;
	}
	public int getDiasParaVencimento() {
		return diasParaVencimento;
	}
	public void setDiasParaVencimento(int diasParaVencimento) {
		this.diasParaVencimento = diasParaVencimento;
	}
	public BigDecimal getPrecoExercicioOpcao() {
		return precoExercicioOpcao;
	}
	public void setPrecoExercicioOpcao(BigDecimal precoExercicioOpcao) {
		this.precoExercicioOpcao = precoExercicioOpcao;
	}
	public BigDecimal getPrecoAcao() {
		return precoAcao;
	}
	public void setPrecoAcao(BigDecimal precoAcao) {
		this.precoAcao = precoAcao;
	}
}
