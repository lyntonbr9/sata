package sata.domain.to;

import java.util.ArrayList;
import java.util.List;

public class OperacaoRealizadaTO{
	
	public static final String OPERACAO_INICIAL = "I";
	public static final String OPERACAO_FINAL = "F";
	public static final String OPERACAO_POSICAO_VENDIDA = "V";
	public static final String OPERACAO_POSICAO_COMPRADA = "C";
	public static final String OPERACAO_POSICAO_NEUTRA = "N";
	
	private String tipo;
	private String codigoAtivo;
	private String posicao;
	private String valor;
	private String valorAlertaSuperior;
	private String valorAlertaInferior;
	private int quantidadeLotes;
	private String dataExecucao;
	private String porcentagemAlerta;
	private boolean acompanhar;
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCodigoAtivo() {
		return codigoAtivo;
	}
	public void setCodigoAtivo(String codigoAtivo) {
		this.codigoAtivo = codigoAtivo;
	}
	public String getPosicao() {
		return posicao;
	}
	public void setPosicao(String posicao) {
		this.posicao = posicao;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public double getValorDouble() {
		return Double.parseDouble(this.valor) / 100;
	}
	public String getValorAlertaSuperior() {
		return valorAlertaSuperior;
	}
	public double getValorAlertaSuperiorDouble() {
		return Double.parseDouble(this.valorAlertaSuperior) / 100;
	}
	public void setValorAlertaSuperior(String valorAlertaSuperior) {
		this.valorAlertaSuperior = valorAlertaSuperior;
	}
	public String getValorAlertaInferior() {
		return valorAlertaInferior;
	}
	public double getValorAlertaInferiorDouble() {
		return Double.parseDouble(this.valorAlertaInferior) / 100;
	}
	public void setValorAlertaInferior(String valorAlertaInferior) {
		this.valorAlertaInferior = valorAlertaInferior;
	}
	public int getQuantidadeLotes() {
		return quantidadeLotes;
	}
	public void setQuantidadeLotes(int quantidadeLotes) {
		this.quantidadeLotes = quantidadeLotes;
	}
	public String getDataExecucao() {
		return dataExecucao;
	}
	public void setDataExecucao(String dataExecucao) {
		this.dataExecucao = dataExecucao;
	}
	public boolean isAcompanhar() {
		return acompanhar;
	}
	public void setAcompanhar(boolean acompanhar) {
		this.acompanhar = acompanhar;
	}
	public String getPorcentagemAlerta() {
		return porcentagemAlerta;
	}
	public void setPorcentagemAlerta(String porcentagemAlerta) {
		this.porcentagemAlerta = porcentagemAlerta;
	}
	public double getPorcentagemAlertaDouble() {
		return Double.parseDouble(this.porcentagemAlerta) / 100;
	}
	
	public static List<OperacaoRealizadaTO> getOperacoesParaAcompanhar(){
		List<OperacaoRealizadaTO> listaOperacoesParaAcompanhar = new ArrayList<OperacaoRealizadaTO>();
		OperacaoRealizadaTO operacaoParaAcompanhar = new OperacaoRealizadaTO();
		operacaoParaAcompanhar.setTipo(OPERACAO_INICIAL);
		operacaoParaAcompanhar.setCodigoAtivo("PETRD21");
		operacaoParaAcompanhar.setPosicao(OPERACAO_POSICAO_VENDIDA);
		operacaoParaAcompanhar.setValor("321");
		operacaoParaAcompanhar.setValorAlertaSuperior("500");
		operacaoParaAcompanhar.setValorAlertaInferior("100");
		operacaoParaAcompanhar.setQuantidadeLotes(400);
		operacaoParaAcompanhar.setDataExecucao("20110312");
		operacaoParaAcompanhar.setAcompanhar(true);
		operacaoParaAcompanhar.setPorcentagemAlerta("7");
		listaOperacoesParaAcompanhar.add(operacaoParaAcompanhar);
		
		operacaoParaAcompanhar = new OperacaoRealizadaTO();
		operacaoParaAcompanhar.setTipo(OPERACAO_INICIAL);
		operacaoParaAcompanhar.setCodigoAtivo("PETRD24");
		operacaoParaAcompanhar.setPosicao(OPERACAO_POSICAO_COMPRADA);
		operacaoParaAcompanhar.setValor("87");
		operacaoParaAcompanhar.setValorAlertaSuperior("200");
		operacaoParaAcompanhar.setValorAlertaInferior("2");
		operacaoParaAcompanhar.setQuantidadeLotes(800);
		operacaoParaAcompanhar.setDataExecucao("20110312");
		operacaoParaAcompanhar.setAcompanhar(true);
		operacaoParaAcompanhar.setPorcentagemAlerta("8");
		listaOperacoesParaAcompanhar.add(operacaoParaAcompanhar);
		
		operacaoParaAcompanhar = new OperacaoRealizadaTO();
		operacaoParaAcompanhar.setTipo(OPERACAO_INICIAL);
		operacaoParaAcompanhar.setCodigoAtivo("PETR4");
		operacaoParaAcompanhar.setPosicao(OPERACAO_POSICAO_NEUTRA);
		operacaoParaAcompanhar.setValor("2398");
		operacaoParaAcompanhar.setValorAlertaSuperior("2490");
		operacaoParaAcompanhar.setValorAlertaInferior("2310");
		operacaoParaAcompanhar.setQuantidadeLotes(400);
		operacaoParaAcompanhar.setDataExecucao("20110312");
		operacaoParaAcompanhar.setAcompanhar(true);
		operacaoParaAcompanhar.setPorcentagemAlerta("5");
		listaOperacoesParaAcompanhar.add(operacaoParaAcompanhar);
		
		operacaoParaAcompanhar = new OperacaoRealizadaTO();
		operacaoParaAcompanhar.setTipo(OPERACAO_INICIAL);
		operacaoParaAcompanhar.setCodigoAtivo("VALE5");
		operacaoParaAcompanhar.setPosicao(OPERACAO_POSICAO_NEUTRA);
		operacaoParaAcompanhar.setValor("4186");
		operacaoParaAcompanhar.setValorAlertaSuperior("4290");
		operacaoParaAcompanhar.setValorAlertaInferior("4110");
		operacaoParaAcompanhar.setQuantidadeLotes(200);
		operacaoParaAcompanhar.setDataExecucao("20110312");
		operacaoParaAcompanhar.setAcompanhar(true);
		operacaoParaAcompanhar.setPorcentagemAlerta("5");
		listaOperacoesParaAcompanhar.add(operacaoParaAcompanhar);
		
		return listaOperacoesParaAcompanhar;
		
	}
}
