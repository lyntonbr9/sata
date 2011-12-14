package sata.domain.to;

public class CotacaoAtivoTO {

	private String codigo;
	private String abertura;
	private String maxima;
	private String minima;
	private String fechamento;
	private String periodo;
	private String tipoPeriodo;
	private String ano;
	private String volume;
	private double volatilidadeAnual;
	private double volatilidadeMensal;
	
	
	public double getVolatilidadeAnual() {
		return volatilidadeAnual;
	}

	public void setVolatilidadeAnual(double volatilidadeAnual) {
		this.volatilidadeAnual = volatilidadeAnual;
	}

	public double getVolatilidadeMensal() {
		return volatilidadeMensal;
	}

	public void setVolatilidadeMensal(double volatilidadeMensal) {
		this.volatilidadeMensal = volatilidadeMensal;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(String tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public String getAbertura() {
		return abertura;
	}

	public void setAbertura(String abertura) {
		this.abertura = abertura;
	}

	public String getMaxima() {
		return maxima;
	}

	public void setMaxima(String maxima) {
		this.maxima = maxima;
	}

	public String getMinima() {
		return minima;
	}

	public void setMinima(String minima) {
		this.minima = minima;
	}

	public String getFechamento() {
		return fechamento;
	}

	public void setFechamento(String fechamento) {
		this.fechamento = fechamento;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


}
