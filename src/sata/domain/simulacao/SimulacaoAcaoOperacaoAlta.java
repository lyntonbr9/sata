package sata.domain.simulacao;

import java.util.List;

import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;

public class SimulacaoAcaoOperacaoAlta implements ISimulacao{
	
	private int stopGain;
	private int stopLoss;
	private double probabilidadeStopGain;
	private double probabilidadeStopLoss;
	
	private List<CotacaoAtivoTO> listaCotacoes;
	//TODO Continuar esta joça
	public ResultadoSimulacaoTO getResultado(List<CotacaoAtivoTO> listaDasCotacoes){
		
		this.listaCotacoes = listaDasCotacoes;
		ResultadoSimulacaoTO resultado = new ResultadoSimulacaoTO();
		
		boolean fazerOperacao = false;
		int aberturaDiaAnterior;
		int fechamentoDiaAnterior;
		int aberturaDoisDiasAntes;
		int fechamentoDoisDiasAntes;
		int maxima;
		int minima;
		int fechamento;
		
		for (int i=2; i < listaCotacoes.size(); i++  ){
			aberturaDiaAnterior = Integer.parseInt(listaCotacoes.get(i-1).getAbertura());
			fechamentoDiaAnterior = Integer.parseInt(listaCotacoes.get(i-1).getFechamento());
			aberturaDoisDiasAntes = Integer.parseInt(listaCotacoes.get(i-2).getAbertura());
			fechamentoDoisDiasAntes = Integer.parseInt(listaCotacoes.get(i-2).getFechamento());
			maxima = Integer.parseInt(listaCotacoes.get(i).getMaxima());
			minima = Integer.parseInt(listaCotacoes.get(i).getMinima());
			
			if (fechamentoDiaAnterior > aberturaDiaAnterior 
					&& fechamentoDoisDiasAntes > aberturaDoisDiasAntes)
				fazerOperacao = true;
			
			if (fazerOperacao){
			
				if(((fechamentoDiaAnterior + stopGain) <= maxima) 
					&& ((fechamentoDiaAnterior + stopGain) >= minima)
					&& ((fechamentoDiaAnterior - stopLoss) <= maxima)
					&& ((fechamentoDiaAnterior + stopLoss) >= minima)){
					
				}
					
					
				
			}			
		}
		
		
		
		return resultado;
	}

	
}
