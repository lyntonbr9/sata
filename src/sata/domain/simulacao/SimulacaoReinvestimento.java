package sata.domain.simulacao;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;

public class SimulacaoReinvestimento implements ISimulacao{

	static Logger logger = Logger.getLogger(SimulacaoReinvestimento.class.getName());
	
	public ResultadoSimulacaoTO getResultado(
			List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros) {
		
		int QTD_DIAS_OPCAO = 20;
		int tamanhoArray = listaDasCotacoes.size()/QTD_DIAS_OPCAO + 1; //Tamanho para guardar as operacoes
		double variacaoAcao[] = new double[tamanhoArray];
		double variacaoATM[] = new double[tamanhoArray];
		double entradaVInoCaixa[] = new double[tamanhoArray];
		double ganhoVEVendaCobMuitoITM[] = new double[tamanhoArray];
		double totalGanhoVEVendaCobMuitoITM = 0.0;
		double valorCall[] = new double[tamanhoArray];
		double gastoCall[] = new double[tamanhoArray];
		double totalGastoCall = 0.0;
		double ganhoCall[] = new double[tamanhoArray];
		double totalGanhoCall = 0.0;
		double totalCaixa = 0.0;
		
		int indiceOperacao = 0;
		
		double fechamentoAnterior = 0.0;
		double fechamentoAcaoNoVencimentoDaOpcao = 0.0;
		double fechamentoAcaoNaATM = 0.0;
		
		int QTD_CALLS = 1;
		double PCTE_CALL_ITM = 0.01;
		double PCTE_CALL_ATM = 0.035;
		int DIA_ATM = 0;
		int j = DIA_ATM;
		for(int i = 0; i <listaDasCotacoes.size(); i++)
		{
			CotacaoAtivoTO caTOAnterior = listaDasCotacoes.get(i); //pega a cotacao anterior
			fechamentoAnterior = Double.parseDouble(caTOAnterior.getFechamento());
			logger.info(caTOAnterior.getCodigo() + ": " + i + " " + caTOAnterior.getPeriodo() + " - F: " + fechamentoAnterior);
			
			i = i + QTD_DIAS_OPCAO - 1; //vai para o vencimento da opcao (a opcao dura 20 dias uteis) diminui 1 pq a lista comeca no indice 0
			if (i >= listaDasCotacoes.size()) 
				break; //se depois no futuro ultrapassar o tamanho da lista termina a simulacao
			CotacaoAtivoTO caTOCorrente = listaDasCotacoes.get(i); //pega a cotacao corrente no vencimento da opcao
			fechamentoAcaoNoVencimentoDaOpcao = Double.parseDouble(caTOCorrente.getFechamento());
			logger.info(caTOCorrente.getCodigo() + ": " + i + " " + caTOCorrente.getPeriodo() + " - F: " + fechamentoAcaoNoVencimentoDaOpcao);

			//calcula a variacao da acao
			variacaoAcao[indiceOperacao] = fechamentoAcaoNoVencimentoDaOpcao - fechamentoAnterior; //pega a variacao da acao
			logger.info("variacaoAcao[" + indiceOperacao + "]=" + variacaoAcao[indiceOperacao]);
			
			//calcula ganhoVEVendaCobertaMuitoITM
			//se a acao nao cair ate o preco de exercicio da ITM voce ganha 1% de VE
			double ganhoVEdaITM = PCTE_CALL_ITM * fechamentoAnterior;
			ganhoVEVendaCobMuitoITM[indiceOperacao] = ganhoVEdaITM;
			logger.info("ganhoVEVendaCobMuitoITM[" + indiceOperacao + "]=" + ganhoVEVendaCobMuitoITM[indiceOperacao]);
			totalGanhoVEVendaCobMuitoITM+=ganhoVEdaITM;

			//atualiza o caixa acrescendo quando o lancamento coberto perde VI
			//e decrescendo no caixa quando a acao ganha valor pq para recomprar
			//a ITM e zerar a posicao vai precisar gastar o valor que subiu da acao
			entradaVInoCaixa[indiceOperacao] = variacaoAcao[indiceOperacao] * (-1);
			totalCaixa+=entradaVInoCaixa[indiceOperacao];
			logger.info("entradaVInoCaixa[" + indiceOperacao + "]=" + entradaVInoCaixa[indiceOperacao]);
			
			//calcula o ganho ou perda da call ATM
			CotacaoAtivoTO caTONaATM = listaDasCotacoes.get(j); //pega a cotacao da acao na ATM quando for comprar
			fechamentoAcaoNaATM = Double.parseDouble(caTONaATM.getFechamento());
			logger.info(caTOAnterior.getCodigo() + " na ATM: " + j + " " + caTONaATM.getPeriodo() + " - F: " + fechamentoAcaoNaATM);
			variacaoATM[indiceOperacao] = fechamentoAcaoNoVencimentoDaOpcao - fechamentoAcaoNaATM;
			if(variacaoATM[indiceOperacao] > 0)
			{
				ganhoCall[indiceOperacao] = QTD_CALLS * variacaoATM[indiceOperacao];
				totalGanhoCall+=ganhoCall[indiceOperacao];
			}
			logger.info("ganhoCall[" + indiceOperacao + "]=" + ganhoCall[indiceOperacao]);
			
			//calcula o gasto na call ATM
			double gastoCallATM = PCTE_CALL_ATM * fechamentoAcaoNaATM;
			valorCall[indiceOperacao] = gastoCallATM;
			double gastoCompraATM = QTD_CALLS * gastoCallATM;
			gastoCall[indiceOperacao] = gastoCompraATM;
			totalCaixa-=gastoCompraATM; //retira o dinheiro da call ATM do caixa
			totalGastoCall+=gastoCompraATM;
			j = j + QTD_DIAS_OPCAO - 1; //vai para a proxima ATM
				
			logger.info("valorCall[" + indiceOperacao + "]=" + valorCall[indiceOperacao]);
			logger.info("gastoCall[" + indiceOperacao + "]=" + gastoCall[indiceOperacao]);
			
			logger.info("totalCaixa[" + indiceOperacao + "]=" + totalCaixa);
			
			logger.info(" ");
			
			indiceOperacao++; //parte para a proxima operacao
			i--; //para quando o i incrementar no laco FOR ele se acertar

		}
		
		//imprime os valores encontrados
//		imprimeVetor("variacaoAcao", variacaoAcao);
//		imprimeVetor("entradaVI", entradaVI);
//		imprimeVetor("ganhoVEVendaCobMuitoITM", ganhoVEVendaCobMuitoITM);
//		imprimeVetor("valorCall", valorCall);
//		imprimeVetor("gastoCall", gastoCall);
//		imprimeVetor("ganhoCall", ganhoCall);
//		imprimeVetor("entradaCaixa", entradaCaixa);
//		logger.info("");
//		logger.info("totalGastoCall=" + totalGastoCall);
//		logger.info("totalGanhoVEVendaCobMuitoITM=" + totalGanhoVEVendaCobMuitoITM);
//		logger.info("totalGanhoCall=" + totalGanhoCall);
//		logger.info("totalCaixa=" + totalCaixa);
		double primeiraCotacaoAcao = Double.parseDouble(listaDasCotacoes.get(0).getFechamento());
		logger.info("GANHOS COM A ACAO APENAS:");
		logger.info("primeiraCotacaoAcao=" + primeiraCotacaoAcao);
		logger.info("ultimaCotacaoAcao=" + fechamentoAcaoNoVencimentoDaOpcao);
		double totalGanhoAcao = fechamentoAcaoNoVencimentoDaOpcao - primeiraCotacaoAcao;
		logger.info("totalGanhoAcao=" + totalGanhoAcao);
		double pctGanhoAcao=totalGanhoAcao*100/primeiraCotacaoAcao;
		logger.info("pctGanhoAcao=" + pctGanhoAcao);
		
		//calcula o valor total ganho
		logger.info("GANHOS COM A SIMULACAO DE REINVESTIMENTO:");
		logger.info("ultimaCotacaoAcao=" + fechamentoAcaoNoVencimentoDaOpcao);
		logger.info("totalCaixa=" + totalCaixa);
		logger.info("totalGanhoCall=" + totalGanhoCall);
		logger.info("totalGanhoVEVendaCobMuitoITM=" + totalGanhoVEVendaCobMuitoITM);
		double totalFinalGanhoSimulacao = fechamentoAcaoNoVencimentoDaOpcao + totalCaixa + totalGanhoCall + totalGanhoVEVendaCobMuitoITM;
		logger.info("totalFinalGanhoSimulacao=ultimaCotacaoAcao + totalCaixa + totalGanhoCall + totalGanhoVEVendaCobMuitoITM=" + totalFinalGanhoSimulacao);
		
		//calcula a porcentagem em relacao ao comeco do investimetno
		double pctGanhoFinalGanhoSimulacao = (totalFinalGanhoSimulacao-primeiraCotacaoAcao)*100/primeiraCotacaoAcao;
		logger.info("pctGanhoFinalGanhoSimulacao=" + pctGanhoFinalGanhoSimulacao);
		
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
		
		/* Simulacao 2007 e 2008 VALE 
		List<CotacaoAtivoTO> listaDasCotacoes = caDAO.getCotacoesDoAtivo("VALE5", "2007");
		List<CotacaoAtivoTO> listaDasCotacoes2008 = caDAO.getCotacoesDoAtivo("VALE5", "2008");
		List<CotacaoAtivoTO> novaLista = new ArrayList<CotacaoAtivoTO>(200);
		
		for(int i=0; i < 187; i++) //removendo as cotacoes anteriores a simulacao
			listaDasCotacoes.remove(0);
		
		novaLista.addAll(listaDasCotacoes);
		
//		for(int i=0; i < listaDasCotacoes2008.size(); i++){
		for(int i=0; i < 83; i++){
			CotacaoAtivoTO caTO = listaDasCotacoes2008.get(i);
			novaLista.add(caTO);
		}
		SimulacaoReinvestimento sr = new SimulacaoReinvestimento();
		sr.getResultado(novaLista, null);
		*/
		/* Simulacao 2009 */
		for(int i=1998; i < 2001; i++){
			logger.info("ANO " + String.valueOf(i));
//			List<CotacaoAtivoTO> listaDasCotacoes2009 = caDAO.getCotacoesDoAtivo("BVMF3", String.valueOf(i));
			List<CotacaoAtivoTO> listaDasCotacoes2009 = caDAO.getCotacoesDoAtivo("PETR4", String.valueOf(i));
			SimulacaoReinvestimento sr = new SimulacaoReinvestimento();
			sr.getResultado(listaDasCotacoes2009, null);			
		}
		
		/* Simulacao 2009
		List<CotacaoAtivoTO> listaDasCotacoes2009 = caDAO.getCotacoesDoAtivo("VALE5", "2009");
		SimulacaoReinvestimento sr = new SimulacaoReinvestimento();
		sr.getResultado(listaDasCotacoes2009, null);
		*/
	}

}
