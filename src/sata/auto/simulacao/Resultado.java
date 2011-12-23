package sata.auto.simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.to.DataTO;
import sata.auto.to.ValorOperacaoTO;
import sata.domain.util.SATAUtil;

public class Resultado {
	
	private List<ValorOperacaoTO> resultados = new ArrayList<ValorOperacaoTO>();
	
	private Integer anoInicial;
	private Integer anoFinal;
	
	public void addValorOperacao(ValorOperacaoTO valorOperacao) {
		resultados.add(valorOperacao);
	}
	
	public void setResultadoMensal(Operacao operacao, Integer mes, Integer ano, Preco preco) {
		addValorOperacao(new ValorOperacaoTO(operacao, new DataTO(mes,ano), preco));
	}
	
	public ValorOperacaoTO getValorOperacao(Operacao operacao, Integer mes, Integer ano) {
		for (ValorOperacaoTO valorOperacao : resultados) {
			if (valorOperacao.getOperacao().equals(operacao) &&
				valorOperacao.getData().getMes().equals(mes) &&
				valorOperacao.getData().getAno().equals(ano)) {
				return valorOperacao;
			}
		}
		return null;
	}
	
	public BigDecimal getResultadoMensal(Operacao operacao, Integer mes, Integer ano) {
		for (ValorOperacaoTO valorOperacao : resultados) {
			if (valorOperacao.getOperacao().equals(operacao) &&
				valorOperacao.getData().getMes().equals(mes) &&
				valorOperacao.getData().getAno().equals(ano)) {
				return valorOperacao.getPreco().getValor();
			}
		}
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getResultadoMensal(Integer mes, Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacaoTO valorOperacao : resultados) {
			if (valorOperacao.getData().getMes().equals(mes) &&
				valorOperacao.getData().getAno().equals(ano)) {
				valor = valor.add(valorOperacao.getPreco().getValor());
			}
		}
		return valor;
	}
	
	public BigDecimal getValorInvestido(Integer mes, Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacaoTO valorOperacao : resultados) {
			if (valorOperacao.getOperacao() instanceof Compra &&
				valorOperacao.getData().getMes().equals(mes) &&
				valorOperacao.getData().getAno().equals(ano)) {
				valor = valor.add(valorOperacao.getPreco().getValor());
			}
		}
		return valor;
	}
	
	public BigDecimal getResultadoPercentualMensal(Integer mes, Integer ano) {
		BigDecimal valorInicial = getValorInvestido(mes, ano).negate();
		BigDecimal resultadoNominal = getResultadoMensal(mes,ano);
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}
	
	public BigDecimal getResultadoAnual(Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacaoTO valorOperacao : resultados) {
			if (valorOperacao.getData().getAno().equals(ano)) {
				valor = valor.add(valorOperacao.getPreco().getValor());
			}
		}
		return valor;
	}
	
	public BigDecimal getResultadoPercentualAnual(Integer ano) {
		BigDecimal valorInicial = getValorInvestido(1, ano).negate();
		BigDecimal resultadoNominal = getResultadoAnual(ano);
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}
	
	public String imprime(boolean anual, boolean mensal, boolean operacoes, boolean calculos) throws CotacaoInexistenteEX {
		String string = "";
		if (calculos) string = string.concat(imprimeCalculos());
		if (operacoes) string = string.concat("\n"+imprimeOperacoes());
		if (mensal) string = string.concat("\n"+imprimeResultadosMensais());
		if (anual) string = string.concat("\n"+imprimeResultadosAnuais());
		return string;
	}
	
	private String imprimeOperacoes() {
		String string = "";
		Collections.sort(resultados);
		DataTO dataAnterior = null;
		for (ValorOperacaoTO valorOperacao : resultados) {
			if (!valorOperacao.getData().equals(dataAnterior))
				string = string.concat("\n");
			string = string.concat(valorOperacao + "\n");
			dataAnterior = valorOperacao.getData();
		}
		return string;
	}
	
	private String imprimeResultadosMensais() {
		String string = "";
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			string = string.concat("\n");
			for (int mes=1; mes <= 12; mes++) {
				DataTO data = new DataTO(mes,ano);
				if (possui(data)) {
					string = string.concat(data + " = " 
							+ SATAUtil.formataNumero(getResultadoPercentualMensal(mes, ano)) 
							+ "% (" + SATAUtil.formataNumero(getResultadoMensal(mes, ano)) + ")\n");
				}
			}
		}
		return string;
	}
	
	private String imprimeResultadosAnuais() {
		String string = "";
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			string = string.concat(ano + " = " + SATAUtil.formataNumero(getResultadoPercentualAnual(ano)) 
					+ "% (" + SATAUtil.formataNumero(getResultadoAnual(ano)) + ")\n");
		}
		return string;
	}
	
	private String imprimeCalculos() throws CotacaoInexistenteEX {
		String string = "";
		Collections.sort(resultados);
		DataTO dataAnterior = null;
		for (ValorOperacaoTO valorOperacao : resultados) {
			if (!valorOperacao.getData().equals(dataAnterior))
				string = string.concat("\n");
			string = string.concat(valorOperacao.getPreco() + "\n");
			dataAnterior = valorOperacao.getData();
		}
		return string;
	}
	
	public void remove(DataTO data) {
		List<ValorOperacaoTO> remover = new ArrayList<ValorOperacaoTO>();
		for (ValorOperacaoTO valorOperacao : resultados) {
			if (valorOperacao.getData().equals(data)) {
				remover.add(valorOperacao);
			}
		}
		resultados.removeAll(remover);
	}
	
	public boolean possui(DataTO data) {
		for (ValorOperacaoTO valorOperacao : resultados) {
			if (valorOperacao.getData().equals(data))
				return true;
		}
		return false;
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
}
