package sata.auto.simulacao;

import static sata.auto.operacao.Operacao.PRIMEIRO_DIA;
import static sata.auto.operacao.Operacao.ULTIMO_DIA;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;

public class DataMining {
	
	public static void main(String[] args) {
		Simulacao simulacao = new Simulacao();
		
		/*Acao acao = new Acao("PETR4");
		Call callITM = new Call(acao, -2);
		Call callOTM = new Call(acao, 2);
		Put putATM = new Put(acao, 0);
		
		simulacao.getOperacoes().add(new Compra(callITM, 1, PRIMEIRO_DIA));
		simulacao.getOperacoes().add(new Venda(callOTM, 1, PRIMEIRO_DIA));
		simulacao.getOperacoes().add(new Compra(putATM, 1, PRIMEIRO_DIA));
		
		simulacao.getOperacoes().add(new Venda(callITM, 0, ULTIMO_DIA));
		simulacao.getOperacoes().add(new Compra(callOTM, 0, ULTIMO_DIA));
		simulacao.getOperacoes().add(new Venda(putATM, 0, ULTIMO_DIA));*/
		
		Acao acao = new Acao("PETR4");
		Put putATM = new Put(acao, 0);
		Call callOTM = new Call(acao, 4);
		
		simulacao.getOperacoes().add(new Compra(acao, 1, PRIMEIRO_DIA));
		simulacao.getOperacoes().add(new Compra(putATM, 1, PRIMEIRO_DIA));
		simulacao.getOperacoes().add(new Venda(callOTM, 2, PRIMEIRO_DIA));
		
		simulacao.getOperacoes().add(new Venda(acao, 0, ULTIMO_DIA));
		simulacao.getOperacoes().add(new Venda(putATM, 0, ULTIMO_DIA));
		simulacao.getOperacoes().add(new Compra(callOTM, 1, ULTIMO_DIA));
		
		simulacao.setAnoInicial(2010);
		simulacao.setAnoFinal(2010);
		Resultado resultado = simulacao.getResultado();
		
		System.out.println(resultado);
		System.out.println(resultado.imprimeResultadosMensais());
		System.out.println(resultado.imprimeResultadosAnuais());

	}
}
