package sata.auto.estrategia;

import static sata.auto.operacao.Operacao.PRIMEIRO_DIA;
import static sata.auto.operacao.Operacao.ULTIMO_DIA;
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
		
		simulacao.getOperacoes().add(new Compra(acao, 1, PRIMEIRO_DIA));
		simulacao.getOperacoes().add(new Compra(putATM, 1, PRIMEIRO_DIA));
		simulacao.getOperacoes().add(new Venda(callOTM, 2, PRIMEIRO_DIA));
		
		simulacao.getOperacoes().add(new Venda(acao, 0, ULTIMO_DIA));
		simulacao.getOperacoes().add(new Venda(putATM, 0, ULTIMO_DIA));
		simulacao.getOperacoes().add(new Compra(callOTM, 1, ULTIMO_DIA));
		
		simulacao.setAnoInicial(2008);
		simulacao.setAnoFinal(2010);
	}
	
	public static void main(String[] args) {
		executa(new PutATM_CallOTM());
	}
}
