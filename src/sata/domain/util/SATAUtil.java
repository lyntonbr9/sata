package sata.domain.util;

import java.sql.Timestamp;
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
//			if( ((fechamentoDiaAnterior - stopLoss) >= minima) ){
//				valorGanho = valorGanho - stopLoss;
//				qtdOperRiscoStop++;
//				System.out.println("Fez a operacao dia RISCO: " + (stopLoss * -1) + " " + listaDasCotacoes.get(i).getPeriodo() + " i = " + i);
//			}
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

}
