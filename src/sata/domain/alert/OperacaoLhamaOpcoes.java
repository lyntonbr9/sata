package sata.domain.alert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sata.domain.dao.IOperacaoAlertaDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.OperacaoRealizadaTO;
import sata.domain.util.SATAUtil;
import sata.metastock.mail.SendMailUsingAuthentication;
import sata.metastock.robos.CotacaoLopesFilho;

public class OperacaoLhamaOpcoes {
	
	public static void main(String[] args) {
		
		System.out.println("ALERTA OperacaoLhamaOpcoes");
		
		Calendar cal = SATAUtil.getDataAtual();
		
		int hora = cal.get(Calendar.HOUR_OF_DAY);
		
		System.out.println("Hora: " + hora);
		
		IOperacaoAlertaDAO oaDAO = SATAFactoryFacade.getOperacaoAlertaDAO();
		
//		if (hora >= 10 && hora <= 17)
//		{
			List<OperacaoRealizadaTO> operacoesRealizadas = oaDAO.getOperacoesParaAcompanhar();
			
			List<OperacaoRealizadaTO> operacoesInvertidas = inverterOperacoes(copyList(operacoesRealizadas));
			
			List<OperacaoRealizadaTO> operacoesInvertidasAtualizadas = atualizarOperacoes(operacoesInvertidas);
			
			double saldoOperacoesRealizadas = getSaldo(operacoesRealizadas);
			
			System.out.println("saldoOperacoesRealizadas: " + saldoOperacoesRealizadas);
			
			double saldoOperacoesInvertidasAtualizadas = getSaldo(operacoesInvertidasAtualizadas);
			
			System.out.println("saldoOperacoesInvertidasAtualizadas: " + saldoOperacoesInvertidasAtualizadas);
			
			double saldoFinal = saldoOperacoesRealizadas + saldoOperacoesInvertidasAtualizadas;
			
			System.out.println("saldoFinal: " + saldoFinal);
			
			System.out.println("Porcentagem saldoFinal: " + (saldoFinal/getValorDeParametro(operacoesRealizadas))*100 + "%");
			
			alertaValorAlcancado(operacoesInvertidasAtualizadas);
			
			alertaPorcentagemAlcancada(saldoFinal, operacoesInvertidasAtualizadas);
			
			System.out.println("Operacoes Realizadas:");
			printList(operacoesRealizadas);
			
			System.out.println("Operacoes Invertidas Atualizadas:");
			printList(operacoesInvertidasAtualizadas);
			
//		}
	}
	
	public static void printList(List<OperacaoRealizadaTO> operacoes)
	{
		for(OperacaoRealizadaTO operacao : operacoes){
			System.out.println("CodigoAtivo: " + operacao.getCodigoAtivo());
//			System.out.println("Posicao: " + operacao.getPosicao());
//			System.out.println("PorcentagemGanhoAlerta: " + operacao.getPorcentagemGanhoAlerta() + "%");
//			System.out.println("PorcentagemPerdaAlerta: " + operacao.getPorcentagemPerdaAlerta() + "%");
//			System.out.println("QuantidadeLotes: " + operacao.getQuantidadeLotes());
			System.out.println("Valor: " + operacao.getValorDouble());
//			System.out.println("ValorAlertaSuperiorDouble: " + operacao.getValorAlertaSuperiorDouble());
//			System.out.println("ValorAlertaInferiorDouble: " + operacao.getValorAlertaInferiorDouble());
//			System.out.println("");
		}
	}
	
	public static List<OperacaoRealizadaTO> copyList(List<OperacaoRealizadaTO> operacoes){
		List<OperacaoRealizadaTO> novaLista = new ArrayList<OperacaoRealizadaTO>();
		for(OperacaoRealizadaTO operacao : operacoes){
			OperacaoRealizadaTO novaOperacao = new OperacaoRealizadaTO();
			novaOperacao.setAcompanhar(operacao.isAcompanhar());
			novaOperacao.setCodigoAtivo(operacao.getCodigoAtivo());
			novaOperacao.setDataExecucao(operacao.getDataExecucao());
			novaOperacao.setPorcentagemPerdaAlerta(operacao.getPorcentagemPerdaAlerta());
			novaOperacao.setPorcentagemGanhoAlerta(operacao.getPorcentagemGanhoAlerta());
			novaOperacao.setPosicao(operacao.getPosicao());
			novaOperacao.setQuantidadeLotes(operacao.getQuantidadeLotes());
			novaOperacao.setValor(operacao.getValor());
			novaOperacao.setValorAlertaSuperior(operacao.getValorAlertaSuperior());
			novaOperacao.setValorAlertaInferior(operacao.getValorAlertaInferior());
			novaLista.add(novaOperacao);
		}
		return novaLista;
	}
	
	public static double getValorDeParametro(List<OperacaoRealizadaTO> operacoes){
		double valorDeParametro = 0.0;
		for(OperacaoRealizadaTO operacao: operacoes){
			if(operacao.getPosicao().equals(OperacaoRealizadaTO.OPERACAO_POSICAO_NEUTRA)){
				valorDeParametro = operacao.getQuantidadeLotes() * operacao.getValorDouble() * 0.3;
				break;
			}
		}
		return valorDeParametro;
	}
	
	public static void alertaPorcentagemAlcancada(double saldoFinal, List<OperacaoRealizadaTO> operacoes){
		
		boolean enviarAlerta = false;
		double valorPorcentagemAlcancada = 0.0;
		double valorDeParametro = getValorDeParametro(operacoes);
		
		for(OperacaoRealizadaTO operacao: operacoes){
			if(saldoFinal >= operacao.getPorcentagemGanhoAlertaDouble() * valorDeParametro){
				enviarAlerta = true;
				valorPorcentagemAlcancada = operacao.getPorcentagemGanhoAlertaDouble();
				break;
			}
			if(saldoFinal <= operacao.getPorcentagemPerdaAlertaDouble() * valorDeParametro){
				enviarAlerta = true;
				valorPorcentagemAlcancada = operacao.getPorcentagemPerdaAlertaDouble();
				break;
			}
		}
		if(enviarAlerta){
			System.out.println("Vai enviar o e-mail de porcentagem alancada");
			SendMailUsingAuthentication.sendEmailOperacaoLhama("[SATA-Alerta] Porcentagem de Alerta " + valorPorcentagemAlcancada*100 + "% alcancada " + saldoFinal, operacoes);
		}
	}
	
	public static void alertaValorAlcancado(List<OperacaoRealizadaTO> operacoes){
		
		boolean enviarAlerta = false;
		double valorAlertaAlcancado = 0.0;
		for(OperacaoRealizadaTO operacao: operacoes){
			if(operacao.getValorDouble() >= operacao.getValorAlertaSuperiorDouble() || operacao.getValorDouble() <= operacao.getValorAlertaInferiorDouble()){
				enviarAlerta = true;
				valorAlertaAlcancado = operacao.getValorDouble();
				break;
			}
		}
		if(enviarAlerta){
			System.out.println("Vai enviar o e-mail de alertaValorAlcancado");
			SendMailUsingAuthentication.sendEmailOperacaoLhama("[SATA-Alerta] Valor de Alerta Alcancado " + valorAlertaAlcancado, operacoes);
		}
	}
	
	public static double getSaldo(List<OperacaoRealizadaTO> operacoes){
		double saldo = 0.0;
		double valorOperacao = 0.0;
		for(OperacaoRealizadaTO operacao: operacoes){
			if(operacao.getPosicao().equals(OperacaoRealizadaTO.OPERACAO_POSICAO_COMPRADA))
				valorOperacao = -1 * operacao.getQuantidadeLotes() * operacao.getValorDouble();
			if(operacao.getPosicao().equals(OperacaoRealizadaTO.OPERACAO_POSICAO_VENDIDA))
				valorOperacao = operacao.getQuantidadeLotes() * operacao.getValorDouble();
			saldo += valorOperacao;
			valorOperacao = 0.0;
		}
		return saldo;
	}
	
	public static List<OperacaoRealizadaTO> inverterOperacoes(List<OperacaoRealizadaTO> operacoes){
		for(OperacaoRealizadaTO operacao: operacoes){
			if(operacao.getPosicao().equals(OperacaoRealizadaTO.OPERACAO_POSICAO_COMPRADA)){
				operacao.setPosicao(OperacaoRealizadaTO.OPERACAO_POSICAO_VENDIDA);
			}else{
				if(operacao.getPosicao().equals(OperacaoRealizadaTO.OPERACAO_POSICAO_VENDIDA)){
					operacao.setPosicao(OperacaoRealizadaTO.OPERACAO_POSICAO_COMPRADA);
				}				
			}
		}
		
		return operacoes;
	}

	public static List<OperacaoRealizadaTO> atualizarOperacoes(List<OperacaoRealizadaTO> operacoes){
		for(OperacaoRealizadaTO operacao: operacoes){
			CotacaoLopesFilho.get(operacao.getCodigoAtivo());
			String valor = CotacaoLopesFilho.getCotacao();
			operacao.setValor(String.valueOf(Double.valueOf(valor.replace(",", "."))*100));
		}
		return operacoes;
	}
	
	public static void alertaLhama(){
		main(null);
	}
}
