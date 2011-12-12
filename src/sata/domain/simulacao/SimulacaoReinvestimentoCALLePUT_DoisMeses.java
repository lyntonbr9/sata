package sata.domain.simulacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;
import sata.domain.util.IConstants;
import sata.metastock.util.BlackScholes;

//TODO TEM QUE RESOLVER ESSE
public class SimulacaoReinvestimentoCALLePUT_DoisMeses implements ISimulacao, IConstants{

	static Logger logger = Logger.getLogger(SimulacaoReinvestimentoCALLePUT_DoisMeses.class.getName());
	
	public ResultadoSimulacaoTO getResultado(
			List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros) {
		
		int QTD_DIAS_OPCAO = 20;
		int tamanhoArray = listaDasCotacoes.size()/QTD_DIAS_OPCAO + 1; //Tamanho para guardar as operacoes
		double variacaoAcao[] = new double[tamanhoArray];
		double variacaoATM[] = new double[tamanhoArray];
		double variacaoPUT[] = new double[tamanhoArray];
		double ganhoVEVendaCobMuitoITM[] = new double[tamanhoArray];
		double totalGanhoVEVendaCobITM = 0.0;
		
		double valorCALL_ATM[] = new double[tamanhoArray];
		double gastoCALL_ATM[] = new double[tamanhoArray];
		double ganhoCALL_ATM[] = new double[tamanhoArray];
		double totalGastoCALL_ATM = 0.0;
		double totalGanhoCALL_ATM = 0.0;
		
		double valorPUT_ATM[] = new double[tamanhoArray];		
		double gastoPUT_ATM[] = new double[tamanhoArray];
		double ganhoPUT_ATM[] = new double[tamanhoArray];
		double totalGastoPUT_ATM = 0.0;
		double totalGanhoPUT_ATM = 0.0;

		double totalCaixa = 0.0;
		int indiceOperacao = 0;
		
		double fechamentoAnterior = 0.0;
		double fechamentoCorrente = 0.0;
		double fechamentoAcaoNaCALL_ATM = 0.0;
		double fechamentoAcaoNaPUT_ATM = 0.0;
		
		//Calcula a volatilidade do ano anterior
		double volatilidade = (Double) parametros[0];
		System.out.println("VOLATILIDADE UTILIZADA: " + volatilidade);
		
		double qtdDiasFaltaUmMesVencEmAnos = BlackScholes.getQtdDiasEmAnos(QTD_DIAS_FALTA_1_MES_VENC);
//		double qtdDiasFaltaUmMesVencEmAnos = BlackScholes.getQtdDiasEmAnos(5);
		double qtdDiasFaltaDoisMesesVencEmAnos = BlackScholes.getQtdDiasEmAnos(QTD_DIAS_FALTA_2_MES_VENC);
//		double qtdDiasFaltaDoisMesesVencEmAnos = BlackScholes.getQtdDiasEmAnos(QTD_DIAS_FALTA_1_MES_VENC);
		
		int QTD_CALLS = 1;
		int QTD_LOTES = 1;
		int j = 0; //indice para as ATMs (CALL e PUT)
		for(int i = 0; i <listaDasCotacoes.size(); i++)
		{
			if (i + QTD_DIAS_OPCAO -1 >= listaDasCotacoes.size()) 
				break; //se depois no futuro ultrapassar o tamanho da lista termina a simulacao
			
			CotacaoAtivoTO caTOAnterior = listaDasCotacoes.get(i); //pega a cotacao anterior
			fechamentoAnterior = Double.parseDouble(caTOAnterior.getFechamento());
			logger.info(caTOAnterior.getCodigo() + ": " + i + " " + caTOAnterior.getPeriodo() + " - F: " + fechamentoAnterior);
			i = i + QTD_DIAS_OPCAO -1; //vai para o vencimento da opcao (a opcao dura 20 dias uteis) diminui 1 pq a lista comeca com indice 0
			
			CotacaoAtivoTO caTOCorrente = listaDasCotacoes.get(i); //pega a cotacao corrente no vencimento da opcao
			fechamentoCorrente = Double.parseDouble(caTOCorrente.getFechamento());
			logger.info(caTOCorrente.getCodigo() + ": " + i + " " + caTOCorrente.getPeriodo() + " - F: " + fechamentoCorrente);

			//calcula a variacao da acao
			logger.info("================= Variacao da Acao =================");
			variacaoAcao[indiceOperacao] = fechamentoCorrente - fechamentoAnterior; //pega a variacao da acao
			logger.info("variacaoAcao[" + indiceOperacao + "]=" + variacaoAcao[indiceOperacao]);
			if(variacaoAcao[indiceOperacao] > 0)
			{
				totalCaixa+=variacaoAcao[indiceOperacao];
			}
			
			//calcula ganhoVEVendaCobertaMuitoITM
			//se a acao nao cair ate o preco de exercicio da ITM voce ganha 1% de VE
//			double ganhoVECobertaITM = PCTE_2_CALL_ITM * fechamentoAnterior * QTD_LOTES;
			
//			logger.info("================= Opcao Coberta 2 ITM =================");
//			logger.info(caTOAnterior.getCodigo() + " na ITM: " + caTOAnterior.getPeriodo() + " - F: " + fechamentoAnterior);
//			double precoExercOpcaoCob2ITM = BlackScholes.getPrecoExercicio(false, fechamentoAnterior, 2); // Pega a 2 ITM para baixo
//			double precoExercOpcaoCob1ITM = BlackScholes.getPrecoExercicio(false, fechamentoAnterior, 1); // Pega a 1 ITM para baixo
//			logger.info("precoExercOpcaoCob2ITM: " + precoExercOpcaoCob2ITM);
//			double valorOpcaoCoberta2ITM = BlackScholes.blackScholes(true, fechamentoAnterior, precoExercOpcaoCob2ITM, qtdDiasFaltaUmMesVencEmAnos, TAXA_DE_JUROS, volatilidade);
//			logger.info("valorOpcaoCoberta2ITM: " + valorOpcaoCoberta2ITM);
//			double ganhoVEVendaCobertaITM = BlackScholes.getVE(true, fechamentoAnterior, precoExercOpcaoCob2ITM, valorOpcaoCoberta2ITM) * QTD_LOTES;
//			logger.info("ganhoVEVendaCobertaITM: " + ganhoVEVendaCobertaITM);
//			ganhoVEVendaCobMuitoITM[indiceOperacao] = ganhoVEVendaCobertaITM;
//			logger.info("ganhoVEVendaCobMuitoITM[" + indiceOperacao + "]=" + ganhoVEVendaCobMuitoITM[indiceOperacao]);
//			totalGanhoVEVendaCobITM+=ganhoVEVendaCobertaITM;
//			totalCaixa+=ganhoVEVendaCobertaITM; //atualiza o caixa com acrescimo quando o lancamento coberto perde VE
			
			logger.info("================= Opcao CALL ATM =================");
			//calcula o ganho na call ATM caso a acao tenha subido
			CotacaoAtivoTO caTONaATM = listaDasCotacoes.get(j); //pega a cotacao da acao na ATM quando for comprar
			fechamentoAcaoNaCALL_ATM = Double.parseDouble(caTONaATM.getFechamento());
//			logger.info(caTONaATM.getCodigo() + " na ATM: " + j + " " + caTONaATM.getPeriodo() + " - F: " + fechamentoAcaoNaCALL_ATM);
//			variacaoATM[indiceOperacao] = fechamentoCorrente - fechamentoAcaoNaCALL_ATM;
//			if(variacaoATM[indiceOperacao] > 0)
//			{
//				ganhoCALL_ATM[indiceOperacao] =  variacaoATM[indiceOperacao];
//				totalGanhoCALL_ATM+=ganhoCALL_ATM[indiceOperacao];
//				totalCaixa+=ganhoCALL_ATM[indiceOperacao];
//			}
//			logger.info("ganhoCALL_ATM[" + indiceOperacao + "]=" + ganhoCALL_ATM[indiceOperacao]);
			
			logger.info("=== CALL ATM Faltando 2 Meses ===");
			//calcula o gasto na call ATM
			double precoExercOpcaoCALL_ATM = BlackScholes.getPrecoExercicio(true, fechamentoAcaoNaCALL_ATM, 2); // Pega a CALL ATM
			logger.info("precoExercCALL_ATM: " + precoExercOpcaoCALL_ATM);
			
			double valorOpcaoDoisMesesCALL_ATM = BlackScholes.blackScholes(true, fechamentoAcaoNaCALL_ATM, precoExercOpcaoCALL_ATM, qtdDiasFaltaDoisMesesVencEmAnos, TAXA_DE_JUROS, volatilidade);
			logger.info("valorOpcaoDoisMesesCALL_ATM: " + valorOpcaoDoisMesesCALL_ATM);
			double ganhoCallDoisMesesNaATM = BlackScholes.getVE(true, fechamentoAcaoNaCALL_ATM, precoExercOpcaoCALL_ATM, valorOpcaoDoisMesesCALL_ATM); //perde o VE no vencimento
			logger.info("ganhoCallDoisMesesNaATM: " + ganhoCallDoisMesesNaATM);
			
			logger.info("=== CALL ATM Faltando 1 Mes ===");
			logger.info("fechamentoAcaoParaCalculoUmMesCALL_ATM: " + fechamentoCorrente);
			double valorOpcaoUmMesRestanteCALL_ATM = BlackScholes.blackScholes(true, fechamentoCorrente, precoExercOpcaoCALL_ATM, qtdDiasFaltaUmMesVencEmAnos, TAXA_DE_JUROS, volatilidade);
			logger.info("valorOpcaoUmMesRestanteCALL_ATM: " + valorOpcaoUmMesRestanteCALL_ATM);
			double custoCallUmMesRestanteNaATM = BlackScholes.getVE(true, fechamentoCorrente, precoExercOpcaoCALL_ATM, valorOpcaoUmMesRestanteCALL_ATM); //perde o VE no vencimento
			logger.info("custoCallUmMesRestanteNaATM: " + custoCallUmMesRestanteNaATM);
			
			logger.info("=== CALL ATM Resultado ===");
			double ganhoCallNaATM = ganhoCallDoisMesesNaATM - custoCallUmMesRestanteNaATM; //perde o VE no vencimento
			logger.info("ganhoCallNaATM: ganhoCallDoisMesesNaATM - custoCallUmMesRestanteNaATM=" + ganhoCallNaATM);
			logger.info("Porcentagem ganhoCallNaATM: " + (ganhoCallNaATM*100)/fechamentoAcaoNaCALL_ATM + "%");
			
//			double gastoCallNaATM = PCTE_1_CALL_ATM * fechamentoAcaoNaCALL_ATM;
//			valorCALL_ATM[indiceOperacao] = ganhoCallNaATM;
//			double gastoCompraATM = QTD_CALLS * ganhoCallNaATM;
//			gastoCALL_ATM[indiceOperacao] = gastoCompraATM;
			ganhoCALL_ATM[indiceOperacao] = ganhoCallNaATM;
			totalGanhoCALL_ATM+=ganhoCallNaATM;
			totalCaixa+=ganhoCallNaATM; 
			
			logger.info("================= Opcao PUT ATM =================");
//			//calcula o ganho na PUT ATM caso a acao tenha caido
			CotacaoAtivoTO caTOPUTNaATM = listaDasCotacoes.get(j); //pega a cotacao da acao na ATM quando for comprar
			fechamentoAcaoNaPUT_ATM = Double.parseDouble(caTOPUTNaATM.getFechamento());
//			logger.info(caTOPUTNaATM.getCodigo() + " na PUT ATM: " + j + " " + caTOPUTNaATM.getPeriodo() + " - F: " + fechamentoAcaoNaPUT_ATM);
//			variacaoPUT[indiceOperacao] = (fechamentoCorrente - fechamentoAcaoNaCALL_ATM) * (-1); //fica positivo caso cair
//			if(variacaoPUT[indiceOperacao] > 0)
//			{
//				ganhoPUT_ATM[indiceOperacao] = QTD_CALLS * variacaoPUT[indiceOperacao];
//				totalGanhoPUT_ATM+=ganhoPUT_ATM[indiceOperacao];
//				totalCaixa+=ganhoPUT_ATM[indiceOperacao];
//			}
//			logger.info("ganhoPUT_ATM[" + indiceOperacao + "]=" + ganhoPUT_ATM[indiceOperacao]);
//			
//			logger.info("=== PUT ATM Faltando 2 Meses ===");
//			//calcula o gasto na PUT ATM
			double precoExercOpcaoPUT_ATM = BlackScholes.getPrecoExercicio(false, fechamentoAcaoNaPUT_ATM, 0); // Pega a PUT ATM
			logger.info("precoExercOpcaoPUT_ATM: " + precoExercOpcaoPUT_ATM);
//			
//			double valorOpcaoDoisMesesPUT_ATM = BlackScholes.blackScholes(false, fechamentoAcaoNaPUT_ATM, precoExercOpcaoPUT_ATM, qtdDiasFaltaDoisMesesVencEmAnos, TAXA_DE_JUROS, volatilidade);
//			logger.info("valorOpcaoDoisMesesPUT_ATM: " + valorOpcaoDoisMesesPUT_ATM);
//			double gastoPUTDoisMesesNaATM = BlackScholes.getVE(false, fechamentoAcaoNaPUT_ATM, precoExercOpcaoPUT_ATM, valorOpcaoDoisMesesPUT_ATM); //perde o VE no vencimento
//			logger.info("gastoPUTDoisMesesNaATM: " + gastoPUTDoisMesesNaATM);
//			
			logger.info("=== PUT ATM Faltando 1 Mes ===");
			logger.info("fechamentoAcaoParaCalculoUmMesPUT_ATM: " + fechamentoAcaoNaPUT_ATM);
			double valorOpcaoUmMesRestantePUT_ATM = BlackScholes.blackScholes(false, fechamentoAcaoNaPUT_ATM, precoExercOpcaoPUT_ATM, qtdDiasFaltaUmMesVencEmAnos, TAXA_DE_JUROS, volatilidade);
			logger.info("valorOpcaoUmMesRestantePUT_ATM: " + valorOpcaoUmMesRestantePUT_ATM);
			double custoPUTUmMesRestanteNaATM = BlackScholes.getVE(false, fechamentoAcaoNaPUT_ATM, precoExercOpcaoPUT_ATM, valorOpcaoUmMesRestantePUT_ATM); //perde o VE no vencimento
			if(custoPUTUmMesRestanteNaATM < 0) custoPUTUmMesRestanteNaATM = 0;
			logger.info("custoPUTUmMesRestanteNaATM: " + custoPUTUmMesRestanteNaATM);
			

			logger.info("=== PUT ATM Resultado ===");
			double gastoPUTNaATM = custoPUTUmMesRestanteNaATM; //perde o VE no vencimento
			logger.info("gastoPUTNaATM: " + gastoPUTNaATM);
			logger.info("Porcentagem gastoPUTNaATM: " + (gastoPUTNaATM*100)/fechamentoAcaoNaPUT_ATM + "%");
			gastoPUT_ATM[indiceOperacao] = gastoPUTNaATM;
			totalGastoPUT_ATM+= gastoPUTNaATM;
			totalCaixa-=gastoPUTNaATM;
			
//			
//			logger.info("=== PUT ATM Resultado ===");
//			double gastoPUTNaATM = gastoPUTDoisMesesNaATM - custoPUTUmMesRestanteNaATM; //perde o VE no vencimento
//			logger.info("gastoPUTNaATM: gastoPUTDoisMesesNaATM - custoPUTUmMesRestanteNaATM =" + gastoPUTNaATM);
//			logger.info("Porcentagem gastoPUTNaATM: " + (gastoPUTNaATM*100)/fechamentoAcaoNaPUT_ATM + "%");
			
//			logger.info("=== PUT ATM Resultado ===");
//			double gastoPUTNaATM = gastoPUTUmMesesNaATM; //perde o VE no vencimento
//			logger.info("gastoPUTNaATM: gastoPUTUmMesesNaATM =" + gastoPUTNaATM);
//			logger.info("Porcentagem gastoPUTNaATM: " + (gastoPUTNaATM*100)/fechamentoAcaoNaPUT_ATM + "%");
			
//			double gastoPUTNaATM = PCTE_1_PUT_ATM * fechamentoAcaoNaPUT_ATM;
//			valorPUT_ATM[indiceOperacao] = gastoPUTNaATM;
//			double gastoCompraPUT_ATM = QTD_CALLS * gastoPUTNaATM;
//			gastoPUT_ATM[indiceOperacao] = gastoCompraPUT_ATM;
//			totalGastoPUT_ATM+=gastoCompraPUT_ATM;
//			totalCaixa-=gastoCompraPUT_ATM; //retira o dinheiro da PUT ATM do caixa
			
			logger.info("================= RESULTADO NO MES =================");
			logger.info("variacaoAcao[" + indiceOperacao + "]=" + variacaoAcao[indiceOperacao]);
			logger.info("ganhoVEVendaCobMuitoITM[" + indiceOperacao + "]=" + ganhoVEVendaCobMuitoITM[indiceOperacao]);
			logger.info("ganhoCALL_ATM[" + indiceOperacao + "]=" + ganhoCALL_ATM[indiceOperacao]);
			logger.info("gastoCALL_ATM[" + indiceOperacao + "]=" + gastoCALL_ATM[indiceOperacao]);
			logger.info("ganhoPUT_ATM[" + indiceOperacao + "]=" + ganhoPUT_ATM[indiceOperacao]);
			logger.info("gastoPUT_ATM[" + indiceOperacao + "]=" + gastoPUT_ATM[indiceOperacao]);
			
//			double resultadoDoMes = ganhoVEVendaCobMuitoITM[indiceOperacao] + ganhoCALL_ATM[indiceOperacao] + ganhoPUT_ATM[indiceOperacao] - gastoCALL_ATM[indiceOperacao] - gastoPUT_ATM[indiceOperacao];
			double ganhoAcao = (variacaoAcao[indiceOperacao] > 0 ? variacaoAcao[indiceOperacao] : 0.0);
			double resultadoDoMes = ganhoAcao + ganhoCALL_ATM[indiceOperacao] - gastoPUT_ATM[indiceOperacao];
			logger.info("resultadoDoMes:  ganhoAcao + ganhoCALL_ATM - gastoPUT_ATM");
			logger.info("resultadoDoMes: " + ganhoAcao + " + " + ganhoCALL_ATM[indiceOperacao] 
					 + " - " + gastoPUT_ATM[indiceOperacao] + " = " + resultadoDoMes);
			logger.info("totalCaixa[" + indiceOperacao + "]=" + totalCaixa);
			
			logger.info(" ");
			logger.info(" ");
			
			j = j + QTD_DIAS_OPCAO - 1; //vai para a proxima ATM
			indiceOperacao++; //parte para a proxima operacao
			i--; //deve-se subtrair 1 porque vai ser incrementado depois no LACO DO FOR

		}
		
		//imprime os valores encontrados
		logger.info("================= RESULTADO FINAL NO ANO =================");
		//calcula o valor total ganho NA ACAO
		double primeiraCotacaoAcao = Double.parseDouble(listaDasCotacoes.get(0).getFechamento());
		logger.info("GANHOS COM A ACAO APENAS:");
		logger.info("primeiraCotacaoAcao=" + primeiraCotacaoAcao);
		logger.info("ultimaCotacaoAcao=" + fechamentoCorrente);
		double totalGanhoAcao = (fechamentoCorrente - primeiraCotacaoAcao) * QTD_LOTES;
		logger.info("totalGanhoAcao=" + totalGanhoAcao);
		double pctGanhoAcao=totalGanhoAcao*100/(primeiraCotacaoAcao * QTD_LOTES);
		logger.info("pctGanhoAcao=" + pctGanhoAcao + "%");
		logger.info(" ");
		
		//calcula o valor total ganho NAS OPCOES
		logger.info("GANHOS COM A SIMULACAO DE REINVESTIMENTO:");
		logger.info("totalCaixa=" + totalCaixa);
		logger.info("totalGanhoVEVendaCobITM=" + totalGanhoVEVendaCobITM);
		logger.info("totalGanhoCALL_ATM=" + totalGanhoCALL_ATM);
		logger.info("totalGastoCALL_ATM=" + totalGastoCALL_ATM);
		logger.info("totalGanhoPUT_ATM=" + totalGanhoPUT_ATM);
		logger.info("totalGastoPUT_ATM=" + totalGastoPUT_ATM);
		
		double totalFinalGanhoSimulacao = primeiraCotacaoAcao + totalCaixa;
		logger.info("totalFinalGanhoSimulacao=primeiraCotacaoAcao + totalCaixa=" + totalFinalGanhoSimulacao);
		
		//calcula a porcentagem em relacao ao comeco do investimetno
		double pctGanhoFinalGanhoSimulacao = (totalFinalGanhoSimulacao-primeiraCotacaoAcao)*100/(primeiraCotacaoAcao*QTD_LOTES);
		logger.info("pctGanhoFinalGanhoSimulacao=" + pctGanhoFinalGanhoSimulacao + "%");
		
		logger.info(" ");
		
		// TODO Auto-generated method stub
		return null;
	}

	private void imprimeVetor(String nomeVetor, double obj[]){
		
		double total=0;
		for(int i = 0; i < obj.length; i++)
		{
			logger.info(nomeVetor + "[" + i + "]=" + obj[i]);
			total = total + obj[i];
		}
		logger.info("TOTAL " + nomeVetor + "=" + total);
	}
	

	@Override
	public void setQtdTotalOperacoesRiscoStop(int qtdTotalOperacoesRiscoStop) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
//		BasicConfigurator.configure();
		
		PropertyConfigurator.configure("log4j.properties");
		ICotacaoAtivoDAO caDAO = SATAFactoryFacade.getCotacaoAtivoDAO();
		
		String acao = "PETR4";
//		String acao = "OGXP3";
		for(int ano=2008; ano <= 2008; ano++)
		{
			logger.info("ANO " + String.valueOf(ano));
			double volatilidade = 0;
			List<CotacaoAtivoTO> listaDasCotacoesAnoPassado = caDAO.getCotacoesDoAtivo(acao, String.valueOf(ano));
			List<CotacaoAtivoTO> listaDasCotacoesAnoRetrasado = caDAO.getCotacoesDoAtivo(acao, String.valueOf(ano-1));
			if (acao.equalsIgnoreCase("PETR4") && (ano == 2009 || ano == 2008)) //SE FOR PETR4 em 2009
				volatilidade = 0.3;
			else
				volatilidade = BlackScholes.getVolatilidadeAnualAcao(listaDasCotacoesAnoPassado, listaDasCotacoesAnoRetrasado);
			
//			volatilidade = 0.49;
			List<CotacaoAtivoTO> listaDasCotacoes = caDAO.getCotacoesDoAtivo(acao, String.valueOf(ano));
			SimulacaoReinvestimentoCALLePUT_DoisMeses sr = new SimulacaoReinvestimentoCALLePUT_DoisMeses();
			sr.getResultado(listaDasCotacoes, new Object[] {volatilidade});			
		}

//		calculaGanho(30000, 2000, 72, 0.04);
		
	}
	
	public static void calculaGanho(double valorInicial, double quantiaPorUnidadeTempo, int tempo, double taxa){
		double caixa = valorInicial;
		double ganho = 0.0;
		for (int i=1; i <= tempo; i++){
			ganho=caixa * taxa;
			caixa=caixa + ganho + quantiaPorUnidadeTempo;
			System.out.println("caixa:" + caixa);
		}
			
		System.out.println("caixa:" + caixa);
	}

}
