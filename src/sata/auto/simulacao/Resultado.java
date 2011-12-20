package sata.auto.simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sata.auto.operacao.Compra;
import sata.auto.operacao.Operacao;
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
	
	public void setResultadoMensal(Operacao operacao, Integer mes, Integer ano, BigDecimal valor) {
		addValorOperacao(new ValorOperacaoTO(operacao, new DataTO(mes,ano), valor));
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
				return valorOperacao.getValor();
			}
		}
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getResultadoMensal(Integer mes, Integer ano) {
		BigDecimal valor =  BigDecimal.ZERO;
		for (ValorOperacaoTO valorOperacao : resultados) {
			if (valorOperacao.getData().getMes().equals(mes) &&
				valorOperacao.getData().getAno().equals(ano)) {
				valor = valor.add(valorOperacao.getValor());
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
				valor = valor.add(valorOperacao.getValor());
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
				valor = valor.add(valorOperacao.getValor());
			}
		}
		return valor;
	}
	
	public BigDecimal getResultadoPercentualAnual(Integer ano) {
		BigDecimal valorInicial = getValorInvestido(1, ano).negate();
		BigDecimal resultadoNominal = getResultadoAnual(ano);
		return resultadoNominal.divide(valorInicial,RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}
	
	public String imprimeResultadosMensais() {
		String string = "";
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			for (int mes=1; mes <= 12; mes++) {
				string = string.concat(new DataTO(mes,ano) + " = " 
						+ SATAUtil.formataNumero(getResultadoPercentualMensal(mes, ano)) + "%\n");
			}
		}
		return string;
	}
	
	public String imprimeResultadosAnuais() {
		String string = "";
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			string = string.concat(ano + " = " + SATAUtil.formataNumero(getResultadoPercentualAnual(ano)) + "%\n");
		}
		return string;
	}
	
	@Override
	public String toString() {
		String string = "";
		Collections.sort(resultados);
		for (ValorOperacaoTO valorOperacao : resultados) {
			string = string.concat(valorOperacao + "\n");
		}
		return string;
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
