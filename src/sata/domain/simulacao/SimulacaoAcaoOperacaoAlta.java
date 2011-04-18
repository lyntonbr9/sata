package sata.domain.simulacao;

import java.util.List;

import sata.domain.to.CotacaoAtivoTO;
import sata.domain.to.ResultadoSimulacaoTO;

public class SimulacaoAcaoOperacaoAlta implements ISimulacao{
	
	private int stopGain;
	private int stopLoss;
	private int qtdTotalOperacoesRiscoStop;
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
		int abertura;
		int qtdTotalOper=0;
		int qtdOperSucesso=0;
		int qtdOperFalha=0;
		int qtdOperRiscoStop=0;
		int valorGanho=0;
		int valorPerda=0;
		int valorTotal=0;
		int qtdOperacoesRiscoStop=0;
		
		for (int i=2; i < listaCotacoes.size(); i++  ){
			aberturaDiaAnterior = Integer.parseInt(listaCotacoes.get(i-1).getAbertura());
			fechamentoDiaAnterior = Integer.parseInt(listaCotacoes.get(i-1).getFechamento());
			aberturaDoisDiasAntes = Integer.parseInt(listaCotacoes.get(i-2).getAbertura());
			fechamentoDoisDiasAntes = Integer.parseInt(listaCotacoes.get(i-2).getFechamento());
			maxima = Integer.parseInt(listaCotacoes.get(i).getMaxima());
			minima = Integer.parseInt(listaCotacoes.get(i).getMinima());
			abertura = Integer.parseInt(listaCotacoes.get(i).getAbertura());
			
			if (fechamentoDiaAnterior > aberturaDiaAnterior 
					&& fechamentoDoisDiasAntes > aberturaDoisDiasAntes)
				fazerOperacao = true;
			
			if (fazerOperacao){
			
				if(((fechamentoDiaAnterior + stopGain) <= maxima) 
					&& ((fechamentoDiaAnterior + stopGain) >= minima)
					&& ((fechamentoDiaAnterior - stopLoss) <= maxima)
					&& ((fechamentoDiaAnterior + stopLoss) >= minima)){
					
					qtdOperRiscoStop++;
					if (qtdOperacoesRiscoStop < ((qtdTotalOperacoesRiscoStop*probabilidadeStopLoss) + 1))
						valorPerda = valorPerda + ((stopLoss)*-1);
					else
						valorGanho = valorGanho + stopGain;
				}
				else{
					if(((fechamentoDiaAnterior + stopGain) <= maxima) 
							&& ((fechamentoDiaAnterior + stopGain) >= minima)){
						qtdOperSucesso++;
						valorGanho = valorGanho + (abertura - fechamentoDiaAnterior);
					}
					else{
						qtdOperFalha++;
						if( ((abertura - fechamentoDiaAnterior)*-1) > stopLoss)
							valorPerda = valorPerda + (abertura - fechamentoDiaAnterior);
						else
							valorPerda = valorPerda + ((stopLoss)*-1); 
					}	
				}
			}			
		}
		qtdTotalOper = qtdOperRiscoStop + qtdOperSucesso + qtdOperFalha;
		valorTotal = valorGanho + valorPerda;
		
		resultado.setQtdOperacoesFalha(qtdOperFalha);
		resultado.setQtdOperacoesRiscoStop(qtdOperRiscoStop);
		resultado.setQtdOperacoesSucesso(qtdOperSucesso);
		resultado.setQtdTotalOperacoes(qtdTotalOper);
		resultado.setValorGanho(valorGanho);
		resultado.setValorPerda(valorPerda);
		resultado.setValorTotal(valorTotal);
		
		return resultado;
	}

	
}
