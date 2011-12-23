package sata.auto.to;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class DataTO implements Comparable<DataTO>{
	private Integer mes;
	private Integer ano;
	
	public DataTO() {}
	
	public DataTO(Integer mes, Integer ano) {
		this.mes = mes;
		this.ano = ano;
	}
	
	public DataTO getMesAnterior() {
		return getMesAnterior(1);
	}
	
	public DataTO getMesAnterior(int qtd) {
		if (mes > qtd)
			return new DataTO(mes-qtd, ano);
		else
			return new DataTO(12-(mes-qtd), ano-1);
	}
	
	public String getDia(Integer dia) {
		return getDataFormatada(dia, "yyyyMMdd");
	}
	
	public String getDiaInicial() {
		return getDia(1);
	}
	
	public String getDiaFinal() {
		return getDia(31);
	}
	
	public String getDataFormatada(Integer dia, String pattern) {
		SimpleDateFormat formato = new SimpleDateFormat(pattern);  
		return formato.format(new GregorianCalendar(ano, mes-1, dia).getTime()); 
	}
	
	@Override
	public String toString() {
		return getMesFormatado() + "/" + ano;
	}
	
	@Override
	public int compareTo(DataTO other) {
		int compAno = ano.compareTo(other.ano);
		if (compAno != 0) return compAno;
		else return mes.compareTo(other.mes);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof DataTO))
			return false;
		return ((DataTO)other).ano.equals(ano) 
			&& ((DataTO)other).mes.equals(mes);
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
