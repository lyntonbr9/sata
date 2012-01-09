package sata.auto.simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.enums.TipoRelatorio;
import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Opcao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.operacao.ativo.preco.PrecoOpcao;
import sata.auto.to.Mes;
import sata.auto.to.ValorOperacao;
import sata.domain.util.IConstants;
import sata.domain.util.SATAUtil;

public class Resultado implements IConstants {
	
	private List<ValorOperacao> resultados = new ArrayList<ValorOperacao>();
	
	private Integer anoInicial;
	private Integer anoFinal;
	private TipoCalculoValorInvestido tipoCalculoValorInvestido;
	
	public void addResultado(Resultado resultado) {
		if (anoInicial == null || anoInicial.intValue() > resultado.anoInicial.intValue())
			this.anoInicial = resultado.anoInicial;
		if (anoFinal == null || anoFinal.intValue() < resultado.anoFinal.intValue())
			this.anoFinal = resultado.anoFinal;
		this.tipoCalculoValorInvestido = resultado.tipoCalculoValorInvestido;
		this.resultados.addAll(resultado.resultados);
	}
	
	public void addValorOperacao(ValorOperacao valorOperacao) {
		resultados.add(valorOperacao);
	}
	
	public void setResultadoMensal(Operacao operacao, Mes mes, Preco preco) {
		setResultadoMensal(operacao, mes.getMes(), mes.getAno(), preco);
	}
	
	public void setResultadoMensal(Operacao operacao, Integer mes, Integer ano, Preco preco) {
		ValorOperacao valorOperacao = getValorOperacao(operacao, mes, ano);
		if (valorOperacao != null)
			valorOperacao.setPreco(preco);
		else addValorOperacao(new ValorOperacao(operacao, new Mes(mes,ano), preco));
	}
	
	public ValorOperacao getValorOperacao(Operacao operacao, Mes mes) {
		return getValorOperacao(operacao, mes.getMes(), mes.getAno());
	}
	
	public ValorOperacao getValorOperacao(Operacao operacao, Integer mes, Integer ano) {
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getOperacao().equals(operacao) &&
				valorOperacao.getMes().getMes().equals(mes) &&
				valorOperacao.getMes().getAno().equals(ano)) {
				return valorOperacao;
			}
		}
		return null;
	}
	
	public BigDecimal getResultadoMensal(Operacao operacao, Mes mes) {
		return getResultadoMensal(operacao, mes.getMes(), mes.getAno());
	}
	
	public BigDecimal getResultadoMensal(Operacao operacao, Integer mes, Integer ano) {
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getOperacao().equals(operacao) &&
				valorOperacao.getMes().getMes().equals(mes) &&
				valorOperacao.getMes().getAno().equals(ano)) {
				return valorOperacao.getValor();
			}
		}
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getResultadoMensal(Mes mes) {
		return getResultadoMensal(mes.getMes(), mes.getAno());
	}
	
	public BigDecimal getResultadoMensalAcao(Integer mes, Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().getMes().equals(mes) &&
				valorOperacao.getMes().getAno().equals(ano) &&
				valorOperacao.getOperacao().getAtivo() instanceof Acao) {
				valor = valor.add(valorOperacao.getValor());
			}
		}
		return valor;
	}
	
	public BigDecimal getResultadoMensal(Integer mes, Integer ano) {
		if (tipoCalculoValorInvestido == TipoCalculoValorInvestido.TOTAL_COMPRADO_IGNORAR_PRIMEIRO_MES
				&& mes.equals(1) && ano.equals(anoInicial)) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().getMes().equals(mes) &&
				valorOperacao.getMes().getAno().equals(ano)) {
				valor = valor.add(valorOperacao.getValor());
			}
		}
		return valor;
	}
	
	public BigDecimal getValorInvestido(Mes mes) {
		return getValorInvestido(mes.getMes(), mes.getAno());
	}
	
	public BigDecimal getValorInvestidoAcao(Integer mes, Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().getMes().equals(mes) &&
					valorOperacao.getMes().getAno().equals(ano) &&
					valorOperacao.getOperacao() instanceof Compra &&
					valorOperacao.getOperacao().getAtivo() instanceof Acao) {
				valor = valor.add(valorOperacao.getValor().negate());
			}
		}
		return valor;
	}
	
	public BigDecimal getValorInvestido(Integer mes, Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().getMes().equals(mes) &&
					valorOperacao.getMes().getAno().equals(ano)) {
				
				switch (tipoCalculoValorInvestido) {
				case TOTAL_COMPRADO:
				case TOTAL_COMPRADO_IGNORAR_PRIMEIRO_MES:
					if (valorOperacao.getOperacao() instanceof Compra)
						if (valorOperacao.getOperacao().getMomento() == ABERTURA)
							valor = valor.add(valorOperacao.getValor().negate());
						else { // Fechamento
							ValorOperacao operacaoReversa = getValorOperacaoReversa(valorOperacao.getOperacao(), mes, ano);
							BigDecimal valorReversa = operacaoReversa.getValor();
							valor = valor.add((valorOperacao.getValor()).add(valorReversa).negate());
						}
					break;
				case DIFERENCA_STRIKES:
					if (valorOperacao.getOperacao().getAtivo() instanceof Opcao &&
							valorOperacao.getOperacao().getMomento() == ABERTURA) {
						BigDecimal precoOpcao = ((PrecoOpcao)valorOperacao.getPreco()).getPrecoExercicioOpcao();
						if (valorOperacao.getOperacao() instanceof Venda)
							precoOpcao = precoOpcao.negate();
						valor = valor.add(precoOpcao);
					}
					break;
				}
			}
		}
		return valor;
	}
	
	public BigDecimal getResultadoPercentualMensal(Mes mes) {
		return getResultadoPercentualMensal(mes.getMes(), mes.getAno());
	}
	
	public BigDecimal getResultadoPercentualMensal(Integer mes, Integer ano) {
		BigDecimal valorInicial = getValorInvestido(mes, ano);
		BigDecimal resultadoNominal = getResultadoMensal(mes,ano);
		if (valorInicial.equals(BigDecimal.ZERO))
			return BigDecimal.ZERO;
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}
	
	public BigDecimal getResultadoPercentualMensalAcao(Mes mes) {
		return getResultadoPercentualMensalAcao(mes.getMes(), mes.getAno());
	}
	
	public BigDecimal getResultadoPercentualMensalAcao(Integer mes, Integer ano) {
		BigDecimal valorInicial = getValorInvestidoAcao(mes, ano);
		BigDecimal resultadoNominal = getResultadoMensalAcao(mes, ano);
		if (valorInicial.equals(BigDecimal.ZERO))
			return BigDecimal.ZERO;
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}
	
	public BigDecimal getResultadoAnual(Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (int mes=1; mes<=12; mes++) {
			valor = valor.add(getResultadoMensal(mes, ano));
		}
		return valor;
	}
	
	public BigDecimal getResultadoPercentualAnual(Integer ano) {
		BigDecimal valorInicial = getValorInvestido(1, ano);
		BigDecimal resultadoNominal = getResultadoAnual(ano);
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}
	
	public BigDecimal getResultadoComReivestimento(BigDecimal valorInicial) {
		BigDecimal caixa = new BigDecimal(valorInicial.doubleValue());
		Collections.sort(resultados);
		for (int ano=anoInicial; ano<=anoFinal; ano++) 
			for (int mes=1; mes<=12; mes++) {
				caixa = getResultadoMensalComReivestimento(caixa, ano, mes);
			}
		return caixa;
	}
	
	public BigDecimal getResultadoMensalComReivestimento(BigDecimal caixa, Integer ano, Integer mes) {
		BigDecimal valor1Lote = getValorInvestido(mes, ano);
		BigDecimal numLotes = new BigDecimal(Math.round(caixa.divide(valor1Lote,RoundingMode.HALF_EVEN).doubleValue()));
		BigDecimal valorGanho = getResultadoMensal(mes, ano).multiply(numLotes);
		caixa = caixa.add(valorGanho);
		return caixa;
	}
	
	public String imprime(TipoRelatorio tipoRelatorio) throws CotacaoInexistenteEX {
		String string = "";
		
		if (tipoRelatorio == TipoRelatorio.OPERACOES ||
			tipoRelatorio == TipoRelatorio.MENSAL ||
			tipoRelatorio == TipoRelatorio.ANUAL ||
			tipoRelatorio == TipoRelatorio.COMPLETO) {
			string += "\nInício da Simulação: " + new Date();
		}
		
		if (tipoRelatorio == TipoRelatorio.OPERACOES ||
			tipoRelatorio == TipoRelatorio.COMPLETO) {
			string += "\n"+imprimeOperacoes();
		}
		
		if (tipoRelatorio == TipoRelatorio.MENSAL ||
			tipoRelatorio == TipoRelatorio.OPERACOES ||
			tipoRelatorio == TipoRelatorio.COMPLETO) {
			string += "\n"+imprimeResultadosMensais();
		}
		
		if (tipoRelatorio == TipoRelatorio.ANUAL ||
			tipoRelatorio == TipoRelatorio.MENSAL ||
			tipoRelatorio == TipoRelatorio.OPERACOES ||
			tipoRelatorio == TipoRelatorio.COMPLETO) {
			string += "\n"+imprimeResultadosAnuais();
		}
		
		if (tipoRelatorio == TipoRelatorio.OPERACOES ||
			tipoRelatorio == TipoRelatorio.MENSAL ||
			tipoRelatorio == TipoRelatorio.ANUAL ||
			tipoRelatorio == TipoRelatorio.COMPLETO) {
			string += "\n"+imprimeResultadoConsolidado();
			string += "\nFim da Simulação: " + new Date();
		}
		
		if (tipoRelatorio == TipoRelatorio.CSV) {
			string += "\n"+imprimeCSV();
		}
		
		if (tipoRelatorio == TipoRelatorio.CSV_MENSAL) {
			string += "\n"+imprimeResultadosMensaisCSV();
		}
		
		if (tipoRelatorio == TipoRelatorio.CSV_MENSAL) {
			string += "\n"+imprimeResultadosMensaisCSV();
		}
		
		if (tipoRelatorio == TipoRelatorio.REINVESTIMENTO) {
			string += "\n"+imprimeResultadoComReinvestimento();
		}
		
		return string;
	}
	
	private String imprimeOperacoes() {
		String string = "";
		Collections.sort(resultados);
		Mes mesAnterior = null;
		for (ValorOperacao valorOperacao : resultados) {
			if (!valorOperacao.getMes().equals(mesAnterior))
				string += "\n";
			string += valorOperacao + "\n";
			mesAnterior = valorOperacao.getMes();
		}
		return string;
	}
	
	private String imprimeResultadosMensais() {
		String string = "";
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			string += "\n";
			for (int iMes=1; iMes <= 12; iMes++) {
				Mes mes = new Mes(iMes,ano);
				if (possui(mes)) {
					string += mes + " = " + SATAUtil.formataNumero(getResultadoPercentualMensal(mes)) 
							+ "% (" + SATAUtil.formataNumero(getResultadoMensal(mes)) + "/" 
							+ SATAUtil.formataNumero(getValorInvestido(mes)) + ")\n";
				}
			}
		}
		return string;
	}
	
	private String imprimeResultadosMensaisCSV() {
		String string = "Mês;Valor;Valor Investido;Percentual";
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			string += "\n";
			for (int iMes=1; iMes <= 12; iMes++) {
				Mes mes = new Mes(iMes,ano);
				if (possui(mes)) {
					string += mes + ";" + SATAUtil.formataNumero(getResultadoMensal(mes)) + ";" 
							+ SATAUtil.formataNumero(getValorInvestido(mes)) + ";"
							+ SATAUtil.formataNumero(getResultadoPercentualMensal(mes)) + "\n";
				}
			}
		}
		return string;
	}
	
	private String imprimeResultadosAnuais() {
		String string = "";
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			string += ano + " = " + SATAUtil.formataNumero(getResultadoPercentualAnual(ano)) 
					+ "% (" + SATAUtil.formataNumero(getResultadoAnual(ano)) + "/" +
					SATAUtil.formataNumero(getValorInvestido(1,ano)) + ")\n";
		}
		return string;
	}
	
	private String imprimeResultadoComReinvestimento() {
		BigDecimal valorInicial = new BigDecimal(100);
		BigDecimal valorFinal = getResultadoComReivestimento(valorInicial);
		BigDecimal percentual = valorFinal.subtract(valorInicial).divide(valorInicial, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
		BigDecimal caixa = valorInicial;
		String string = "Valor Inicial: " + SATAUtil.formataNumero(valorInicial);
		for (int ano=anoInicial; ano<=anoFinal; ano++) 
			for (int mes=1; mes<=12; mes++) {
				caixa = getResultadoMensalComReivestimento(caixa, ano, mes);
				string += "\n"+new Mes(mes,ano) + ": " + SATAUtil.formataNumero(caixa);
			}
		string += "\nValor Final: " + SATAUtil.formataNumero(valorFinal);
		string += "\nPercentual: " + SATAUtil.formataNumero(percentual)+"%";
		return string;
	}
	
	private String imprimeResultadoConsolidado() {
		BigDecimal soma = BigDecimal.ZERO;
		BigDecimal qtdAnos = BigDecimal.ZERO;
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			soma = soma.add(getResultadoPercentualAnual(ano));
			qtdAnos = qtdAnos.add(BigDecimal.ONE);
		}
		BigDecimal mediaAnual = soma.divide(qtdAnos, RoundingMode.HALF_EVEN);
		BigDecimal mediaMensal = mediaAnual.divide(new BigDecimal(12), RoundingMode.HALF_EVEN);
		
		String string = "Média Anual: " + SATAUtil.formataNumero(mediaAnual) + "%";
		string += "\nMédia Mensal: " + SATAUtil.formataNumero(mediaMensal) + "%";
		return string;
	}
	
	private String imprimeCSV() {
		String string = "Operação;Dia;Valor;Preço de Exercício;Preço da Ação;Volatilidade\n";
		Collections.sort(resultados);
		for (ValorOperacao valorOperacao : resultados) {
			string += imprimeCSV(valorOperacao.getOperacao(), valorOperacao.getMes()) + "\n";
		}
		return string;
	}
	
	public String imprimeCSV(Operacao operacao, Mes mes) {
		String string = "";
		ValorOperacao valorOperacao = getValorOperacao(operacao, mes);
		string += valorOperacao.getOperacao()+";";
		string += valorOperacao.getPreco().getDia()+";";
		string += SATAUtil.formataNumero(valorOperacao.getValor(),4)+";";
		if (valorOperacao.getPreco() instanceof PrecoOpcao) {
			string += SATAUtil.formataNumero(((PrecoOpcao)valorOperacao.getPreco()).getPrecoExercicioOpcao(),4)+";";
			string += SATAUtil.formataNumero(((PrecoOpcao)valorOperacao.getPreco()).getPrecoAcao(),4)+";";
		}
		else string += ";;";
		string += SATAUtil.formataNumero(valorOperacao.getPreco().getVolatilidade(),4);
		return string;
	}
	
	public BigDecimal calculaResultadoPercentualMensal(Mes mes, BigDecimal valor) {
		BigDecimal valorInicial = getValorInvestido(mes);
		BigDecimal resultadoNominal = getResultadoMensal(mes).add(valor);
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}
	
	public void remove(Mes mes) {
		List<ValorOperacao> remover = new ArrayList<ValorOperacao>();
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().equals(mes)) {
				remover.add(valorOperacao);
			}
		}
		resultados.removeAll(remover);
	}
	
	public boolean possui(Mes mes) {
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().equals(mes))
				return true;
		}
		return false;
	}
	
	public boolean possui(Operacao operacao, Mes mes) {
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().equals(mes) &&
					valorOperacao.getOperacao().equals(operacao))
				return true;
		}
		return false;
	}
	
	public void limpa() {
		resultados.clear();
	}
	
	private ValorOperacao getValorOperacaoReversa(Operacao operacao, Integer mes, Integer ano) {
		return getValorOperacao(operacao.getReversa(), mes, ano);
	}
	
	public void setAnoInicial(Integer anoInicial) {
		this.anoInicial = anoInicial;
	}
	public Integer getAnoInicial() {
		return anoInicial;
	}
	public void setAnoFinal(Integer anoFinal) {
		this.anoFinal = anoFinal;
	}
	public Integer getAnoFinal() {
		return anoFinal;
	}
	public List<ValorOperacao> getResultados() {
		return resultados;
	}
	public void setResultados(List<ValorOperacao> resultados) {
		this.resultados = resultados;
	}
	public TipoCalculoValorInvestido getTipoCalculoValorInvestido() {
		return tipoCalculoValorInvestido;
	}
	public void setTipoCalculoValorInvestido(
			TipoCalculoValorInvestido tipoCalculoValorInvestido) {
		this.tipoCalculoValorInvestido = tipoCalculoValorInvestido;
	}
}
