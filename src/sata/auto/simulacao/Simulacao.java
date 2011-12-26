package sata.auto.simulacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.operacao.Operacao;
import sata.auto.operacao.ativo.preco.Preco;
import sata.auto.to.DataTO;

public class Simulacao {
	
	private List<Operacao> operacoes = new ArrayList<Operacao>();
	private Integer anoInicial;
	private Integer anoFinal;
	private int tipoCalculoValorInvestido;
	private BigDecimal caixa;
	
	public Resultado getResultado() {
		Resultado resultado = new Resultado();
		resultado.setAnoInicial(anoInicial);
		resultado.setAnoFinal(anoFinal);
		resultado.setTipoCalculoValorInvestido(tipoCalculoValorInvestido);
		for (int ano=anoInicial.intValue(); ano <= anoFinal.intValue(); ano++) {
			for (int mes=1; mes <= 12; mes++) {
				DataTO data = new DataTO(mes,ano);
				for (Operacao operacao : operacoes) {
					executaOperacao(resultado, operacao, data);
					executaOperacao(resultado, operacao.reversa(), data);
				}
			}
		}
		return resultado;
	}
	
	private void executaOperacao(Resultado resultado, Operacao operacao, DataTO data) {
		try {
			Preco preco = operacao.getPreco(data);
			if (operacao.condicaoVerdadeira(preco)) {
				resultado.setResultadoMensal(operacao, data.getMes(), data.getAno(), preco);
			}
			
		} catch (CotacaoInexistenteEX e) {
			resultado.remove(data);
		}
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
	public int getTipoCalculoValorInvestido() {
		return tipoCalculoValorInvestido;
	}
	public void setTipoCalculoValorInvestido(int tipoCalculoValorInvestido) {
		this.tipoCalculoValorInvestido = tipoCalculoValorInvestido;
	}
}
