package sata.auto.simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.Venda;
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
	
	public BigDecimal getResultadoMensal(Integer mes, Integer ano) {
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
	
	public BigDecimal getValorInvestido(Integer mes, Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().getMes().equals(mes) &&
					valorOperacao.getMes().getAno().equals(ano)) {
				
				switch (tipoCalculoValorInvestido) {
				case TOTAL_COMPRADO:
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
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}
	
	public BigDecimal getResultadoAnual(Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacao valorOperacao : resultados) {
			if (valorOperacao.getMes().getAno().equals(ano)) {
				valor = valor.add(valorOperacao.getValor());
			}
		}
		return valor;
	}
	
	public BigDecimal getResultadoPercentualAnual(Integer ano) {
		BigDecimal valorInicial = getValorInvestido(1, ano);
		BigDecimal resultadoNominal = getResultadoAnual(ano);
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}
	
	public String imprime(boolean anual, boolean mensal, boolean operacoes, boolean calculos, boolean csv) throws CotacaoInexistenteEX {
		String string = "";
		if (calculos) string = string.concat(imprimeCalculos());
		if (operacoes) string = string.concat("\n"+imprimeOperacoes());
		if (mensal) string = string.concat("\n"+imprimeResultadosMensais());
		if (anual) string = string.concat("\n"+imprimeResultadosAnuais());
		if (csv) string = string.concat("\n"+imprimeCSV());
		string = string.concat("\n"+imprimeResultadoConsolidado());
		return string;
	}
	
	private String imprimeOperacoes() {
		String string = "";
		Collections.sort(resultados);
		Mes mesAnterior = null;
		for (ValorOperacao valorOperacao : resultados) {
			if (!valorOperacao.getMes().equals(mesAnterior))
				string = string.concat("\n");
			string = string.concat(valorOperacao + "\n");
			mesAnterior = valorOperacao.getMes();
		}
		return string;
	}
	
	private String imprimeResultadosMensais() {
		String string = "";
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			string = string.concat("\n");
			for (int iMes=1; iMes <= 12; iMes++) {
				Mes mes = new Mes(iMes,ano);
				if (possui(mes)) {
					string = string.concat(mes + " = " 
							+ SATAUtil.formataNumero(getResultadoPercentualMensal(mes)) 
							+ "% (" + SATAUtil.formataNumero(getResultadoMensal(mes)) + "/" 
							+ SATAUtil.formataNumero(getValorInvestido(mes))) + ")\n";
				}
			}
		}
		return string;
	}
	
	private String imprimeResultadosAnuais() {
		String string = "";
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			string = string.concat(ano + " = " + SATAUtil.formataNumero(getResultadoPercentualAnual(ano)) 
					+ "% (" + SATAUtil.formataNumero(getResultadoAnual(ano)) + "/" +
					SATAUtil.formataNumero(getValorInvestido(1,ano))) + ")\n";
		}
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
		string = string.concat("\nMédia Mensal: " + SATAUtil.formataNumero(mediaMensal) + "%");
		return string;
	}
	
	private String imprimeCalculos() throws CotacaoInexistenteEX {
		String string = "";
		Collections.sort(resultados);
		Mes mesAnterior = null;
		for (ValorOperacao valorOperacao : resultados) {
			if (!valorOperacao.getMes().equals(mesAnterior))
				string = string.concat("\n");
			string = string.concat(valorOperacao.getPreco() + "\n");
			mesAnterior = valorOperacao.getMes();
		}
		return string;
	}
	
	private String imprimeCSV() {
		String string = "";
		Collections.sort(resultados);
		Mes mesAnterior = null;
		string = string.concat(resultados.get(0).getMes().toString()+";");
		for (ValorOperacao valorOperacao : resultados) {
			if (mesAnterior != null && !valorOperacao.getMes().equals(mesAnterior)) {
				string = string.concat(SATAUtil.formataNumero(getResultadoMensal(mesAnterior),4) + "\n");
				string = string.concat(valorOperacao.getMes().toString()+";");
			}
			string = string.concat(SATAUtil.formataNumero(valorOperacao.getValor(),4) + ";");
			mesAnterior = valorOperacao.getMes();
		}
		string = string.concat(SATAUtil.formataNumero(getResultadoMensal(mesAnterior),4) + "\n");
		return string;
	}
	
	public String imprimeTituloCSV() {
		return "Operação;Dia;Valor;Preço de Exercício;Preço da Ação;Volatilidade";
	}
	
	public String imprimeCSV(Operacao operacao, Mes mes) {
		String string = "";
		ValorOperacao valorOperacao = getValorOperacao(operacao, mes);
		string = string.concat(valorOperacao.getOperacao()+";");
		string = string.concat(valorOperacao.getPreco().getDia()+";");
		string = string.concat(SATAUtil.formataNumero(valorOperacao.getValor(),4)+";");
		if (valorOperacao.getPreco() instanceof PrecoOpcao) {
			string = string.concat(SATAUtil.formataNumero(((PrecoOpcao)valorOperacao.getPreco()).getPrecoExercicioOpcao(),4)+";");
			string = string.concat(SATAUtil.formataNumero(((PrecoOpcao)valorOperacao.getPreco()).getPrecoAcao(),4)+";");
		}
		else string = string.concat(";;");
		string = string.concat(SATAUtil.formataNumero(valorOperacao.getPreco().getVolatilidade(),4));
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
	
	/*private ValorOperacao getValorOperacaoReversa(Operacao operacao, Mes mes) {
		return getValorOperacaoReversa(operacao, mes.getMes(), mes.getAno());
	}*/
	
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
