package sata.domain.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import sata.domain.to.CotacaoAtivoTO;
import sata.metastock.robos.CotacaoLopesFilho;

public class SATAUtil implements IConstants{
	
	
	public static Timestamp getTimeStampPeriodoCotacao(String periodo){
		Calendar cal = new GregorianCalendar();
		cal.set(Integer.valueOf(periodo.substring(0,4)), Integer.valueOf(periodo.substring(4,6)) - 1, Integer.valueOf(periodo.substring(6,8)),0,0,0);
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		return ts;
	}
	
	public static String getTimeStampFormatado(Timestamp ts, boolean comHora){
		Calendar cal = new GregorianCalendar();
		cal.setTime(ts);
		String tempoFormatado = "";
		if (comHora == false)
			tempoFormatado = cal.get(Calendar.DAY_OF_MONTH)+ "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
		else
			tempoFormatado = cal.get(Calendar.DAY_OF_MONTH)+ "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR)
				+ " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
		return tempoFormatado;
	}
	
	public static int[] getCandles(List<CotacaoAtivoTO> listaDasCotacoes){
		
		int Candles[] = new int[listaDasCotacoes.size()];

		int abertura;
		int fechamento;

		//seta as candles
		for (int i=0; i < listaDasCotacoes.size() ; i++  )
		{
			abertura = Integer.parseInt(listaDasCotacoes.get(i).getAbertura());
			fechamento = Integer.parseInt(listaDasCotacoes.get(i).getFechamento());
			Candles[i] = (fechamento > abertura) ? CANDLE_VERDE : CANDLE_VERMELHA; 
		}
		
		return Candles;
	}
	
	public static int getValorGanho(CotacaoAtivoTO caTO, int posicaoListaCotacoes, int fechamentoDiaAnterior, int stopGain, int stopLoss){
		int abertura = Integer.parseInt(caTO.getAbertura());
		int maxima = Integer.parseInt(caTO.getMaxima());
		int minima = Integer.parseInt(caTO.getMinima());
		int fechamento = Integer.parseInt(caTO.getFechamento());
		int valorGanho;
		
		if ((fechamentoDiaAnterior + stopGain) <= maxima)
		{
			//ver qtd de operacoes de risco
			if( ((fechamentoDiaAnterior - stopLoss) >= minima) ){
				System.out.println("Fez a operacao dia RISCO: " + caTO.getPeriodo() + " posicaoListaCotacoes = " + posicaoListaCotacoes);
			}
//			else
//			{
				if (abertura < (fechamentoDiaAnterior + stopGain))
					valorGanho = stopGain;
				else
					valorGanho = (abertura - fechamentoDiaAnterior);
				
				System.out.println("Fez a operacao dia SUCESSO: " + valorGanho  + " " + caTO.getPeriodo() 
						+ " posicaoListaCotacoes = " + posicaoListaCotacoes + " A: " + abertura + " Max: " + maxima
						+ " Min: " + minima + " F: " + fechamento);
			
//			}
		}
		else{
			//valorPerda = valorPerda + (fechamento - abertura);
			valorGanho = (stopLoss * -1);
			System.out.println("Fez a operacao dia FRACASSO: " + valorGanho  + " " + caTO.getPeriodo() 
					+ " posicaoListaCotacoes = " + posicaoListaCotacoes + " A: " + abertura + " Max: " + maxima
					+ " Min: " + minima + " F: " + fechamento);
		}
		return valorGanho;
	}

	//Formata dd/MM/yyyy para yyyyMMdd
	public static String getDataSemFormato(String data)
	{
		
		String[] vetorData = data.split("/"); //a data é separada [0] = dia, [1] = mes, [2] = ano
		
		if(Integer.parseInt(vetorData[1]) < 10) //formatando o mes de 1...9 para 01...09
			vetorData[1] = "0" + vetorData[1]; 
		
		if(Integer.parseInt(vetorData[0]) < 10) //formatando o dia de 1...9 para 01...09
			vetorData[0] = "0" + vetorData[0]; 
		
		System.out.println("Data formatada: " + vetorData[2] + vetorData[1] + vetorData[0]);
		return vetorData[2] + vetorData[1] + vetorData[0];
		
	}
	
	public static Calendar getDataAtual(){
		Date dataAtual = new Date();
		GregorianCalendar calendario = new GregorianCalendar();
		calendario.setTime(dataAtual);
		return calendario;		
	}

	// pega as cotacoes do site
	// (http://br.finance.yahoo.com/q/hp?s=PETR4.SA) no intervalo
	public static List<CotacaoAtivoTO> getCotacoesFromYahooFinances(String acao, String dataInicial, String dataFinal)
	{
		List<CotacaoAtivoTO> listaCotacoesAtivo = new ArrayList<CotacaoAtivoTO>();
		Hashtable<String, String> h = getYahooFinancesURLParameters(acao, dataInicial, dataFinal);
		
		String html = CotacaoLopesFilho.POST("http://br.finance.yahoo.com/q/hp", h);
//		System.out.println(html);
//		String dataInicioLista = getDataToYahooFinances(dataFinal); // Ex: "10 de mai de 2011"
		String dataFimDaLista = getDataToYahooFinances(dataInicial); // Ex: "2 de mai de 2011"
//		System.out.println(dataInicioLista);
//		System.out.println(dataFimDaLista);
		
//		String pedacoHtml = html.substring(html.indexOf(dataInicioLista));
//		System.out.println(pedacoHtml);
		String tagTDInicialDia = "<td class=\"yfnc_tabledata1\" nowrap align=\"right\">";
		if(html.indexOf(tagTDInicialDia) == -1){
			System.out.println("nao encontrou");
			return listaCotacoesAtivo;
		}
			
		String pedacoHtml = html.substring(html.indexOf(tagTDInicialDia));
//		System.out.println(pedacoHtml);
		String tagTD = "<td class=\"yfnc_tabledata1\" align=\"right\">";
		String fimTD = "</td>";
		
		//Ordem de leitura dos valores:
		// 0 - Dia  1 - Ano  2 - Abertura  3 - Alta   4 - Baixa  5 - Fechar
		String valores[] = new String[6];
		
		int maximoVezesBusca = 300;
		int contador = 0;
		boolean buscar = true;
		int posicaoFimTD = 0;
		
		int i;
		//para todos os dias que faltam
		while(buscar)
		{
			//evita que fique em loop infinito
			contador++;
			if (contador == maximoVezesBusca)
				break;
			
			//Le os valores do HTML
			pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(tagTDInicialDia));
			posicaoFimTD = pedacoHtml.indexOf(fimTD);
			//Pega a data
			valores[0] = pedacoHtml.substring(tagTDInicialDia.length(), posicaoFimTD);
			
			//se vier os dividendos na tabela pula pra proxima linha dela
			if(valores[0].contains("/")){
				pedacoHtml = pedacoHtml.substring(posicaoFimTD);
				continue;
			}
			
			//verifica se ja chegou ao fim para sair do loop, senao continua a extrair
			if(valores[0].equalsIgnoreCase(dataFimDaLista))
				break;
			
			//Formata para a data padrao dd/MM/yyyy
			valores[0] = formataDataFromYahooFinances(valores[0]);
			
			//Pega o ano
			valores[1] = valores[0].substring(valores[0].length() - 4);
			
			//Pega os valores da cotacao
			i = 2;
			while (i < 6) 
			{
				pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(tagTD));
//				System.out.println(pedacoHtml);
				posicaoFimTD = pedacoHtml.indexOf(fimTD);
//				System.out.println(posicaoFimTD);
//				System.out.println(tagTD.length());
				valores[i] = pedacoHtml.substring(tagTD.length(), posicaoFimTD);
//				System.out.println(valores[i]);
				pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(posicaoFimTD));
				i++;
			}
			CotacaoAtivoTO caTO = new CotacaoAtivoTO();
			caTO.setCodigo(acao);
			caTO.setPeriodo(valores[0]); //data corrente de insercao
			caTO.setAno(valores[1]);
			caTO.setTipoPeriodo("D");
			caTO.setAbertura(valores[2].replace(",", ""));
			caTO.setMaxima(valores[3].replace(",", ""));
			caTO.setMinima(valores[4].replace(",", ""));
			caTO.setFechamento(valores[5].replace(",", ""));
			listaCotacoesAtivo.add(caTO);
		}
		return listaCotacoesAtivo;
	}
	
	public static String getDataToYahooFinances(String data){
		
		String valores[] = data.split("/");
		int dia = Integer.parseInt(valores[0]);
		int indiceMes = Integer.parseInt(valores[1]) - 1;
		int ano = Integer.parseInt(valores[2]);
		return String.valueOf(dia) + " de " + String.valueOf(mesesYahooFinances[indiceMes]) + " de " + String.valueOf(ano);
	}
	
	public static Hashtable<String, String> getYahooFinancesURLParameters(String acao, 
				String dataInicial, String dataFinal){
		
		Hashtable<String, String> h = new Hashtable<String, String>();
		String valoresDataInicial[] = dataInicial.split("/");
		String valoresDataFinal[] = dataFinal.split("/");
		int indiceMesInicial = (Integer.parseInt(valoresDataInicial[1]) - 1);
		int indiceMesFinal = (Integer.parseInt(valoresDataFinal[1]) - 1);
		h.put("s", acao + ".SA");
		h.put("a", String.valueOf(indiceMesInicial) ); //indice mes inicial
		h.put("b", valoresDataInicial[0]); //dia inicial
		h.put("c", valoresDataInicial[2]); //ano inicial
		h.put("d", String.valueOf(indiceMesFinal)); //indice mes final
		h.put("e", valoresDataFinal[0]); //dia final
		h.put("f", valoresDataFinal[2]); //ano final
		h.put("g", "d"); //diario
		
		return h;
	}
	
	//A data do Yahoo Finances vem no formato Ex: 10 de mai de 2011
	public static String formataDataFromYahooFinances(String dataYahooFinances)
	{
		String valoresSplit[] = dataYahooFinances.split(" ");
		
		System.out.println("EI: " + dataYahooFinances);
		String dia = valoresSplit[0];
		if(Integer.parseInt(dia) < 10)
			dia = "0" + dia;
		
		int mes = getIndiceMesYahooFinances(valoresSplit[2]) + 1;
		String mesStr = String.valueOf(mes);
		if(mes < 10)
			mesStr = "0" + mesStr;
		
		String ano = valoresSplit[4];
		
		//return dia + "/" + mesStr + "/" + ano;
		return ano + mesStr + dia;
	}
	
	//retorna a posicao do mes no array de meses
	public static int getIndiceMesYahooFinances(String mesYahooFinanc)
	{
		for(int i=0; i < mesesYahooFinances.length; i++){
			if(mesYahooFinanc.equalsIgnoreCase(mesesYahooFinances[i])){
				return i;
			}
		}
		return -1; //Se nao encontrar
	}
	
	//formata a data para yyyyMMdd
	public static String formataDataFromInfoMoney(String dataInfoMoney)
	{
		String valoresSplit[] = dataInfoMoney.split("/");
		
		return valoresSplit[2] + valoresSplit[1] + valoresSplit[0];
	}
	
	//retorna data para o site InfoMoney
	public static String getDataToInfoMoney(String dataSemFormato){
		String valoresSplit[] = dataSemFormato.split("/");
		String dia;
		String mes;
		
		if (Integer.parseInt(valoresSplit[0]) < 10)
			dia = "0" + valoresSplit[0].toString();
		else
			dia = valoresSplit[0].toString();
		
		if (Integer.parseInt(valoresSplit[1]) < 10)
			mes = "0" + valoresSplit[1].toString();
		else
			mes = valoresSplit[1].toString();
		
		return dia + "/" + mes + "/" + valoresSplit[2];
	}
	
	// pega as cotacoes do site
	// (http://br.finance.yahoo.com/q/hp?s=PETR4.SA) no intervalo
	public static List<CotacaoAtivoTO> getCotacoesFromInfoMoney(String acao, String dataInicial, String htmlSource)
	{
		List<CotacaoAtivoTO> listaCotacoesAtivo = new ArrayList<CotacaoAtivoTO>();
		
		String html = htmlSource;
//		System.out.println(html);
//		String dataInicioLista = getDataToYahooFinances(dataFinal); // Ex: "10 de mai de 2011"
		String dataFimDaLista = dataInicial;
//		String dataFimDaLista = "09/05/2011";
//		System.out.println(dataInicioLista);
//		System.out.println(dataFimDaLista);
		
//		String pedacoHtml = html.substring(html.indexOf(dataInicioLista));
//		System.out.println(pedacoHtml);
		String tagTDInicialDia = "<td>";
		
		if(html.indexOf(dataFimDaLista) == -1){
			System.out.println("Nao encontrou data do ultimo cadastro do ativo");
			return listaCotacoesAtivo;
		}
			
		String pedacoHtml = html.substring(html.indexOf(tagTDInicialDia));
//		System.out.println(pedacoHtml);
		String tagTD = "<td class=\"numbers\">";
		String fimTD = "</td>";
		
		//Ordem de leitura dos valores:
		// 0 - Dia  1 - Ano  2 - Abertura  3 - Alta   4 - Baixa  5 - Fechar
		String valores[] = new String[8];
		
		int maximoVezesBusca = 300;
		int contador = 0;
		boolean buscar = true;
		int posicaoFimTD = 0;
		
		int i;
		//para todos os dias que faltam
		while(buscar)
		{
			//evita que fique em loop infinito
			contador++;
			if (contador == maximoVezesBusca)
				break;
			
			//Le os valores do HTML
			pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(tagTDInicialDia));
			posicaoFimTD = pedacoHtml.indexOf(fimTD);
			//Pega a data
			valores[0] = pedacoHtml.substring(tagTDInicialDia.length(), posicaoFimTD);
			
			//verifica se ja chegou ao fim para sair do loop, senao continua a extrair
			if(valores[0].equalsIgnoreCase(dataFimDaLista))
				break;
			
			//Pega o ano
			valores[1] = valores[0].substring(valores[0].length() - 4);
			
			
			//Pega os valores da cotacao
			i = 2;
			while (i < 8) 
			{
				pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(tagTD));
//				System.out.println(pedacoHtml);
				posicaoFimTD = pedacoHtml.indexOf(fimTD);
//				System.out.println(posicaoFimTD);
//				System.out.println(tagTD.length());
				valores[i] = pedacoHtml.substring(tagTD.length(), posicaoFimTD);
//				System.out.println(valores[i]);
				pedacoHtml = pedacoHtml.substring(posicaoFimTD);
				i++;
			}
			CotacaoAtivoTO caTO = new CotacaoAtivoTO();
			caTO.setCodigo(acao);
			//Formata para a data padrao yyyyMMdd
			valores[0] = formataDataFromInfoMoney(valores[0]);
			caTO.setPeriodo(valores[0]); //data corrente de insercao
			caTO.setAno(valores[1]);
			caTO.setTipoPeriodo("D");
			caTO.setAbertura(valores[4].replace(",", ""));
			caTO.setMaxima(valores[7].replace(",", ""));
			caTO.setMinima(valores[5].replace(",", ""));
			caTO.setFechamento(valores[3].replace(",", ""));
			listaCotacoesAtivo.add(caTO);
		}
		return listaCotacoesAtivo;
	}
	
	public static boolean indicePertence(ArrayList<Integer> listaIndices, int indice){
    	
    	for(int i=0;i < listaIndices.size();i++){
    		if(listaIndices.get(i).intValue()==indice){
    			return true;
    		}
    	}
    	
    	return false;
    }
}

