package sata.auto.estrategia;

import static sata.auto.operacao.Operacao.ABERTURA;
import static sata.auto.operacao.Operacao.FECHAMENTO;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;

public class PutATM_CallOTM extends Estrategia {
	
	@Override
	public void executa() {
		Acao acao = new Acao("PETR4");
		Put putATM = new Put(acao, 0);
		Call callOTM = new Call(acao, 4);
		
		simulacao.getOperacoes().add(new Compra(acao, 1, ABERTURA));
		simulacao.getOperacoes().add(new Compra(putATM, 1, ABERTURA));
		simulacao.getOperacoes().add(new Venda(callOTM, 2, ABERTURA));
		
		simulacao.getOperacoes().add(new Venda(acao, 0, FECHAMENTO));
		simulacao.getOperacoes().add(new Venda(putATM, 0, FECHAMENTO));
		simulacao.getOperacoes().add(new Compra(callOTM, 1, FECHAMENTO));
		
		simulacao.setAnoInicial(2000);
		simulacao.setAnoFinal(2011);
	}
	
	public static void main(String[] args) {
		executa(new PutATM_CallOTM());
	}
}
