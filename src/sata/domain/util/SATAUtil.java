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
		String dataInicioLista = getDataToYahooFinances(dataFinal); // Ex: "10 de mai de 2011"
		String dataFimDaLista = getDataToYahooFinances(dataInicial); // Ex: "2 de mai de 2011"
		System.out.println(dataInicioLista);
		System.out.println(dataFimDaLista);
		
		String pedacoHtml = html.substring(html.indexOf(dataInicioLista));
		System.out.println(pedacoHtml);
		String tagTD = "<td class=\"yfnc_tabledata1\" align=\"right\">";
		String fimTD = "</td>";
		
		// 0 - Abertura   1 - Alta   2 - Baixa   4 - Fechar
		String valores[] = new String[4];
		int i = 0;
		//para todos os dias que faltam
		CotacaoAtivoTO caTO = new CotacaoAtivoTO();
		while (i < 4) {
			pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(tagTD));
			// System.out.println(pedacoHtml);
			int posicaoFimTD = pedacoHtml.indexOf(fimTD);
			// System.out.println(posicaoFimTD);
			// System.out.println(tagTD.length());
			valores[i] = pedacoHtml.substring(tagTD.length(), posicaoFimTD);
//			System.out.println(valores[i]);
			pedacoHtml = pedacoHtml.substring(pedacoHtml.indexOf(posicaoFimTD));
			i++;
		}
		System.out.println("Teste");
		caTO.setCodigo(acao);
		caTO.setAbertura(valores[0].replace(",", ""));
		caTO.setMaxima(valores[1].replace(",", ""));
		caTO.setMinima(valores[2].replace(",", ""));
		caTO.setFechamento(valores[3].replace(",", ""));
		listaCotacoesAtivo.add(caTO);

		return listaCotacoesAtivo;
	}
	public static String getDataToYahooFinances(String data){
		
		String meses[] = {"jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dez"};
		String valores[] = data.split("/");
		int dia = Integer.parseInt(valores[0]);
		int indiceMes = Integer.parseInt(valores[1]) - 1;
		int ano = Integer.parseInt(valores[2]);
		return String.valueOf(dia) + " de " + String.valueOf(meses[indiceMes]) + " de " + String.valueOf(ano);
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
}
