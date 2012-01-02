package sata.auto.operacao;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.simulacao.Resultado;
import sata.auto.to.Mes;


public class Stop {
	
	Atributo atributo;
	Operador operacao;
	double valor;
	
	public Stop() {}
	
	public Stop(Atributo atributo, Operador operacao, double valor) {
		this.atributo = atributo;
		this.operacao = operacao;
		this.valor = valor;
	}

	public boolean stop(Mes mes, Resultado resultado) {
		double valorComparacao = 0;
		switch (atributo) {
		case PERCENTUAL_OPERACAO:
			valorComparacao = resultado.getResultadoPercentualMensal(mes).doubleValue();
		}
		return Condicao.verdadeira(valor, valorComparacao, operacao);
	}

	public Atributo getAtributo() {
		return atributo;
	}
	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}
	public Operador getOperacao() {
		return operacao;
	}
	public void setOperacao(Operador operacao) {
		this.operacao = operacao;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
}
