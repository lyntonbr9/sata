package sata.auto.gui.web.mbean;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import sata.auto.operacao.Compra;
import sata.auto.operacao.Condicao;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Opcao;
import sata.auto.operacao.ativo.RendaFixa;
import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.auto.operacao.ativo.preco.PrecoRendaFixa;
import sata.domain.util.FacesUtil;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

@ManagedBean
@SessionScoped
public class GraficoMB implements IConstants {

	BigDecimal precoInicialAcao = new BigDecimal(20);
	BigDecimal taxaJuros = new BigDecimal(TAXA_DE_JUROS*100);
	BigDecimal volatilidade = new BigDecimal(27);
	Integer incrementoDiario = 5;
	Integer variacaoPrecoAcao = 20;
	double minY;
	double maxY;
	CartesianChartModel graficoModel = new CartesianChartModel();
	
	final int SCALE = 50;
	
	public void gerarGrafico() {
		// eixo x => variar de -VARIACAO_PERCENTUAL% a +VARIACAO_PERCENTUAL% o preço da ação no encerramento
		// eixo y => percentual de variação da operação em cada preço de ação
		// séries => o tempo para o vencimento de 30 dias ao último dia (de VARIACAO_DIARIA em VARIACAO_DIARIA)
		OperacaoMB operacaoMB = FacesUtil.getMB(OperacaoMB.class);
		if (!operacaoMB.getOperacoes().isEmpty()) {
			try {
				graficoModel.clear();
				minY = 0;
				maxY = 0;
				for (int dias=31; dias>=1; dias -= incrementoDiario) {
					ChartSeries serie = new ChartSeries();
					serie.setLabel(dias + " dias");
					for (int variacaoAcao=-variacaoPrecoAcao; variacaoAcao<=+variacaoPrecoAcao; variacaoAcao++) {
						BigDecimal valorTotal = BigDecimal.ZERO;
						BigDecimal valorInvestido = BigDecimal.ZERO;
						for (Operacao operacao : operacaoMB.getOperacoes()) {
							BigDecimal valorOperacao = calculaValorOperacao(operacao, 31, variacaoAcao);
							if (isCondicaoVerdadeira(operacao, valorOperacao, variacaoAcao)) {
								valorInvestido = valorInvestido.add(calculaValorInvestido(operacao, valorOperacao,variacaoAcao));
								valorTotal = valorTotal.add(valorOperacao);
								if (operacao.isReversivel())
									valorTotal = valorTotal.add(calculaValorOperacao(operacao.getReversa(), dias, variacaoAcao));
							}
						}
						BigDecimal x = valorTotal.divide(valorInvestido, RoundingMode.HALF_EVEN).multiply(CEM);
						serie.set(SATAUtil.formataNumero(calculaPrecoAcao(FECHAMENTO, variacaoAcao)), x);
						if (x.doubleValue() < minY) minY = x.doubleValue();
						if (x.doubleValue() > maxY) maxY = x.doubleValue();
					}
					graficoModel.addSeries(serie);
				}
				minY -= 1;
				maxY += 1;
			}
			catch (Exception e) {
				FacesUtil.addException(e);
			}
		}
	}
	
	private BigDecimal calculaValorOperacao(Operacao operacao, int dias, int variacaoAcao) {
		BigDecimal valorOperacao = BigDecimal.ZERO; 
		BigDecimal precoAcao = calculaPrecoAcao(operacao.getMomento(), variacaoAcao);
		if (operacao.getAtivo() instanceof Acao) {
			valorOperacao = precoAcao;
		}
		
		else if (operacao.getAtivo() instanceof Opcao) {
			boolean isCall = operacao.getAtivo() instanceof Call;
			BigDecimal precoExercicioOpcao = Operacao.calculaPrecoExercicioOpcao((Opcao)operacao.getAtivo(), precoInicialAcao, volatilidade.divide(CEM, SCALE, RoundingMode.HALF_EVEN));
			int diasParaVencimento = dias;
			if (operacao.getMesesParaVencimento() > 1)
				diasParaVencimento *= operacao.getMesesParaVencimento();
			valorOperacao = PrecoOpcao.blackScholes(isCall, precoAcao, precoExercicioOpcao, diasParaVencimento, volatilidade.divide(CEM, SCALE, RoundingMode.HALF_EVEN), taxaJuros.divide(CEM, SCALE, RoundingMode.HALF_EVEN));
		}
		
		else if (operacao.getAtivo() instanceof RendaFixa)
			valorOperacao = PrecoRendaFixa.calculaRendimento(precoAcao, taxaJuros.divide(CEM, SCALE, RoundingMode.HALF_EVEN));
		
		valorOperacao = valorOperacao.multiply(new BigDecimal(operacao.getQtdLotes()));
		if (operacao instanceof Compra)
			return valorOperacao.negate();
		else return valorOperacao;
	}
	
	private BigDecimal calculaValorInvestido(Operacao operacao, BigDecimal valorOperacao, int variacaoAcao) {
		if (operacao.getMomento() == ABERTURA)
			if (operacao.getAtivo() instanceof RendaFixa)
				return calculaPrecoAcao(operacao.getMomento(), variacaoAcao);
			else return valorOperacao.negate();
		return BigDecimal.ZERO;
	}
	
	private BigDecimal calculaPrecoAcao(int momento, double variacaoAcao) {
		if (momento == ABERTURA) return precoInicialAcao;
		else return precoInicialAcao.multiply(new BigDecimal(1+variacaoAcao/100));
	}
	
	private boolean isCondicaoVerdadeira(Operacao operacao, BigDecimal valorOperacao, double variacaoAcao) {
		if (operacao.getCondicao() == null)
			return true;
		
		double valorComparacao = 0;
		switch (operacao.getCondicao().getAtributo()) {
		case PRECO:
			valorComparacao = calculaPrecoAcao(operacao.getMomento(), variacaoAcao).doubleValue();
			break;
		case VOLATILIDADE:
			valorComparacao = volatilidade.divide(CEM, SCALE, RoundingMode.HALF_EVEN).doubleValue();
			break;
		}
		return Condicao.verdadeira(operacao.getCondicao().getValor(), valorComparacao, operacao.getCondicao().getOperacao());
	}

	public BigDecimal getPrecoInicialAcao() {
		return precoInicialAcao;
	}

	public void setPrecoInicialAcao(BigDecimal precoInicialAcao) {
		this.precoInicialAcao = precoInicialAcao;
	}

	public BigDecimal getTaxaJuros() {
		return taxaJuros;
	}

	public void setTaxaJuros(BigDecimal taxaJuros) {
		this.taxaJuros = taxaJuros;
	}

	public BigDecimal getVolatilidade() {
		return volatilidade;
	}

	public void setVolatilidade(BigDecimal volatilidade) {
		this.volatilidade = volatilidade;
	}

	public CartesianChartModel getGraficoModel() {
		return graficoModel;
	}

	public void setGraficoModel(CartesianChartModel graficoModel) {
		this.graficoModel = graficoModel;
	}

	public Integer getIncrementoDiario() {
		return incrementoDiario;
	}

	public void setIncrementoDiario(Integer incrementoDiario) {
		this.incrementoDiario = incrementoDiario;
	}

	public Integer getVariacaoPrecoAcao() {
		return variacaoPrecoAcao;
	}

	public void setVariacaoPrecoAcao(Integer variacaoPrecoAcao) {
		this.variacaoPrecoAcao = variacaoPrecoAcao;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}
}
