package sata.auto.estrategia;

import sata.auto.enums.TipoRelatorio;
import sata.auto.operacao.Compra;
import sata.auto.operacao.Venda;
import sata.auto.operacao.ativo.Acao;
import sata.auto.operacao.ativo.Call;
import sata.auto.operacao.ativo.Put;
import sata.auto.simulacao.Simulacao;

public class AcaoCallOTMPutITM extends Estrategia {
	
	@Override
	public void prepara() {
		anoInicial = 2008;
		anoFinal = 2011;
		Acao acao = new Acao("PETR4");
		
		simulacoes.add(new Simulacao(new Compra(acao)));
		simulacoes.add(new Simulacao(new Venda (new Call(acao, 2))));
		simulacoes.add(new Simulacao(new Compra(new Put (acao, -2))));
	}
	
	public static void main(String[] args) {
		new AcaoCallOTMPutITM().executa(TipoRelatorio.REINVESTIMENTO);
	}
}
