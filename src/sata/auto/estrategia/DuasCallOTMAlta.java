package sata.auto.estrategia;

import sata.auto.enums.Atributo;
import sata.auto.enums.Operador;
import sata.auto.enums.TipoCalculoValorInvestido;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Stop;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.simulacao.Simulacao;

public class DuasCallOTMAlta extends Estrategia {
	
	@Override
	public void prepara() {
		anoInicial = 2000;
		anoFinal = 2011;
		Acao acao = new Acao("PETR4");
		
		Simulacao simulacao = new Simulacao(
				new Venda (new Call(acao, 1, true)),
				new Compra(new Call(acao, 3, true)));
		
		simulacao.setStop(new Stop(Atributo.PERCENTUAL_OPERACAO, Operador.MENOR_IGUAL, -20));
		simulacao.setTipoCalculoValorInvestido(TipoCalculoValorInvestido.DIFERENCA_STRIKES);
		
		simulacoes.add(simulacao);
	}
	
	public static void main(String[] args) {
		new DuasCallOTMAlta().executa(true, true, true, true, false);
	}
}
