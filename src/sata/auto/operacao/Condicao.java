package sata.auto.operacao;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.operacao.ativo.preco.Preco;


public class Condicao {
	
	Atributo atributo;
	Operador operacao;
	double valor;
	
	public Condicao() {}
	
	public Condicao(Atributo atributo, Operador operacao, double valor) {
		this.atributo = atributo;
		this.operacao = operacao;
		this.valor = valor;
	}
	
	public boolean verdadeira(Preco preco) {
		double valorComparacao = 0;
		
		switch (atributo) {
		case PRECO:
			valorComparacao = preco.getValor().doubleValue();
			break;
		case VOLATILIDADE:
			valorComparacao = preco.getVolatilidade().doubleValue();
			break;
		}
		return verdadeira(valor, valorComparacao, operacao);
	}
	
	public static boolean verdadeira(double valor, double valorComparacao, Operador operador) {
		switch (operador) {
		case IGUAL:
			return valorComparacao == valor;
			
		case DIFERENTE:
			return valorComparacao != valor;
			
		case MAIOR:
			return valorComparacao > valor;
			
		case MENOR:
			return valorComparacao < valor;
			
		case MAIOR_IGUAL:
			return valorComparacao >= valor;
			
		case MENOR_IGUAL:
			return valorComparacao <= valor;
		}
		
		return false;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Condicao))
			return false;
		return ((Condicao)other).atributo == atributo
			&& ((Condicao)other).operacao == operacao
			&& ((Condicao)other).valor == valor;
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
