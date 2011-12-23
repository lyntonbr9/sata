package sata.auto.estrategia;

import java.util.Date;

import sata.auto.exception.CotacaoInexistenteEX;
import sata.auto.simulacao.Resultado;
import sata.auto.simulacao.Simulacao;

public abstract class Estrategia {
	
	Simulacao simulacao = new Simulacao();
	
	public static void executa(Estrategia estrategia) {
		System.out.println("início: " + new Date());
		estrategia.executa();
		Resultado resultado = estrategia.simulacao.getResultado();
		System.out.println("fim do processamento: " + new Date());
		try {
			System.out.println(resultado.imprime(true, true, true, true));
		} catch (CotacaoInexistenteEX e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("fim: " + new Date());
	}
	
	public abstract void executa();

}
