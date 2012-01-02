package sata.auto.estrategia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.simulacao.Resultado;
import sata.auto.simulacao.Simulacao;

public abstract class Estrategia {
	
	Integer anoInicial;
	Integer anoFinal;
	
	static List<Simulacao> simulacoes = new ArrayList<Simulacao>();
	
	public void executa(boolean anual, boolean mensal, boolean operacoes, boolean calculos, boolean csv) {
		Resultado resultado = new Resultado();
		System.out.println("Início: " + new Date());
		prepara();
		
		for (Simulacao simulacao: simulacoes) {
			if (simulacao.getAnoInicial() == null && anoInicial != null)
				simulacao.setAnoInicial(anoInicial);
			if (simulacao.getAnoFinal() == null && anoFinal != null)
				simulacao.setAnoFinal(anoFinal);
			resultado.addResultado(simulacao.getResultado());
		}
		
		try {
			System.out.println(resultado.imprime(anual, mensal, operacoes, calculos, csv));
		} catch (CotacaoInexistenteEX e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("\nFim: " + new Date());
	}
	
	public abstract void prepara();

}
