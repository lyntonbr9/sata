package sata.auto.simulacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import sata.auto.operacao.Operacao;
import sata.auto.to.DataTO;

public class Simulacao {
	
	private List<Operacao> operacoes = new ArrayList<Operacao>();
	private Integer anoInicial;
	private Integer anoFinal;
	private BigDecimal caixa;
	
	public Resultado getResultado() {
		Resultado resultado = new Resultado();
		resultado.setAnoInicial(anoInicial);
		resultado.setAnoFinal(anoFinal);
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			for (int mes=1; mes <= 12; mes++) {
				for (Operacao operacao : operacoes) {
					DataTO data = new DataTO(mes,ano);
					BigDecimal valor = operacao.getValor(data);
					resultado.setResultadoMensal(operacao, mes, ano, valor);
				}
			}
		}
		return resultado;
	}
	
	public List<Operacao> getOperacoes() {
		return operacoes;
	}
	public void setOperacoes(List<Operacao> operacoes) {
		this.operacoes = operacoes;
	}
	public Integer getAnoInicial() {
		return anoInicial;
	}
	public void setAnoInicial(Integer anoInicial) {
		this.anoInicial = anoInicial;
	}
	public Integer getAnoFinal() {
		return anoFinal;
	}
	public void setAnoFinal(Integer anoFinal) {
		this.anoFinal = anoFinal;
	}
	public void setCaixa(BigDecimal caixa) {
		this.caixa = caixa;
	}
	public BigDecimal getCaixa() {
		return caixa;
	}
}
