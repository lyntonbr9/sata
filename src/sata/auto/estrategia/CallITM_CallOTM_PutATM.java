package sata.auto.estrategia;

import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;

public class CallITM_CallOTM_PutATM extends Estrategia {

	@Override
	public void executa() {
		Acao acao = new Acao("PETR4");
		Call callITM = new Call(acao, -2);
		Call callOTM = new Call(acao, 2);
		Put putATM = new Put(acao, 0);
		
		simulacao.getOperacoes().add(new Compra(callITM));
		simulacao.getOperacoes().add(new Venda(callOTM));
		simulacao.getOperacoes().add(new Compra(putATM));
		
		simulacao.setAnoInicial(2000);
		simulacao.setAnoFinal(2010);
	}

	public static void main(String[] args) {
		executa(new CallITM_CallOTM_PutATM());
	}
}
