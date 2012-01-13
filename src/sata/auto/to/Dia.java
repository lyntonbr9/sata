package sata.auto.to;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Dia implements Comparable<Dia> {
	
	private static final String FORMATO_DATA_PADRAO = "dd/MM/yyyy";
	private static final String FORMATO_DATA_BANCO = "yyyyMMdd";
	
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
	
	public static Dia converte(String data) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMATO_DATA_PADRAO); 
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(sdf.parse(data)); 
		return converte(calendar);
	}
	
	public static Dia converte(Calendar calendar) {
		return new Dia(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
	}
	
	public String format(String pattern) {
		SimpleDateFormat formato = new SimpleDateFormat(pattern);  
		return formato.format(new GregorianCalendar(mes.getAno(), mes.getMes()-1, dia).getTime()); 
	}
	
	public Dia getDiaPosterior(int qtd) {
		Calendar calendar = getCalendar();
		calendar.add(Calendar.DATE, qtd);
		return converte(calendar);
	}
	
	public Dia getProximoDia() {
		return getDiaPosterior(1);
	}
	
	public Dia getDiaAnterior(int qtd) {
		return getDiaPosterior(-qtd);
	}
	
	public Dia getDiaAnterior() {
		return getDiaPosterior(-1);
	}
	
	public Dia getMesPosterior() {
		return new Dia(dia, mes.getMesPosterior());
	}
	
	public String formatoBanco() {
		return format(FORMATO_DATA_BANCO);
	}
	
	public String formatoPadrao() {
		return format(FORMATO_DATA_PADRAO);
	}
	
	public Calendar getCalendar() {
		return new GregorianCalendar(mes.getAno(), mes.getMes()-1, dia);
	}
	
	public Integer getAno() {
		return mes.getAno();
	}
	
	@Override
	public int compareTo(Dia other) {
		return new CompareToBuilder()
	    	       .append(mes, other.mes)
	    	       .append(dia, other.dia)
	    	       .toComparison();
	}
	
	public boolean lessThan(Dia dia) {
		return this.compareTo(dia) == -1;
	}
	
	public boolean lessEqualsThan(Dia dia) {
		return this.compareTo(dia) == -1
			|| this.equals(dia);
	}
	
	public boolean greaterThan(Dia dia) {
		return this.compareTo(dia) == 1;
	}
	
	public boolean greaterEqualsThan(Dia dia) {
		return this.compareTo(dia) == 1
			|| this.equals(dia);
	}
	
	@Override
	public String toString() {
		return formatoPadrao();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,27).
	       append(dia).
	       append(mes).
	       toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
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
