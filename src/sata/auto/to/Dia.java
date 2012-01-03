package sata.auto.to;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Dia implements Comparable<Dia> {
	
	Integer dia;
	Mes mes;
	
	public Dia() {}
	
	public Dia(Integer dia, Mes mes) {
		this.dia = dia;
		this.mes = mes;
	}
	
	public Dia(Integer dia, Integer mes, Integer ano) {
		this.dia = dia;
		this.mes = new Mes(mes, ano);
	}
	
	public String format(String pattern) {
		SimpleDateFormat formato = new SimpleDateFormat(pattern);  
		return formato.format(new GregorianCalendar(mes.getAno(), mes.getMes()-1, dia).getTime()); 
	}
	
	public Dia getProximoDia() {
		Calendar calendar = getCalendar();
		calendar.add(Calendar.DATE, 1);
		return new Dia(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
	}
	
	public Dia getMesPosterior() {
		return new Dia(dia, mes.getMesPosterior());
	}
	
	public String formatoBanco() {
		return format("yyyyMMdd");
	}
	
	public String formatoPadrao() {
		return format("dd/MM/yyyy");
	}
	
	public Calendar getCalendar() {
		return new GregorianCalendar(mes.getAno(), mes.getMes()-1, dia);
	}
	
	@Override
	public int compareTo(Dia other) {
		int comp = mes.compareTo(other.mes);
		if (comp != 0) return comp;
		else return dia.compareTo(other.dia);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Dia))
			return false;
		return ((Dia)other).dia.equals(dia) 
			&& ((Dia)other).mes.equals(mes);
	}
	
	@Override
	public String toString() {
		return formatoPadrao();
	}

	public Integer getDia() {
		return dia;
	}
	public void setDia(Integer dia) {
		this.dia = dia;
	}
	public Mes getMes() {
		return mes;
	}
	public void setMes(Mes mes) {
		this.mes = mes;
	}
}
