package sata.metastock.simulacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.mail.MessagingException;

import sata.metastock.mail.Email;
import sata.metastock.robos.CotacaoLopesFilho;

public class SimulaGanhoOpcoes {

	static String[] opcoesPETR4 = { "petrh20", "petrh22", "petrh23", "petrh24",
			"petrh25", "petri20", "petri22", "petri23", "petri24", "petri25" };
	
	static double[] strikesPETR4 = { 19.83, 21.83, 22.66, 23.83, 24.66, 19.83,
		21.83, 22.83, 23.83, 24.83 };

/*	static double[] strikesPETR4 = { 20.00, 22.00, 22.83, 24.00, 24.83, 20.00,
			22.00, 23.00, 24.00, 25.00 }; */

	static String[] opcoesVALE5 = { "valeh42", "valeh44", "valeh45", "valeh46",
			"valeh47", "valei42", "valei44", "valei45", "valei46", "valei47" };
	static double[] strikesVALE5 = { 42.00, 44.00, 45.00, 46.00, 46.48, 41.48,
			44.00, 45.83, 46.00, 46.48 };

	static String[] emails = { "flaviogc@gmail.com",
			"flaviogc@br-petrobras.com.br", "tobebendo@gmail.com" };
	
	static int precisao = 3;
	
	private static String getResultadoSimulacao(String codigoAcao, String[] opcoesDaAcao, double[] strikesDasOpcoes){
		
		String resultado = "";
		String msgTemp="";
		CotacaoLopesFilho.get(codigoAcao);

		String cotacaoAcao=CotacaoLopesFilho.getCotacao();
		System.out.println("Cotação " + codigoAcao + ": " +  cotacaoAcao + "\n");
		resultado = "Cotação " + codigoAcao + ": " +  cotacaoAcao + "\n";
		BigDecimal precoCompraAcao = new BigDecimal(cotacaoAcao.replace(',', '.'));

		for (int i = 0; i < opcoesDaAcao.length; i++) {

			System.out.print(opcoesDaAcao[i]);
			resultado += opcoesDaAcao[i];

			CotacaoLopesFilho.get(opcoesDaAcao[i]);
			String cotacaoOpcao = CotacaoLopesFilho.getCotacao();
			System.out.println(": " + cotacaoOpcao);
			resultado += ": " + cotacaoOpcao + " ";

			BigDecimal precoOpcao = new BigDecimal(cotacaoOpcao.replace(',', '.'));

			BigDecimal VE = null;
			if (precoCompraAcao.doubleValue() <= strikesDasOpcoes[i]) {
				VE = precoOpcao;
			} else {
				VE = new BigDecimal(strikesDasOpcoes[i]).add(precoOpcao).subtract(precoCompraAcao);
			}
			System.out.println("VE: " + VE);
			resultado += "Strike: " + strikesDasOpcoes[i] + " VE: " + VE.setScale(precisao,BigDecimal.ROUND_HALF_EVEN) + " ";

			BigDecimal prcVE = VE.divide(precoCompraAcao,BigDecimal.ROUND_HALF_EVEN, precisao).multiply(new BigDecimal(100));
			msgTemp = "PRC_VE: " + prcVE.setScale(precisao,BigDecimal.ROUND_HALF_EVEN) + "% ";
			System.out.println(msgTemp);
			resultado += msgTemp;

			BigDecimal VI = null;
			if (precoCompraAcao.doubleValue() - strikesDasOpcoes[i] <0)
				VI = new BigDecimal(0);
			else
				VI = precoCompraAcao.subtract(new BigDecimal(strikesDasOpcoes[i]));

			System.out.println("VI: " + VI);
			resultado += "VI: " + VI.setScale(precisao,BigDecimal.ROUND_HALF_EVEN) + " ";

			//porcentagem do VI em relacao a acao
			BigDecimal prcVI = VI.divide(precoCompraAcao,BigDecimal.ROUND_HALF_EVEN, precisao).multiply(new BigDecimal(100));
			msgTemp = "PRC_VI: " + prcVI.setScale(precisao,BigDecimal.ROUND_HALF_EVEN) + "% ";
			System.out.println(msgTemp);
			resultado += msgTemp;
			
			//razao do VI com o VE
			String alerta=" ";
			BigDecimal razaoVI_VE = VI.divide(VE, BigDecimal.ROUND_HALF_EVEN, precisao);
			if (razaoVI_VE.doubleValue() >= 2.0 && razaoVI_VE.doubleValue() <= 3.0)
				alerta+= " <========";
			msgTemp = "VI/VE: " + razaoVI_VE.setScale(precisao,BigDecimal.ROUND_HALF_EVEN) + alerta + "\n";
			System.out.println(msgTemp);
			resultado += msgTemp;
		}
		return resultado;
	}

	public static void main(String[] args) throws InterruptedException, MessagingException {

		while (true) 
		{
			Date dt = new Date();
			System.out.println(dt.getHours());
//			Thread.currentThread().sleep(60000);

			// if(true){
			if (dt.getHours() >= 10 && dt.getHours() < 18) 
			{
				String mensagem = "";
				
				mensagem+= getResultadoSimulacao("petr4", opcoesPETR4, strikesPETR4);
				mensagem+= getResultadoSimulacao("vale5", opcoesVALE5, strikesVALE5);

				System.out.println(mensagem);
				
//				Email mail = new Email();
//				mail.sendSSLMessage(emails, "VE p/ VENDA COBERTA", mensagem,
//						"flaviogc@gmail.com");

				System.out.println("t1");
				Thread.currentThread().sleep(1800000);
				System.out.println("t2");
			}
		}
	}
}