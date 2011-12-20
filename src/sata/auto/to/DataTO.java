package sata.auto.to;

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
		return new DataTO(mes-qtd, ano);
	}
	
	public String getDia(Integer dia) {
		return ano + getMesFormatado() + dia;
	}
	
	public String getDiaInicial() {
		return ano + getMesFormatado() + "01";
	}
	
	public String getDiaFinal() {
		return ano + getMesFormatado() + "31";
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
