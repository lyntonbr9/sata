package sata.auto.to;


public class Mes implements Comparable<Mes>{
	private Integer mes;
	private Integer ano;
	
	public Mes() {}
	
	public Mes(Integer mes, Integer ano) {
		this.mes = mes;
		this.ano = ano;
	}
	
	public Mes getMesAnterior() {
		return getMesAnterior(1);
	}
	
	public Mes getMesAnterior(int qtd) {
		if (mes > qtd)
			return new Mes(mes-qtd, ano);
		else
			return new Mes(12-(mes-qtd), ano-1);
	}
	
	public Mes getMesPosterior() {
		if (mes < 12)
			return new Mes(mes+1, ano);
		else
			return new Mes(1, ano+1);
	}
	
	public Dia getDia(Integer dia) {
		return new Dia (dia, this);
	}
	
	public Dia getDiaInicial() {
		return getDia(1);
	}
	
	public Dia getDiaFinal() {
		return getDia(31);
	}
	
	@Override
	public String toString() {
		return getMesFormatado() + "/" + ano;
	}
	
	@Override
	public int compareTo(Mes other) {
		int compAno = ano.compareTo(other.ano);
		if (compAno != 0) return compAno;
		else return mes.compareTo(other.mes);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Mes))
			return false;
		return ((Mes)other).ano.equals(this.ano) 
			&& ((Mes)other).mes.equals(this.mes);
	}
	
	private String getMesFormatado() {
		return String.format("%02d", mes);
	}
	
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
}
