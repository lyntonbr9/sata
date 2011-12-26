package sata.auto.estrategia;

import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.simulacao.Resultado;

public class DuasCallOTMAlta extends Estrategia {
	
	@Override
	public void executa() {
		Acao acao = new Acao("PETR4");
		Call callOTM8 = new Call(acao, 8);
		Call callOTM10 = new Call(acao, 10);
		
		simulacao.getOperacoes().add(new Venda(callOTM8, 1, null));
		simulacao.getOperacoes().add(new Compra(callOTM10, 1, null));
		
		simulacao.setAnoInicial(2000);
		simulacao.setAnoFinal(2011);
		simulacao.setTipoCalculoValorInvestido(Resultado.DIFERENCA_STRIKES);
	}
	
	public static void main(String[] args) {
		executa(new DuasCallOTMAlta());
	}
}
