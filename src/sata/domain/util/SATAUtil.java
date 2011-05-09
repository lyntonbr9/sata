package sata.domain.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import sata.domain.to.CotacaoAtivoTO;

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
}
