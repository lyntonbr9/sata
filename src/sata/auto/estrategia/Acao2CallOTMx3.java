package sata.auto.estrategia;

import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.simulacao.Simulacao;

public class Acao2CallOTMx3 extends Estrategia {
	
	@Override
	public void prepara() {
		anoInicial = 2000;
		anoFinal = 2011;
		Acao acao = new Acao("PETR4");
		
		simulacoes.add(new Simulacao(new Compra(acao)));
		simulacoes.add(new Simulacao(new Venda(3, new Call(acao, 4)), 
									 new Compra(3, new Call(acao, 6))));
	}
	
	public static void main(String[] args) {
		new Acao2CallOTMx3().executa(true, true, true, true, false);
	}
}
