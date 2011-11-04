package sata.domain.simulacao;

import java.util.List;

import sata.domain.dao.ICotacaoAtivoDAO;
import sata.domain.dao.SATAFactoryFacade;
import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;

public class SimulacaoReinvestimento implements ISimulacao{

	public ResultadoSimulacaoTO getResultado(
			List<CotacaoAtivoTO> listaDasCotacoes, Object[] parametros) {
		
		int tamanhoArray = listaDasCotacoes.size()/20 + 1; //Tamanho para guardar as operacoes
		double variacaoAcao[] = new double[tamanhoArray];
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
		double fechamentoCorrente = 0.0;
//		boolean compradoNaCall = false;
		
		int QTD_CALLS = 2;
		
		for(int i = 0; i <listaDasCotacoes.size(); i++)
		{
			CotacaoAtivoTO caTOAnterior = listaDasCotacoes.get(i); //pega a cotacao anterior
			fechamentoAnterior = Double.parseDouble(caTOAnterior.getFechamento());
			System.out.println(caTOAnterior.getCodigo() + ": " + i + " " + caTOAnterior.getPeriodo() + " - F: " + fechamentoAnterior);
			i = i + 20; //vai para daqui a 20 dias
			if (i > listaDasCotacoes.size()) 
				break; //se depois de 20 dias ultrapassar o tamanho da lista termina a simulacao
			
			CotacaoAtivoTO caTOCorrente = listaDasCotacoes.get(i); //pega a cotacao corrente
			fechamentoCorrente = Double.parseDouble(caTOCorrente.getFechamento());
			System.out.println(caTOCorrente.getCodigo() + ": " + i + " " + caTOCorrente.getPeriodo() + " - F: " + fechamentoCorrente);

			//calcula a variacao da acao
			variacaoAcao[indiceOperacao] = fechamentoCorrente - fechamentoAnterior; //pega a variacao da acao
			System.out.println("variacaoAcao[" + indiceOperacao + "]=" + variacaoAcao[indiceOperacao]);
			
			//calcula ganhoVEVendaCobertaMuitoITM
			//se a acao nao cair ate o preco de exercicio da ITM voce ganha 1% de VE
			double ganhoVE = 0.01 * fechamentoAnterior;;
			ganhoVEVendaCobMuitoITM[indiceOperacao] = ganhoVE;
			System.out.println("ganhoVEVendaCobMuitoITM[" + indiceOperacao + "]=" + ganhoVEVendaCobMuitoITM[indiceOperacao]);
			totalGanhoVEVendaCobMuitoITM+=ganhoVE;

			//calcula o caixa total 
			//com acrescimo no caixa quando o lancamento coberto perde VI
			//e decrescimo no caixa quando a acao ganha valor pq para recomprar
			//a ITM vai precisar gastar o valor que subiu da acao
			entradaVInoCaixa[indiceOperacao] = variacaoAcao[indiceOperacao] * (-1);
			totalCaixa+=entradaVInoCaixa[indiceOperacao];
			System.out.println("entradaVInoCaixa[" + indiceOperacao + "]=" + entradaVInoCaixa[indiceOperacao]);
			
			//calcula o ganho na 2 ATM de call caso a acao tenha subido
//			if(variacaoAcao[indiceOperacao] > 0 && compradoNaCall)
			if(variacaoAcao[indiceOperacao] > 0)
			{
				ganhoCall[indiceOperacao] = QTD_CALLS * variacaoAcao[indiceOperacao];
				totalGanhoCall+=ganhoCall[indiceOperacao];
			}
			System.out.println("ganhoCall[" + indiceOperacao + "]=" + ganhoCall[indiceOperacao]);
			
			//calcula para ver se faz a operacao da compra de 2 CALL ATM
			//caso se tenha dinheiro em caixa (toal do caixa mais o ganho com as calls ATM)
			//uma call ATM vale aproximado 3,5% da acao
			double gastoCallCorrente = 0.035 * fechamentoCorrente;
			valorCall[indiceOperacao] = gastoCallCorrente;
			double gastoCompra2ATM = QTD_CALLS * gastoCallCorrente;
//			if(totalCaixa + totalGanhoCall >= gastoCompra2ATM) //Entao compra de 2 CALL ATM
//			{
				gastoCall[indiceOperacao] = gastoCompra2ATM;
				totalCaixa-=gastoCompra2ATM; //retira o dinheiro das 2 CALLs ATM do caixa
				totalGastoCall+=gastoCompra2ATM;
//				compradoNaCall = true;
//			}
//			else{
//				compradoNaCall = false;
//			}
			System.out.println("valorCall[" + indiceOperacao + "]=" + valorCall[indiceOperacao]);
			System.out.println("gastoCall[" + indiceOperacao + "]=" + gastoCall[indiceOperacao]);
			
			System.out.println("totalCaixa[" + indiceOperacao + "]=" + totalCaixa);
			
			System.out.println(" ");
			
			indiceOperacao++; //parte para a proxima operacao
			i--; //para quando incrementar o i no for acertar

		}
		
		//imprime os valores encontrados
//		imprimeVetor("variacaoAcao", variacaoAcao);
//		imprimeVetor("entradaVI", entradaVI);
//		imprimeVetor("ganhoVEVendaCobMuitoITM", ganhoVEVendaCobMuitoITM);
//		imprimeVetor("valorCall", valorCall);
//		imprimeVetor("gastoCall", gastoCall);
//		imprimeVetor("ganhoCall", ganhoCall);
//		imprimeVetor("entradaCaixa", entradaCaixa);
//		System.out.println("");
//		System.out.println("totalGastoCall=" + totalGastoCall);
//		System.out.println("totalGanhoVEVendaCobMuitoITM=" + totalGanhoVEVendaCobMuitoITM);
//		System.out.println("totalGanhoCall=" + totalGanhoCall);
//		System.out.println("totalCaixa=" + totalCaixa);
		double primeiraCotacaoAcao = Double.parseDouble(listaDasCotacoes.get(0).getFechamento());
		System.out.println("primeiraCotacaoAcao=" + primeiraCotacaoAcao);
		System.out.println("ultimaCotacaoAcaoNaSimulacao=" + fechamentoCorrente);
		
		//calcula o valor total ganho
		double totalFinal = fechamentoCorrente + totalCaixa + totalGanhoCall + totalGanhoVEVendaCobMuitoITM;
		System.out.println("totalFinal=" + totalFinal);
		
		//calcula a porcentagem em relacao ao comeco do investimetno
		
		double pctGanhoFinal = (totalFinal-primeiraCotacaoAcao)*100/primeiraCotacaoAcao;
		System.out.println("pctGanhoFinal=" + pctGanhoFinal);
		
		// TODO Auto-generated method stub
		return null;
	}

	private void imprimeVetor(String nomeVetor, double obj[]){
		
		double total=0;
		for(int i = 0; i < obj.length; i++)
		{
			System.out.println(nomeVetor + "[" + i + "]=" + obj[i]);
			total = total + obj[i];
		}
		System.out.println("TOTAL " + nomeVetor + "=" + total);
	}
	

	@Override
	public void setQtdTotalOperacoesRiscoStop(int qtdTotalOperacoesRiscoStop) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
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
		for(int i=2000; i < 2012; i++){
			System.out.println("ANO " + String.valueOf(i));
			List<CotacaoAtivoTO> listaDasCotacoes2009 = caDAO.getCotacoesDoAtivo("VALE5", String.valueOf(i));
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
