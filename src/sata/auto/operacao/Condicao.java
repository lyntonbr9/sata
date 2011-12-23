package sata.auto.operacao;


public class Condicao {
	
	public static final int PRECO = 0;
	public static final int VOLATILIDADE = 1;
	
	public static final int IGUAL = 2;
	public static final int DIFERENTE = 3;
	public static final int MAIOR = 4;
	public static final int MENOR = 5;
	public static final int MAIOR_IGUAL = 6;
	public static final int MENOR_IGUAL = 7;
	
	int atributo;
	int operacao;
	double valor;
	
	public Condicao() {}
	
	public Condicao(int atributo, int operacao, double valor) {
		this.atributo = atributo;
		this.operacao = operacao;
		this.valor = valor;
	}

	public int getAtributo() {
		return atributo;
	}
	public void setAtributo(int atributo) {
		this.atributo = atributo;
	}
	public int getOperacao() {
		return operacao;
	}
	public void setOperacao(int operacao) {
		this.operacao = operacao;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
}
