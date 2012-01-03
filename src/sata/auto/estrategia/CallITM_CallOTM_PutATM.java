package sata.auto.estrategia;

import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;
import sata.auto.simulacao.Simulacao;

public class CallITM_CallOTM_PutATM extends Estrategia {

	@Override
	public void prepara() {
		anoInicial = 2000;
		anoFinal = 2011;
		Acao acao = new Acao("PETR4");
		
		simulacoes.add(new Simulacao(new Compra(new Call(acao, -2)),
									 new Venda (new Call(acao, 2)),
									 new Compra(new Put(acao, 0))));
	}

	public static void main(String[] args) {
		new CallITM_CallOTM_PutATM().executa(TipoRelatorio.COMPLETO);
	}
}
