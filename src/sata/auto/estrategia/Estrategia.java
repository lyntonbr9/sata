package sata.auto.estrategia;

import sata.auto.simulacao.Resultado;
import sata.auto.simulacao.Simulacao;

public abstract class Estrategia {
	
	Simulacao simulacao = new Simulacao();
	
	public static void executa(Estrategia estrategia) {
		estrategia.executa();
		Resultado resultado = estrategia.simulacao.getResultado();
		System.out.println(resultado);
		System.out.println(resultado.imprimeResultadosMensais());
		System.out.println(resultado.imprimeResultadosAnuais());
	}
	
	public abstract void executa();

}
