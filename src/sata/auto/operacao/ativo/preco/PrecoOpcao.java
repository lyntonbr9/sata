package sata.auto.operacao.ativo.preco;

import java.math.BigDecimal;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.ativo.Acao;
import sata.auto.to.DataTO;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;
import sata.metastock.util.BlackScholes;

public class PrecoOpcao extends Preco implements IConstants {
	
	boolean call;
	DataTO data;
	Acao acao;
	int mesesParaVencimento;
	int momentoPrecoAcao;
	int momentoPrecoOpcao;
	int ordem;
	BigDecimal precoExercicioOpcao;
	
	public PrecoOpcao() {}
	
	public PrecoOpcao(boolean call, DataTO data, Acao acao,
			int mesesParaVencimento, int momentoPrecoAcao,
			int momentoPrecoOpcao, int ordem) {
		this.call = call;
		this.data = data;
		this.acao = acao;
		this.mesesParaVencimento = mesesParaVencimento;
		this.momentoPrecoAcao = momentoPrecoAcao;
		this.momentoPrecoOpcao = momentoPrecoOpcao;
		this.ordem = ordem;
	}
	
	@Override
	public void calculaPreco() throws CotacaoInexistenteEX {
		valor = blackScholes();
	}

	private BigDecimal blackScholes() throws CotacaoInexistenteEX {
		int diasParaVencimento = getDiasParaVencemento();
		double precoAcao = getPrecoAcao().doubleValue();
		double precoExercicioOpcao = calculaPrecoExercicioOpcao().doubleValue();
		double tempoParaVencimentoOpcaoEmAnos = BlackScholes.getQtdDiasEmAnos(diasParaVencimento);
		double taxaDeJuros = TAXA_DE_JUROS;
		double volatilidade = acao.getVolatilidade(data, momentoPrecoAcao).doubleValue();
		double valor = BlackScholes.blackScholes(call, precoAcao, precoExercicioOpcao, tempoParaVencimentoOpcaoEmAnos, taxaDeJuros, volatilidade);
		return new BigDecimal(valor);
	}
	
	private int getDiasParaVencemento() {
		if (mesesParaVencimento > 0) 
			return (mesesParaVencimento*30)-1;
		return 1;
	}
	
	private BigDecimal getPrecoAcao() throws CotacaoInexistenteEX {
		return acao.getPreco(data, momentoPrecoAcao);
	}
	
	private BigDecimal calculaPrecoExercicioOpcao() throws CotacaoInexistenteEX {
		BigDecimal spread = new BigDecimal(SPREAD/100);
		BigDecimal precoAcaoOpcao = acao.getPreco(data, momentoPrecoOpcao);
		precoExercicioOpcao = precoAcaoOpcao.add(precoAcaoOpcao.multiply(new BigDecimal(ordem)).multiply(spread));
		volatilidade = acao.getVolatilidade(data, momentoPrecoOpcao);
		periodo = acao.getDia(data, momentoPrecoOpcao);
		return precoExercicioOpcao;
	}
	
	@Override
	public String toString() {
		String opcao = "Call";
		if (!call) opcao = "Put";
		return opcao + "("+SATAUtil.formataNumero(precoExercicioOpcao)+")" 
		+ " = " + SATAUtil.formataNumero(valor)
		+ "; Dias para vencimento = " + getDiasParaVencemento();
	}

	public boolean isCall() {
		return call;
	}
	public void setCall(boolean call) {
		this.call = call;
	}
	public DataTO getData() {
		return data;
	}
	public void setData(DataTO data) {
		this.data = data;
	}
	public Acao getAcao() {
		return acao;
	}
	public void setAcao(Acao acao) {
		this.acao = acao;
	}
	public int getMesesParaVencimento() {
		return mesesParaVencimento;
	}
	public void setMesesParaVencimento(int mesesParaVencimento) {
		this.mesesParaVencimento = mesesParaVencimento;
	}
	public int getMomentoPrecoAcao() {
		return momentoPrecoAcao;
	}
	public void setMomentoPrecoAcao(int momentoPrecoAcao) {
		this.momentoPrecoAcao = momentoPrecoAcao;
	}
	public int getMomentoPrecoOpcao() {
		return momentoPrecoOpcao;
	}
	public void setMomentoPrecoOpcao(int momentoPrecoOpcao) {
		this.momentoPrecoOpcao = momentoPrecoOpcao;
	}
	public int getOrdem() {
		return ordem;
	}
	public void setOrdem(int ordem) {
		this.ordem = ordem;
	}
	public BigDecimal getPrecoExercicioOpcao() {
		return precoExercicioOpcao;
	}
	public void setPrecoExercicioOpcao(BigDecimal precoExercicioOpcao) {
		this.precoExercicioOpcao = precoExercicioOpcao;
	}
}
