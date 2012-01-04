package sata.auto.to;

public class Variacao {
	
	Integer valorInicial;
	Integer valorFinal;
	Integer incremento = 1;
	
	public Variacao(Integer valor) {
		this.valorInicial = -valor;
		this.valorFinal = valor;
	}
	
	public Variacao(Integer valorInicial, Integer valorFinal) {
		this.valorInicial = valorInicial;
		this.valorFinal = valorFinal;
	}
	
	public Variacao(Integer valorInicial, Integer valorFinal, Integer incremento) {
		this.valorInicial = valorInicial;
		this.valorFinal = valorFinal;
		this.incremento = incremento;
	}

	public Integer getValorInicial() {
		return valorInicial;
	}
	public void setValorInicial(Integer valorInicial) {
		this.valorInicial = valorInicial;
	}
	public Integer getValorFinal() {
		return valorFinal;
	}
	public void setValorFinal(Integer valorFinal) {
		this.valorFinal = valorFinal;
	}
	public Integer getIncremento() {
		return incremento;
	}
	public void setIncremento(Integer incremento) {
		this.incremento = incremento;
	}
}
